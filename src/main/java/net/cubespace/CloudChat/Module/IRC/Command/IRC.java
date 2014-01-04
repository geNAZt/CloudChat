package net.cubespace.CloudChat.Module.IRC.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.IRC.Bot.Bot;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fabian on 29.11.13.
 */
public class IRC implements CLICommand {
    private CloudChatPlugin plugin;
    private IRCModule ircModule;

    public IRC(IRCModule ircModule, CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.ircModule = ircModule;
    }

    @Command(command = "irc:reconnect", arguments = 0)
    public void ircReconnectCommand(CommandSender sender, String[] args) {
        ircModule.getIrcBot().shutdown();

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                ircModule.setIrcBot(new Bot(ircModule, plugin));
            }
        }, 5, TimeUnit.SECONDS);
    }
}
