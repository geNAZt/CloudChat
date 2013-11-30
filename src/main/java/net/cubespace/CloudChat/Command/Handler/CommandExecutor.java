package net.cubespace.CloudChat.Command.Handler;

import net.md_5.bungee.api.CommandSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The type Command executor.
 */
public class CommandExecutor {
    private HashMap<String, CommandStruct> commandMap = new HashMap<String, CommandStruct>();

    /**
     * Add a new CLICommand into the executor
     *
     * @param cliCommand the cli command
     */
    public void add(CLICommand cliCommand) {
        for (Method method : cliCommand.getClass().getDeclaredMethods()) {
            //Check if Command Annotation is present
            if (method.isAnnotationPresent(Command.class)) {
                Annotation[] annotations = method.getAnnotations();

                //Get the Command Annotation
                for(Annotation annotation : annotations) {
                    if(annotation instanceof Command) {
                        Command aCmd = (Command)annotation;

                        //Save the command
                        CommandStruct command = new CommandStruct();
                        command.setAnnotation(aCmd);
                        command.setCommand(method);
                        command.setInstance(cliCommand);

                        commandMap.put(aCmd.command(), command);

                        break;
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender commandSender, String command, String[] args) {
        for (int argsIncluded = args.length; argsIncluded >= -1; argsIncluded--) {
            StringBuilder identifierBuilder = new StringBuilder(command);
            for(int i = 0; i < argsIncluded; i++) {
                identifierBuilder.append(' ').append(args[i]);
            }

            String identifier = identifierBuilder.toString();

            if (commandMap.containsKey(identifier)) {
                String[] realArgs = Arrays.copyOfRange(args, argsIncluded, args.length);
                CommandStruct command1 = commandMap.get(identifier);

                if(realArgs.length < command1.getAnnotation().arguments()) {
                    onNotEnoughArguments(commandSender, command1);

                    return true;
                }

                try {
                    command1.getCommand().invoke(command1.getInstance(), commandSender, realArgs);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                return true;
            }
        }

        if(commandMap.containsKey(command)) {
            CommandStruct commandStruct = commandMap.get(command);

            try {
                commandStruct.getCommand().invoke(commandStruct.getInstance(), commandSender, args);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        onUnknownCommand(commandSender, command);

        return true;
    }

    /**
     * On not enough arguments.
     *
     * @param sender the sender
     * @param command the command
     */
    public void onNotEnoughArguments(CommandSender sender, CommandStruct command) {

    }

    /**
     * On unknown command.
     *
     * @param sender the sender
     * @param command the command
     */
    public void onUnknownCommand(CommandSender sender, String command) {

    }
}
