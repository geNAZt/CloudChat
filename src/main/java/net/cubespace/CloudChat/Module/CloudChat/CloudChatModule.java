package net.cubespace.CloudChat.Module.CloudChat;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PermissionlessBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Module.CloudChat.Command.Admin;
import net.cubespace.CloudChat.Module.CloudChat.Command.Playermenu;
import net.cubespace.CloudChat.Module.CloudChat.Command.Reload;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CloudChatModule extends Module {
    public CloudChatModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        //Bind Commands
        plugin.getBindManager().bind("cc:reload", Binder.class, commandAliases.CCReload);
        plugin.getBindManager().bind("cc:report", Binder.class, commandAliases.CCReport);
        plugin.getBindManager().bind("cc:playermenu", PermissionlessBinder.class);

        plugin.getCommandExecutor().add(this, new Reload(plugin));
        plugin.getCommandExecutor().add(this, new Admin(plugin));
        plugin.getCommandExecutor().add(this, new Playermenu(plugin));
    }

    @Override
    public void onDisable() {
        //Unbind Commands
        plugin.getBindManager().unbind("cc:reload");
        plugin.getBindManager().unbind("cc:report");
        plugin.getBindManager().unbind("cc:playermenu");

        plugin.getCommandExecutor().remove(this);
    }
}
