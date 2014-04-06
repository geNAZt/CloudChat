package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

public class Factions extends Config {
    public Factions(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "factions.yml");
    }

    @SuppressWarnings("CanBeFinal")
    public String FactionChannel = "faction";
    @SuppressWarnings("CanBeFinal")
    public String AllyChannel = "factionally";
    @SuppressWarnings("CanBeFinal")
    public String AllyAndTruceChannel = "factionallyandtruce";
    @SuppressWarnings("CanBeFinal")
    public String TruceChannel = "factiontruce";
    @SuppressWarnings("CanBeFinal")
    public String EnemyChannel = "factionenemy";
}
