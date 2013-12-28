package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;

/**
 * Created by Fabian on 29.11.13.
 */
public class Reload implements CLICommand {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public Reload(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @Command(command = "cc:reload", arguments = 0)
    public void reloadCommand(CommandSender sender, String[] args) {
        channelManager.reload();
        plugin.getConfigManager().reloadAll();
        sender.sendMessage("Reloaded all Channels and IRC Config");
    }
}
