package net.cubespace.CloudChat.Module.IRC.Bot;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.IRC.CommandManager;
import net.cubespace.CloudChat.Module.IRC.Commands.Players;
import net.cubespace.CloudChat.Module.IRC.Format.IrcToMCFormat;
import net.cubespace.CloudChat.Module.IRC.Format.MCToIrcFormat;
import net.cubespace.CloudChat.Module.IRC.Format.NickchangeFormatter;
import net.cubespace.CloudChat.Module.IRC.IRCManager;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Bot extends PircBot implements Runnable {
    private CloudChatPlugin plugin;
    private CommandManager cmdManager;
    private IRCManager ircManager;
    private IRC ircConfig;
    private ChannelManager channelManager;
    private ScheduledTask botTask;

    public Bot(IRCModule ircModule, CloudChatPlugin plugin) {
        this.plugin = plugin;
        ircConfig = plugin.getConfigManager().getConfig("irc");
        channelManager = plugin.getManagerRegistry().getManager("channelManager");

        setName(ircConfig.Name);

        cmdManager = new CommandManager();
        cmdManager.registerCommand("players", new Players(ircModule, plugin));

        ircManager = new IRCManager();

        botTask = plugin.getProxy().getScheduler().runAsync(plugin, this);
    }

    /**
     * If a new Bot gets run, connect him to the IRC Network and let him join the configured Channels
     */
    @Override
    public void run() {
        // Connect to the IRC server.
        try {
            connect(ircConfig.Host);
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
            if(!ircManager.hasJoined(channel)) {
                joinChannel(channel);
                sendToChannel(ircConfig.JoinMessage, channel);
                ircManager.addJoinedChannel(channel);
            }
        }
    }

    /**
     * Shuts the bot down
     */
    public void shutdown() {
        for(String channel : ircManager.getJoinedChannels()) {
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
            ircManager.addJoinedChannel(user.getNick(), channel);
        }
    }

    /**
     *
     * @param message
     * @param channel
     */
    public synchronized void sendToChannel(String message, String channel) {
        sendMessage(channel, MCToIrcFormat.translateString(message));
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
            relayMessage(sender, target, ircConfig.Relay_ActionPrefix + sender + " " + action, false);
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
                relayMessage(sender, channel, ircConfig.Relay_JoinMessage, false);
            } else {
                plugin.getPluginLogger().info("Bot joined " + channel);
            }
        }

        ircManager.addJoinedChannel(sender, channel);
    }

    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if(ircConfig.Relay_Nickchange) {
            List<String> joinedChannels = ircManager.getJoinedChannels(oldNick);

            for(String channel : joinedChannels) {
                relayMessage(oldNick, channel, NickchangeFormatter.format(ircConfig.Relay_NickchangeMessage, oldNick, newNick), false);
                ircManager.addJoinedChannel(newNick, channel);
            }

            ircManager.removeJoinedChannels(oldNick);
        }
    }

    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        if(ircConfig.Relay_Quit) {
            List<String> joinedChannels = ircManager.getJoinedChannels(sourceNick);

            for(String channel : joinedChannels)
                relayMessage(sourceNick, channel, ircConfig.Relay_QuitMessage, false);
        }

        ircManager.removeJoinedChannels(sourceNick);
    }

    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        IRCSender ircSender = new IRCSender();
        ircSender.setNick(sender);
        ircSender.setChannel(sender);

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            sendMessage(sender, "Unknown Command");
        }
    }

    private void relayMessage(String sender, String channel, String message, boolean useChannelFormat) {
        IRCSender ircSender = new IRCSender();
        ircSender.setNick(ircConfig.IngameName + " " + sender);
        ircSender.setChannel(channel);

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            for(Map.Entry<String, String> channelEntry : ircConfig.Channels.entries()) {
                if(channelEntry.getValue().equals(channel)) {
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
}
