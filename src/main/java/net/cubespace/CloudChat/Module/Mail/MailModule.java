package net.cubespace.CloudChat.Module.Mail;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.PlayerNameBinder;
import net.cubespace.CloudChat.Module.Mail.Command.Mail;
import net.cubespace.CloudChat.Module.Mail.Listener.PlayerJoinListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 04.01.14 17:15
 */
public class MailModule {
    public MailModule(CloudChatPlugin plugin) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerNameBinder(plugin, "mail"));

        plugin.getCommandExecutor().add(new Mail(plugin));

        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(plugin));
    }
}
