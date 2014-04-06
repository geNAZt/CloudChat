package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;


public class Reload implements CLICommand {
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;

    public Reload(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @SuppressWarnings("unused")
    @Command(command = "cc:reload", arguments = 0)
    public void reloadCommand(CommandSender sender, String[] args) {
        channelManager.reload();
        plugin.getConfigManager().reloadAll();

        Messages messages = plugin.getConfigManager().getConfig("messages");
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Reload_Success)).send(sender);
    }
}
