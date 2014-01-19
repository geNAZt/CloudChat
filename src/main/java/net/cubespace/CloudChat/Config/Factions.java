package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

public class Factions extends Config {
    public Factions(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "factions.yml");
    }

    public String FactionChannel = "faction";
    public String AllyChannel = "factionally";
    public String AllyAndTruceChannel = "factionallyandtruce";
    public String TruceChannel = "factiontruce";
    public String EnemyChannel = "factionenemy";
}
