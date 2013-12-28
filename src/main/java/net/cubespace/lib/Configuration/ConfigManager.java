package net.cubespace.lib.Configuration;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.lib.CubespacePlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 13:43
 */
public class ConfigManager {
    private HashMap<String, Config> configHashMap = new HashMap<>();
    private CubespacePlugin plugin;

    /**
     * Construct a new ConfigManager for the Plugin
     *
     * @param plugin
     */
    public ConfigManager(CubespacePlugin plugin) {
        this.plugin = plugin;

        plugin.getPluginLogger().debug("Loaded the ConfigManager");
    }

    /**
     * Init a new Config and store it inside the Manager
     *
     * @param name The name which should be used to store the Config to
     * @param config The config which should be inited and stored
     */
    public void initConfig(String name, Config config) {
        plugin.getPluginLogger().debug("Trying to load new Config '" + name + "'");

        try {
            config.init(this.plugin);
            configHashMap.put(name, config);
        } catch (InvalidConfigurationException e) {
            plugin.getPluginLogger().error("Could not init Config", e);
            throw new RuntimeException(e);
        }

        plugin.getPluginLogger().debug("Current ConfigMap size " + configHashMap.size());
    }

    /**
     * Gets the Config stored under the name
     *
     * @param name
     * @return
     */
    public <T> T getConfig(String name) {
        plugin.getPluginLogger().debug("Getting Config for " + name +": " + configHashMap.get(name).toString());

        return (T) configHashMap.get(name);
    }

    /**
     * Reload all loaded Configs
     */
    public void reloadAll() {
        plugin.getPluginLogger().info("Reloading all Configs");

        for(Map.Entry<String, Config> loadedConfig : configHashMap.entrySet()) {
            plugin.getPluginLogger().debug("Trying to reload " + loadedConfig.getKey() + " for " + loadedConfig.getValue().getClass().getName());
            try {
                loadedConfig.getValue().reload();
            } catch (InvalidConfigurationException e) {
                plugin.getPluginLogger().error("Could not reload Config", e);
                throw new RuntimeException(e);
            }
        }
    }
}
