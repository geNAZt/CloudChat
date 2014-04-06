package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.IRC.Commands.Command;
import net.cubespace.lib.CubespacePlugin;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> executors = new HashMap<>();
    private final CubespacePlugin plugin;

    public CommandManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }


    public void registerCommand(String command, Command executor) {
        executors.put(command, executor);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean dispatchCommand(IRCSender sender, String command) {
        IRC config = plugin.getConfigManager().getConfig("irc");

        if(!command.startsWith(config.Command_Prefix)) {
            plugin.getPluginLogger().debug("Message does not start with a Command prefix");
            return false;
        }

        command = command.substring(config.Command_Prefix.length());

        String[] cmd = command.split(" ");

        //We only got the Command prefix :(
        if(cmd.length == 0) {
            return false;
        }

        if(executors.containsKey(cmd[0])) {
            plugin.getPluginLogger().info("Executing IRC Command " + cmd[0]);
            return executors.get(cmd[0]).execute(sender, Arrays.copyOfRange(cmd, 1, cmd.length));
        } else {
            plugin.getPluginLogger().debug("Command given was not found");
            return false;
        }
    }
}
