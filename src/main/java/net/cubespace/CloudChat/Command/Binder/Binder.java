package net.cubespace.CloudChat.Command.Binder;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Binder extends Command {
    protected String commandName;
    protected CloudChatPlugin plugin;

    public Binder(CloudChatPlugin plugin, String name, String... aliases) {
        super(name, null, aliases);

        this.plugin = plugin;
        this.commandName = name;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender.hasPermission("cloudchat.command.*") || commandSender.hasPermission("cloudchat.command." + commandName.replace(":", "."))) {
            plugin.getCommandExecutor().onCommand(commandSender, commandName, strings);
        } else {
            commandSender.sendMessage("You don't have the Permission to use this Command");
        }
    }
}
