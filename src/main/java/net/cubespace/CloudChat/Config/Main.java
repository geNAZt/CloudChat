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
    @Comment("Should the Chat announce Player joins ?")
    public boolean Announce_PlayerJoin = false;
    @Comment("Which format should be used for the Join Message ?")
    public String Announce_PlayerJoinMessage = "&8[&2%channel_short&8]&r %prefix%nick%suffix&r joined the Server";
    @Comment("Should the Chat announce Player quits ?")
    public boolean Announce_PlayerQuit = false;
    @Comment("Which format should be used for the Quit Message ?")
    public String Announce_PlayerQuitMessage = "&8[&2%channel_short&8]&r %prefix%nick%suffix&r left the Server";
    @Comment("Which format should be used if a Player goes AFK ?")
    public String Announce_PlayerGotAfk = "&8[&2%channel_short&8]&r %prefix%nick%suffix&r is afk";
    @Comment("Which format should be used if a Player comes out of AFK ?")
    public String Announce_PlayerGotOutOfAfk = "&8[&2%channel_short&8]&r %prefix%nick%suffix&r no longer is afk";
    @Comment("Disabled Servers (they handle Chat on their own)")
    public ArrayList<String> DontHandleForServers = new ArrayList<>();
}
