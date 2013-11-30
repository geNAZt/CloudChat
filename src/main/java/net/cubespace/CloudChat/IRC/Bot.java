package net.cubespace.CloudChat.IRC;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.CloudChatIRCChatEvent;
import net.cubespace.CloudChat.IRC.Commands.Players;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Fabian on 29.11.13.
 */
public class Bot extends PircBot implements Runnable {
    private CloudChatPlugin plugin;
    private CommandManager cmdManager;

    public Bot(CloudChatPlugin plugin) {
        this.plugin = plugin;

        setName(plugin.getIrcConfig().Name);

        cmdManager = new CommandManager();
        cmdManager.registerCommand("players", new Players(plugin));
    }

    @Override
    public void run() {
        // Connect to the IRC server.
        try {
            connect(plugin.getIrcConfig().Host);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IrcException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if(plugin.getIrcConfig().Channels.size() == 0) {
            plugin.getLogger().warning("IRC Bot has no channels to join to");
            disconnect();
            return;
        }

        for(String channel : plugin.getIrcConfig().Channels.values()) {
            joinChannel(channel);
            sendToChannel(plugin.getIrcConfig().JoinMessage, channel);
        }
    }

    public synchronized void sendToChannel(String message, String channel) {
        sendMessage(channel, message);
    }

    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        IRCSender ircSender = new IRCSender();
        ircSender.setNick(plugin.getIrcConfig().IngameName + " " + sender);
        ircSender.setChannel(channel);

        System.out.println(new Date().toString() + " " + ircSender.getNick() + ": " + message);

        if(!cmdManager.dispatchCommand(ircSender, message)) {
            plugin.getProxy().getPluginManager().callEvent(new CloudChatIRCChatEvent(message, ircSender));
        }
    }

    protected void onPrivateMessage(String sender, String login, String hostname, String message) {

    }
}
