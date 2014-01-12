package net.cubespace.CloudChat.Module.IRC.Bot;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.IRC.CommandManager;
import net.cubespace.CloudChat.Module.IRC.Commands.Message;
import net.cubespace.CloudChat.Module.IRC.Commands.Mute;
import net.cubespace.CloudChat.Module.IRC.Commands.Players;
import net.cubespace.CloudChat.Module.IRC.Commands.Scmd;
import net.cubespace.CloudChat.Module.IRC.Commands.Unmute;
import net.cubespace.CloudChat.Module.IRC.Format.IrcToMCFormat;
import net.cubespace.CloudChat.Module.IRC.Format.MCToIrcFormat;
import net.cubespace.CloudChat.Module.IRC.Format.NickchangeFormatter;
import net.cubespace.CloudChat.Module.IRC.IRCManager;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.CloudChat.Module.IRC.PMSession;
import net.cubespace.CloudChat.Module.IRC.Permission.WhoisResolver;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Bot extends PircBot implements Runnable {
    private CubespacePlugin plugin;
    private CommandManager cmdManager;
    private IRCManager ircManager;
    private IRC ircConfig;
    private ChannelManager channelManager;
    private ScheduledTask botTask;
    private IRCModule ircModule;
    private LinkedBlockingQueue<String> whoisQueue = new LinkedBlockingQueue<>();
    private boolean shutdown = false;

    public Bot(IRCModule ircModule, final CubespacePlugin plugin) {
        this.plugin = plugin;
        this.ircModule = ircModule;
        ircConfig = plugin.getConfigManager().getConfig("irc");
        channelManager = plugin.getManagerRegistry().getManager("channelManager");

        plugin.getPluginLogger().info("Setting Botname to " + ircConfig.Name);
        setName(ircConfig.Name);
        try {
            setEncoding("UTF8");
        } catch (UnsupportedEncodingException e) {
            plugin.getPluginLogger().warn("Could not set IRC Bot encoding");
        }

        cmdManager = new CommandManager();
        cmdManager.registerCommand("players", new Players(ircModule, plugin));
        cmdManager.registerCommand("mute", new Mute(ircModule, plugin));
        cmdManager.registerCommand("unmute", new Unmute(ircModule, plugin));
        cmdManager.registerCommand("message", new Message(ircModule, plugin));
        cmdManager.registerCommand("scmd", new Scmd(ircModule, plugin));

        ircManager = new IRCManager(plugin, ircModule);

        botTask = plugin.getProxy().getScheduler().runAsync(plugin, this);

        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        String nick = whoisQueue.take();

                        Thread.sleep(500);

                        if(ircManager.isNickOnline(nick)) {
                            sendRawLine("WHOIS " + nick);
                        }

                        Thread.sleep(500);
                    }
                } catch(InterruptedException e) {
                    plugin.getPluginLogger().warn("Got interrupted in the Whois Checking Thread", e);
                }
            }
        });
    }

    /**
     * If a new Bot gets run, connect him to the IRC Network and let him join the configured Channels
     */
    @Override
    public void run() {
        // Connect to the IRC server.
        try {
            plugin.getPluginLogger().info("Connecting to IRC");
            connect(ircConfig.Host);
            plugin.getPluginLogger().info("Connected to IRC");
        } catch (IrcException | IOException e) {
            plugin.getPluginLogger().warn("Error in connecting the IRC Bot", e);
            throw new RuntimeException();
        }

        if(ircConfig.Channels.size() == 0) {
            plugin.getPluginLogger().warn("IRC Bot has no channels to join to");
            disconnect();
            return;
        }

        for(String channel : ircConfig.Channels.values()) {
            if(!ircManager.botHasJoined(channel)) {
                plugin.getPluginLogger().debug("Attempt to join Channel " + channel);

                if(channel.contains(" ")) {
                    String[] split = channel.split(" ");
                    channel = split[0];
                    joinChannel(channel, split[1]);
                    sendToChannel(ircConfig.JoinMessage, channel);
                    ircManager.botJoinedChannel(channel);
                } else {
                    joinChannel(channel);
                    sendToChannel(ircConfig.JoinMessage, channel);
                    ircManager.botJoinedChannel(channel);
                }
            }
        }
    }

    /**
     * Shuts the bot down
     */
    public void shutdown() {
        shutdown = true;

        plugin.getPluginLogger().info("Shutting IRC Bot down");
        for(String channel : ircManager.getBotJoinedChannels()) {
            sendToChannel(ircConfig.LeaveMessage, channel);
        }

        disconnect();
        botTask.cancel();
    }

    /**
     * If the Bot joins a new Channel he gets the Userlist from the IRC Network
     * @param channel Channel the bot joined
     * @param users The Users inside the Channel
     */
    protected void onUserList(String channel, User[] users) {
        for(User user : users) {
            plugin.getPluginLogger().debug("Adding user " + user.getNick() + " to the IRC User list for Channel " + channel);

            if(!ircManager.isNickOnline(user.getNick())) {
                ircManager.newPMSession(user.getNick());

                if(!ircManager.isResolving(user.getNick())) {
                    ircManager.addWhoIsResolver(user.getNick(), new WhoisResolver());
                    whoisQueue.add(user.getNick());
                }
            }

            ircManager.addJoinedChannel(user.getNick(), channel);
        }
    }

    /**
     *
     * @param message
     * @param channel
     */
    public synchronized void sendToChannel(String message, String channel) {
        if(channel.contains(" ")) {
            channel = channel.split(" ")[0];
        }

        plugin.getPluginLogger().debug("Got Minecraft Message sending it to " + channel + ": " + message);
        sendMessage(channel, MCToIrcFormat.translateString(message));
    }

    protected void onServerResponse(int code, String response) {
        plugin.getPluginLogger().debug("Got server Message: " + code + " " + response);
        ircManager.newWhoisLine(code, response);
    }

    /**
     * If a IRC Message comes in relay it into the Minecraft Chat
     *
     * @param channel
     * @param sender
     * @param login
     * @param hostname
     * @param message
     */
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        plugin.getPluginLogger().debug("Got IRC Message from " + channel + ": " + message);
        relayMessage(sender, channel, IrcToMCFormat.translateString(message), true);
    }

    /**
     * If a User does a ACTION in IRC check if we relay it into Minecraft
     *
     * @param sender
     * @param login
     * @param hostname
     * @param target
     * @param action
     */
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        if(ircConfig.Relay_Action) {
            plugin.getPluginLogger().debug("Relaying a Action from IRC: " + sender + " " + action);
            relayMessage(sender, target, ircConfig.Relay_ActionPrefix + sender + " " + IrcToMCFormat.translateString(action), false);
        }
    }

    /**
     * When a new User joins a Channel this method gets called
     *
     * @param channel
     * @param sender
     * @param login
     * @param hostname
     */
    protected void onJoin(String channel, String sender, String login, String hostname) {
        if(ircConfig.Relay_Join) {
            if(!sender.equals(ircConfig.Name)) {
                plugin.getPluginLogger().debug("Adding user " + sender + " to the IRC User list for Channel " + channel);
                relayMessage(sender, channel, ircConfig.Relay_JoinMessage, false);

                if(!ircManager.isNickOnline(sender)) {
                    ircManager.newPMSession(sender);

                    if(!ircManager.isResolving(sender)) {
                        ircManager.addWhoIsResolver(sender, new WhoisResolver());
                        whoisQueue.add(sender);
                    }
                }
            } else {
                plugin.getPluginLogger().info("Bot joined " + channel);
            }
        }

        ircManager.addJoinedChannel(sender, channel);
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        if(recipientNick.equals(ircConfig.Name)) {
            plugin.getPluginLogger().warn("We got kicked out of a Channel " + channel + " by " + kickerNick + "(" + reason + ")");
            ircManager.removeBotJoinedChannel(channel);
            ircManager.removeChannel(channel);
            joinChannel(channel);
        } else {
            ircManager.removeChannelFromNick(recipientNick, channel);

            if(!ircManager.isNickOnline(recipientNick)) {
                ircManager.removePMSession(recipientNick);
                ircManager.removeWhois(recipientNick);
                ircManager.removeScmdSessions(recipientNick);
            }
        }
    }

    public void onDisconnect() {
        ircManager = new IRCManager(plugin, ircModule);

        int tries = 0;

        // Be sure the Bot stays connected
        while (!isConnected() && !shutdown) {
            try {
                plugin.getPluginLogger().info("Trying to reconnect to IRC");
                tries++;

                if(tries < 5) {
                    reconnect();
                } else {
                    plugin.getPluginLogger().warn("Maximum of 5 Tries reached. Hard reset of the Bot");
                    shutdown();

                    plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                        @Override
                        public void run() {
                            ircModule.setIrcBot(new Bot(ircModule, plugin));
                        }
                    }, 5, TimeUnit.SECONDS);

                    return;
                }
            } catch (Exception e) {
                plugin.getPluginLogger().error("Could not reconnect IRC Bot", e);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                plugin.getPluginLogger().error("Got interrupted while sleeping... Y U do dis to me :(", e);
            }
        }
    }

    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if(ircConfig.Relay_Nickchange) {
            plugin.getPluginLogger().debug("IRC Nick changed from " + oldNick + " to " + newNick);
            List<String> joinedChannels = ircManager.getJoinedChannels(oldNick);

            for(String channel : joinedChannels) {
                relayMessage(oldNick, channel, NickchangeFormatter.format(ircConfig.Relay_NickchangeMessage, oldNick, newNick), false);
                ircManager.addJoinedChannel(newNick, channel);
            }

            ircManager.removeJoinedChannels(oldNick);
            if(!ircManager.moveWhois(oldNick, newNick)) {
                ircManager.addWhoIsResolver(newNick, new WhoisResolver());
                whoisQueue.add(newNick);
            }

            ircManager.removePMSession(oldNick);
            ircManager.newPMSession(newNick);

            if(ircManager.hasScmdSessions(oldNick)) {
                ircManager.moveScmdSessions(oldNick, newNick);
            }
        }
    }

    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        if(ircConfig.Relay_Quit) {
            plugin.getPluginLogger().debug("IRC User " + sourceNick + " disconnected");

            for(String channel : ircManager.getJoinedChannels(sourceNick))
                relayMessage(sourceNick, channel, ircConfig.Relay_QuitMessage, false);
        }

        ircManager.removeJoinedChannels(sourceNick);
        ircManager.removeWhois(sourceNick);
        ircManager.removePMSession(sourceNick);
        ircManager.removeScmdSessions(sourceNick);
    }

    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        plugin.getPluginLogger().debug("IRC PM to bot from " + sender + ": " + message);

        IRCSender ircSender = new IRCSender();
        ircSender.setNick(sender);
        ircSender.setChannel(sender);
        ircSender.setRawNick(sender);

        //Check if Whois is resolved
        if(!ircManager.isResolving(sender)) {
            ircManager.addWhoIsResolver(sender, new WhoisResolver());
            whoisQueue.add(sender);
        }

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            //This can only be used as PM Channel
            if(ircManager.hasPmSession(sender) && !ircManager.getPmSession(sender).getTo().equals("")) {
                PMSession pmSession = ircManager.getPmSession(sender);
                PMEvent pmEvent = new PMEvent(ircConfig.IngameName + " " + sender, pmSession.getTo(), message);
                plugin.getAsyncEventBus().callEvent(pmEvent);
            } else {
                sendMessage(sender, "Unknown Command and no PM Session opened");
            }
        }
    }

    private void relayMessage(String sender, String channel, String message, boolean useChannelFormat) {
        plugin.getPluginLogger().debug("Got a new relay Message from IRC: " + sender + ": " + message);

        IRCSender ircSender = new IRCSender();
        ircSender.setNick(ircConfig.IngameName + " " + sender);
        ircSender.setChannel(channel);
        ircSender.setRawNick(sender);

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            plugin.getPluginLogger().debug("Message is not a Command");
            for(Map.Entry<String, String> channelEntry : ircConfig.Channels.entries()) {
                if(channelEntry.getValue().contains(channel)) {
                    ChannelDatabase channelDatabase = channelManager.get(channelEntry.getKey());
                    PlayerDatabase ircDatabase = new PlayerDatabase(plugin, "IRC");
                    ircDatabase.Nick = ircConfig.IngameName + " " + sender;

                    Sender sender1 = new Sender("IRC", channelDatabase, ircDatabase);

                    String sendMessage = message;
                    if(useChannelFormat) {
                        sendMessage = ircConfig.Relay_Message.replace("%message", message);
                    }

                    plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender1, sendMessage));
                }
            }
        }
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }
}
