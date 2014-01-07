package net.cubespace.CloudChat.Module.CloudChat;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PermissionlessBinder;
import net.cubespace.CloudChat.Module.CloudChat.Command.Admin;
import net.cubespace.CloudChat.Module.CloudChat.Command.Playermenu;
import net.cubespace.CloudChat.Module.CloudChat.Command.Reload;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 21:11
 */
public class CloudChatModule {
    public CloudChatModule(CloudChatPlugin plugin) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "cc:reload"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "cc:report"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new PermissionlessBinder(plugin, "cc:playermenu"));

        plugin.getCommandExecutor().add(new Reload(plugin));
        plugin.getCommandExecutor().add(new Admin(plugin));
        plugin.getCommandExecutor().add(new Playermenu(plugin));
    }
}
