package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Towny extends Config {
    public Towny(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "towny.yml");
    }

    @Comment("Town Channel")
    public String TownChannel = "towny";
    @Comment("Nation Channel")
    public String NationChannel = "townynation";
}
