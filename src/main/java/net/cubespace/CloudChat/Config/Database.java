package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

public class Database extends Config {
    public Database(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database.yml");
        CONFIG_HEADER = "Config for Database Logging";
    }

    @Comment("Is logging enabled ?")
    public Boolean Enabled = false;
    @Comment("Under which DataStorage should be saved ?")
    public String Url = "jdbc:sqlite:{DIR}RegionShop.db";
    @Comment("User for DataStorage")
    public String Username = "walrus";
    @Comment("Password for the User")
    public String Password = "bukkit";
}
