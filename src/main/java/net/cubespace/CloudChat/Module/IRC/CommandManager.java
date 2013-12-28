package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.Module.IRC.Commands.Command;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Fabian on 29.11.13.
 */
public class CommandManager {
    private HashMap<String, Command> executors = new HashMap<String, Command>();

    public void registerCommand(String command, Command executor) {
        executors.put(command, executor);
    }

    public boolean dispatchCommand(IRCSender sender, String command) {
        if(!command.startsWith(".")) {
            return false;
        }

        command = command.substring(1);

        String[] cmd = command.split(" ");

        //We got .
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
