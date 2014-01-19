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
    private boolean loaded = false;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("mail")) {
            CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");
            plugin.getBindManager().bind("mail", PlayerNameBinder.class, commandAliases.Mail.toArray(new String[0]));

            plugin.getCommandExecutor().add(this, new Mail(plugin));

            plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));

            loaded = true;
        }
    }

    @Override
    public void onDisable() {
        if(loaded) {
            loaded = false;

            plugin.getBindManager().unbind("mail");

            plugin.getCommandExecutor().remove(this);

            plugin.getAsyncEventBus().removeListener(this);
        }
    }
}
