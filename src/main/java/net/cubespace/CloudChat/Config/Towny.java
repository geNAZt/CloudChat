package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Towny extends Config {
    public Towny(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "towny.yml");
    }

    @SuppressWarnings("CanBeFinal")
    public String TownChannel = "towny";
    @SuppressWarnings("CanBeFinal")
    public String NationChannel = "townynation";
}
