package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 17:07
 */
public class Twitter extends Config {
    public Twitter(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "twitter.yml");
        CONFIG_HEADER = new String[]{"Config for Twitter"};
    }

    public Boolean Enabled = false;
    public String ConsumerKey = "";
    public String ConsumerSecret = "";
    public String AccessToken = "";
    public String AccessTokenSecret = "";
    public ArrayList<String> AccountToMonitor = new ArrayList<>();
    public String IngameName = "&8[&2Twitter&8]&r";
    public String Message = "&8[&2%channel_short&8]&r %nick: %tweet";
    public ArrayList<String> PostToChannels = new ArrayList<>();
}
