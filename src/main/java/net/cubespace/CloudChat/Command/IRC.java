package net.cubespace.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.IRC.Bot;
import net.md_5.bungee.api.CommandSender;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fabian on 29.11.13.
 */
public class IRC implements CLICommand {
    private CloudChatPlugin plugin;

    public IRC(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "irc:reconnect", arguments = 0)
    public void ircReconnectCommand(CommandSender sender, String[] args) {
        for(Map.Entry<String, String> channel : plugin.getIrcConfig().Channels.entries()) {
            plugin.getIrcBot().sendToChannel(plugin.getIrcConfig().LeaveMessage, channel.getValue());
        }


        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getIrcBot().disconnect();
                plugin.getBotTask().cancel();

                Bot ircBot = new Bot(plugin);

                plugin.setIrcBot(ircBot);
                plugin.setBotTask(plugin.getProxy().getScheduler().runAsync(plugin, ircBot));
            }
        }, 2, TimeUnit.SECONDS);
    }
}
