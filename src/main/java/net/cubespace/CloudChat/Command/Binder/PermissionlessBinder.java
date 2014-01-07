package net.cubespace.CloudChat.Command.Binder;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PermissionlessBinder extends Command {
    protected String commandName;
    protected CloudChatPlugin plugin;

    public PermissionlessBinder(CloudChatPlugin plugin, String name, String... aliases) {
        super(name, null, aliases);

        this.plugin = plugin;
        this.commandName = name;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        plugin.getCommandExecutor().onCommand(commandSender, commandName, strings);
    }
}
