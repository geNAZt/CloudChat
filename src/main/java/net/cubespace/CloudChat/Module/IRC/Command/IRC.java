package net.cubespace.CloudChat.Module.IRC.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.IRC.Bot.Bot;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.TimeUnit;

public class IRC implements CLICommand {
    private final CubespacePlugin plugin;
    private final IRCModule ircModule;

    public IRC(IRCModule ircModule, CubespacePlugin plugin) {
        this.plugin = plugin;
        this.ircModule = ircModule;
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "irc:reconnect", arguments = 0)
    public void ircReconnectCommand(final CommandSender sender, String[] args) {
        final Messages messages = plugin.getConfigManager().getConfig("messages");

        ircModule.getIrcBot().shutdown();

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                ircModule.setIrcBot(new Bot(ircModule, plugin));

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_IRC_Reconnect_Success)).send(sender);
            }
        }, 5, TimeUnit.SECONDS);
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "irc:mute", arguments = 0)
    public void ircMuteCommand(final CommandSender sender, String[] args) {
        final Messages messages = plugin.getConfigManager().getConfig("messages");

        ircModule.getIrcBot().setMuted(true);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_IRC_Mute_Success)).send(sender);
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "irc:unmute", arguments = 0)
    public void ircUnmuteCommand(final CommandSender sender, String[] args) {
        final Messages messages = plugin.getConfigManager().getConfig("messages");

        ircModule.getIrcBot().setMuted(false);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_IRC_UnMute_Success)).send(sender);
    }
}
