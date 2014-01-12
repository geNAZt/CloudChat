package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.lib.Configuration.Config;
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
        CONFIG_HEADER = "Config for Twitter";
    }

    @Comment("Is Twitter enabled?")
    public Boolean Enabled = false;
    @Comment("The Twitter Consumer key")
    public String ConsumerKey = "";
    @Comment("The Twitter Consumer secret")
    public String ConsumerSecret = "";
    @Comment("The Twitter Access token")
    public String AccessToken = "";
    @Comment("The Twitter Access token secret")
    public String AccessTokenSecret = "";
    @Comment("Which Tiwtter Account should be searched?")
    public ArrayList<String> AccountToMonitor = new ArrayList<>();
    @Comment("Ingame Prefix for Twitter")
    public String IngameName = "&8[&2Twitter&8]&r";
    @Comment("The message to use when a new Tweet comes")
    public String Message = "&8[&2%channel_short&8]&r %nick: %tweet";
    @Comment("Post it to this Channels")
    public ArrayList<String> PostToChannels = new ArrayList<>();
}
