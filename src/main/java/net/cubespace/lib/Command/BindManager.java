package net.cubespace.lib.Command;

import com.google.common.base.Preconditions;
import net.cubespace.lib.CubespacePlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class BindManager {
    private CubespacePlugin plugin;
    private HashMap<String, net.md_5.bungee.api.plugin.Command> boundCommands = new HashMap<>();

    public BindManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Bind a new Command to the Plugin using a Binder
     *
     * @param command which should be bound to the Plugin
     * @param binder which should be used to Bind the Command
     */
    public void bind(String command, Class<? extends net.md_5.bungee.api.plugin.Command> binder, String ... aliases) {
        Preconditions.checkNotNull(command, "Command can not be null");
        Preconditions.checkNotNull(binder, "Binder can not be null");

        try {
            Constructor constructor = binder.getConstructor(CubespacePlugin.class, String.class, String[].class);
            net.md_5.bungee.api.plugin.Command bindCommand = (net.md_5.bungee.api.plugin.Command) constructor.newInstance(plugin, command, aliases);
            plugin.getProxy().getPluginManager().registerCommand(plugin, bindCommand);

            boundCommands.put(command, bindCommand);
        } catch (IllegalAccessException|InstantiationException|InvocationTargetException|NoSuchMethodException e) {
            plugin.getPluginLogger().error("Could not bind Command.", e);
        }
    }

    /**
     * Checks if a command has been bound already
     *
     * @param command from which you want to know if it is bound
     * @return true if bound / false if not
     */
    public boolean isBound(String command) {
        return boundCommands.containsKey(command);
    }

    /**
     * Get the Binder with which the Command has been bound
     *
     * @param command which should be looked up
     * @return Binder which has been bound or null
     */
    public net.md_5.bungee.api.plugin.Command getBinder(String command) {
        return boundCommands.get(command);
    }

    /**
     * Removes the Binder from BungeeCord so you can rebind the Command with a new Binder
     *
     * @param command which should be unbound
     */
    public void unbind(String command) {
        plugin.getProxy().getPluginManager().unregisterCommand(getBinder(command));
        boundCommands.remove(command);
    }
}
