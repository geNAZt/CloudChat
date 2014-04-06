package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Twitter extends Config {
    public Twitter(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "twitter.yml");
        CONFIG_HEADER = new String[]{"Config for Twitter"};
    }

    @SuppressWarnings("CanBeFinal")
    public Boolean Enabled = false;
    @SuppressWarnings("CanBeFinal")
    public String ConsumerKey = "";
    @SuppressWarnings("CanBeFinal")
    public String ConsumerSecret = "";
    @SuppressWarnings("CanBeFinal")
    public String AccessToken = "";
    @SuppressWarnings("CanBeFinal")
    public String AccessTokenSecret = "";
    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> AccountToMonitor = new ArrayList<>();
    @SuppressWarnings("CanBeFinal")
    public String IngameName = "&8[&2Twitter&8]&r";
    @SuppressWarnings("CanBeFinal")
    public String Message = "&8[&2%channel_short&8]&r %nick: %tweet";
    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> PostToChannels = new ArrayList<>();
}
