package net.cubespace.CloudChat.Command.Binder;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 18:25
 */
public class Binder extends Command {
    protected String commandName;
    protected CloudChatPlugin plugin;

    public Binder(CloudChatPlugin plugin, String name, String permission, String... aliases) {
        super(name, permission, aliases);

        this.plugin = plugin;
        this.commandName = name;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        plugin.getCommandExecutor().onCommand(commandSender, commandName, strings);
    }
}
