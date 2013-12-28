package net.cubespace.lib.Configuration;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.craftminecraft.bungee.bungeeyaml.bukkitapi.file.YamlConfiguration;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.ConfigObject;
import net.cubespace.lib.CubespacePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 14:07
 */
public class Config extends ConfigObject {
    protected transient File CONFIG_FILE = null;
    protected transient String CONFIG_HEADER = null;
    protected transient CubespacePlugin plugin;

    public Config() {
        CONFIG_HEADER = null;
    }

    public Config reload() throws InvalidConfigurationException {
        if (CONFIG_FILE == null) {
            throw new InvalidConfigurationException(new NullPointerException());
        }

        if (!CONFIG_FILE.exists()) {
            throw new InvalidConfigurationException(new IOException("File doesn't exist"));
        }

        plugin.getPluginLogger().debug("Reloading Config file " + CONFIG_FILE.getAbsolutePath() + " for " + getClass().getName());

        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(CONFIG_FILE);

        try {
            onLoad(yamlConfig);
            yamlConfig.save(CONFIG_FILE);
        } catch (Exception ex) {
            throw new InvalidConfigurationException(ex);
        }

        return this;
    }

    public Config save() throws InvalidConfigurationException {
        if (CONFIG_FILE == null) {
            throw new InvalidConfigurationException(new NullPointerException());
        }

        if (!CONFIG_FILE.exists()) {
            plugin.getPluginLogger().debug("Trying to create a new Config File " + CONFIG_FILE.getAbsolutePath() + " for " + getClass().getName());
            try {
                if (CONFIG_FILE.getParentFile() != null) {
                    if(!CONFIG_FILE.getParentFile().mkdirs()) {
                        plugin.getPluginLogger().debug("Could not create parent Directories");
                    }
                }

                if(!CONFIG_FILE.createNewFile()) {
                    plugin.getPluginLogger().debug("Could not create new File");
                }

                if (CONFIG_HEADER != null) {
                    Writer newConfig = new BufferedWriter(new FileWriter(CONFIG_FILE));
                    boolean firstLine = true;

                    for (String line : CONFIG_HEADER.split("\n")) {
                        if (!firstLine)
                            newConfig.write("\n");
                        else
                            firstLine = false;

                        newConfig.write("# " + line);
                    }

                    newConfig.close();
                }

            } catch (Exception ex) {
                throw new InvalidConfigurationException(ex);
            }
        }

        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(CONFIG_FILE);

        try {
            onSave(yamlConfig);
            yamlConfig.save(CONFIG_FILE);
        } catch (Exception ex) {
            throw new InvalidConfigurationException(ex);
        }

        return this;
    }

    public Config init(CubespacePlugin plugin) throws InvalidConfigurationException {
        if (CONFIG_FILE == null) {
            throw new InvalidConfigurationException(new NullPointerException());
        }

        this.plugin = plugin;

        if (CONFIG_FILE.exists()) {
            return reload();
        } else {
            return save();
        }
    }
}
