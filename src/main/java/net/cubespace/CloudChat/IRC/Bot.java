package net.cubespace.CloudChat.IRC;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.CloudChatIRCChatEvent;
import net.cubespace.CloudChat.IRC.Commands.Players;
import net.cubespace.CloudChat.Manager.IRCManager;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Fabian on 29.11.13.
 */
public class Bot extends PircBot implements Runnable {
    private CloudChatPlugin plugin;
    private CommandManager cmdManager;
    private IRCManager ircManager;

    public Bot(CloudChatPlugin plugin) {
        this.plugin = plugin;

        setName(plugin.getIrcConfig().Name);

        cmdManager = new CommandManager();
        cmdManager.registerCommand("players", new Players(plugin));

        ircManager = new IRCManager();
    }

    /**
     * If a new Bot gets run, connect him to the IRC Network and let him join the configured Channels
     */
    @Override
    public void run() {
        // Connect to the IRC server.
        try {
            connect(plugin.getIrcConfig().Host);
        } catch (IrcException | IOException e) {
            e.printStackTrace();
        }

        if(plugin.getIrcConfig().Channels.size() == 0) {
            plugin.getLogger().warning("IRC Bot has no channels to join to");
            disconnect();
            return;
        }

        for(String channel : plugin.getIrcConfig().Channels.values()) {
            if(!ircManager.hasJoined(channel)) {
                joinChannel(channel);
                sendToChannel(plugin.getIrcConfig().JoinMessage, channel);
                ircManager.addJoinedChannel(channel);
            }
        }
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
        sendMessage(channel, message);
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
        relayMessage(sender, channel, message, true);
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
        if(plugin.getIrcConfig().Relay_Action) {
            relayMessage(sender, target, plugin.getIrcConfig().Relay_ActionPrefix + sender + " " + action, false);
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
        if(plugin.getIrcConfig().Relay_Join) {
            if(!sender.equals(plugin.getIrcConfig().Name)) {
                relayMessage(sender, channel, plugin.getIrcConfig().Relay_JoinMessage, false);
            }
        }

        ircManager.addJoinedChannel(sender, channel);
    }

    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if(plugin.getIrcConfig().Relay_Nickchange) {
            List<String> joinedChannels = ircManager.getJoinedChannels(oldNick);

            for(String channel : joinedChannels) {
                relayMessage(oldNick, channel, NickchangeFormatter.format(plugin.getIrcConfig().Relay_NickchangeMessage, oldNick, newNick), false);
                ircManager.addJoinedChannel(newNick, channel);
            }

            ircManager.removeJoinedChannels(oldNick);
        }
    }

    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        if(plugin.getIrcConfig().Relay_Quit) {
            List<String> joinedChannels = ircManager.getJoinedChannels(sourceNick);

            for(String channel : joinedChannels)
                relayMessage(sourceNick, channel, plugin.getIrcConfig().Relay_QuitMessage, false);
        }

        ircManager.removeJoinedChannels(sourceNick);
    }

    private void relayMessage(String sender, String channel, String message, boolean format) {
        IRCSender ircSender = new IRCSender();
        ircSender.setNick(plugin.getIrcConfig().IngameName + " " + sender);
        ircSender.setChannel(channel);

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            plugin.getProxy().getPluginManager().callEvent(new CloudChatIRCChatEvent(message, ircSender, format));
        }
    }
}
