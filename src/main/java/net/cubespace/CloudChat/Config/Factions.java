package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

public class Factions extends Config {
    public Factions(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "factions.yml");
    }

    @Comment("Faction Servers. Which Servers in your network are some ? :D")
    public ArrayList<String> FactionServers = new ArrayList<>();
    @Comment("Faction Channel")
    public String FactionChannel = "faction";
    @Comment("Ally Channel")
    public String AllyChannel = "factionally";
    @Comment("Ally and Truce Channel")
    public String AllyAndTruceChannel = "factionallyandtruce";
    @Comment("Truce Channel")
    public String TruceChannel = "factiontruce";
    @Comment("Enemy Channel")
    public String EnemyChannel = "factionenemy";
}
