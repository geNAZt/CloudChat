package net.cubespace.CloudChat.Command.Binder;

import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PermissionlessBinder extends Command {
    private final String commandName;
    private final CubespacePlugin plugin;

    public PermissionlessBinder(CubespacePlugin plugin, String name, String... aliases) {
        super(name, null, aliases);

        this.plugin = plugin;
        this.commandName = name;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        plugin.getCommandExecutor().onCommand(commandSender, commandName, strings);
    }
}
