package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 18:49
 */
public class Main extends Config {
    public Main(CloudChatPlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
        CONFIG_HEADER = "CloudChat by geNAZt";
    }

    @Comment("The maximum of Channels a Player can join")
    public int MaxChannelsPerChatter = 3;
    @Comment("At which commands should CloudChat NOT bind ?")
    public ArrayList<String> DoNotBind = new ArrayList<>();
}
