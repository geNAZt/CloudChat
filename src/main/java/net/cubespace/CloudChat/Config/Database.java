package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 01.12.13 17:52
 */
public class Database extends Config {
    public Database(CloudChatPlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database.yml");
        CONFIG_HEADER = "Config for Database Logging";
    }

    @Comment("Is logging enabled ?")
    public Boolean Enabled = false;
    @Comment("Under which DataStorage should be saved ?")
    public String Url = "jdbc:h2:{DIR}RegionShop.db.h2";
    @Comment("User for DataStorage")
    public String Username = "walrus";
    @Comment("Password for the User")
    public String Password = "bukkit";
}
