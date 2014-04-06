package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

public class Database extends Config {
    public Database(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database.yml");
        CONFIG_HEADER = new String[]{"Config for Database Logging"};
    }

    @SuppressWarnings("CanBeFinal")
    public Boolean Enabled = false;
    @SuppressWarnings("CanBeFinal")
    public String Url = "jdbc:sqlite:{DIR}RegionShop.db";
    @SuppressWarnings("CanBeFinal")
    public String Username = "walrus";
    @SuppressWarnings("CanBeFinal")
    public String Password = "bukkit";
}
