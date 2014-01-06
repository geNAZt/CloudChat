package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;


public class Reload implements CLICommand {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public Reload(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @Command(command = "cc:reload", arguments = 0)
    public void reloadCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        channelManager.reload();
        plugin.getConfigManager().reloadAll();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Reload_Success)).send(sender);
    }
}
