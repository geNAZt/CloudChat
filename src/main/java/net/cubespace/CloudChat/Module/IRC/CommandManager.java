package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.IRC.Commands.Command;
import net.cubespace.lib.CubespacePlugin;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
    private HashMap<String, Command> executors = new HashMap<String, Command>();
    private CubespacePlugin plugin;

    public CommandManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }


    public void registerCommand(String command, Command executor) {
        executors.put(command, executor);
    }

    public boolean dispatchCommand(IRCSender sender, String command) {
        IRC config = plugin.getConfigManager().getConfig("irc");

        if(!command.startsWith(config.Command_Prefix)) {
            return false;
        }

        command = command.substring(config.Command_Prefix.length());

        String[] cmd = command.split(" ");

        //We only got the Command prefix :(
        if(cmd.length == 0) {
            return false;
        }

        if(executors.containsKey(cmd[0])) {
            return executors.get(cmd[0]).execute(sender, Arrays.copyOfRange(cmd, 1, cmd.length));
        } else {
            //No command given
            return false;
        }
    }
}
