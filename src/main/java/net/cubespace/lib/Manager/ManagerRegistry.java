package net.cubespace.lib.Manager;

import net.cubespace.lib.CubespacePlugin;

import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:15
 */
public class ManagerRegistry {
    private HashMap<String, IManager> managerHashMap = new HashMap<>();
    private CubespacePlugin plugin;

    public ManagerRegistry(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerManager(String name, IManager manager) {
        plugin.getPluginLogger().info("New Manager has been registered " + name + ": " + manager.toString());

        managerHashMap.put(name, manager);
    }

    public <T> T getManager(String name) {
        plugin.getPluginLogger().debug("Getting Manager " + name);

        return (T) managerHashMap.get(name);
    }
}
