package net.cubespace.CloudChat.Module.Mail;

import net.cubespace.CloudChat.Command.Binder.PlayerNameBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.Mail.Command.Mail;
import net.cubespace.CloudChat.Module.Mail.Listener.PlayerJoinListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class MailModule extends Module {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if (!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("mail"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("mail"), PlayerNameBinder.class, commandAliases.Mail.toArray(new String[0]));
            plugin.getCommandExecutor().add(this, new Mail(plugin));
            plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));
        }
    }

    @Override
    public void onDisable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if (plugin.getBindManager().isBound(commandAliases.BaseCommands.get("mail"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("mail"));
            plugin.getCommandExecutor().remove(this);
            plugin.getAsyncEventBus().removeListener(this);
        }
    }
}
