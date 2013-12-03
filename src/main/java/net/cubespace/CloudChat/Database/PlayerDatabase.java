package net.cubespace.CloudChat.Database;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 20:26
 */
public class PlayerDatabase extends Config {
    public PlayerDatabase(CloudChatPlugin plugin, String userName) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + userName + ".yml");
        CONFIG_HEADER = "User Database for " + userName;

        Nick = userName;
    }

    public ArrayList<String> JoinedChannels = new ArrayList<String>();
    public String Focus = "global";
    public String Nick = "";
    public String Prefix = "";
    public String Suffix = "";
    public String Reply = "";
    public String World = "";
    public String WorldAlias = "";
}
