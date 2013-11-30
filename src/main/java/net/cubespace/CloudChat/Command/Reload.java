package net.cubespace.CloudChat.Command;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.md_5.bungee.api.CommandSender;

/**
 * Created by Fabian on 29.11.13.
 */
public class Reload implements CLICommand {
    private CloudChatPlugin plugin;

    public Reload(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "cc:reload", arguments = 0)
    public void reloadCommand(CommandSender sender, String[] args) {
        plugin.getChannelManager().reload();

        try {
            plugin.getIrcConfig().reload();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        sender.sendMessage("Reloaded all Channels and IRC Config");
    }
}
