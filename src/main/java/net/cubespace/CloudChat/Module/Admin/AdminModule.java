package net.cubespace.CloudChat.Module.Admin;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Module.Admin.Command.Admin;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 07:01
 */
public class AdminModule {
    public AdminModule(CloudChatPlugin plugin) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "cc:report"));
        plugin.getCommandExecutor().add(new Admin(plugin));
    }
}
