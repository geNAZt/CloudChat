package net.cubespace.lib.Command;

import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.CubespacePlugin;
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
    private CubespacePlugin plugin;

    public CommandExecutor(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a new CLICommand into the executor
     *
     * @param cliCommand the cli command
     */
    public void add(CLICommand cliCommand) {
        plugin.getPluginLogger().info("Registered new Command " + cliCommand.toString());

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
                        plugin.getPluginLogger().debug("Added command " + aCmd.command() + " for the method " + method.getName());

                        break;
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender commandSender, String command, String[] args) {
        plugin.getPluginLogger().info(commandSender.getName() + " emitted command: " + command + " with args " + StringUtils.join(args, " "));

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
                    plugin.getPluginLogger().debug("Command has not enough Arguments to be handled");
                    onNotEnoughArguments(commandSender, command1);

                    return true;
                }

                try {
                    plugin.getPluginLogger().debug("Invoking command with arguments " + StringUtils.join(realArgs, " "));
                    command1.getCommand().invoke(command1.getInstance(), commandSender, realArgs);
                } catch (Exception e) {
                    plugin.getPluginLogger().error("Exception thrown while executing a Command", e);
                }

                return true;
            }
        }

        if(commandMap.containsKey(command)) {
            CommandStruct commandStruct = commandMap.get(command);

            try {
                plugin.getPluginLogger().debug("Invoking command with arguments " + StringUtils.join(args, " "));
                commandStruct.getCommand().invoke(commandStruct.getInstance(), commandSender, args);
                return true;
            } catch (Exception e) {
                plugin.getPluginLogger().error("Exception thrown while executing a Command", e);
            }
        }

        plugin.getPluginLogger().debug("Executed a unknown Command");
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
        sender.sendMessage("You have not given enough Arguments. You need at least " + command.getAnnotation().arguments() + " arguments");
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
