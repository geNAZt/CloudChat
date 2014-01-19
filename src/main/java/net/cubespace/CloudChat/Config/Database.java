package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

public class Database extends Config {
    public Database(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database.yml");
        CONFIG_HEADER = new String[]{"Config for Database Logging"};
    }

    public Boolean Enabled = false;
    public String Url = "jdbc:sqlite:{DIR}RegionShop.db";
    public String Username = "walrus";
    public String Password = "bukkit";
}
