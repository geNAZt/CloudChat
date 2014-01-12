package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

public class Main extends Config {
    public Main(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
        CONFIG_HEADER = "CloudChat by geNAZt";
    }

    @Comment("The maximum of Channels a Player can join")
    public int MaxChannelsPerChatter = 3;
    @Comment("Which channel is the global one")
    public String Global = "global";
    @Comment("Is the CloudChat in private Mode ?")
    public Boolean PrivateMode = false;
    @Comment("At which commands should CloudChat NOT bind ?")
    public ArrayList<String> DoNotBind = new ArrayList<>();
    @Comment("Should the Chat announce Player joins ?")
    public boolean Announce_PlayerJoin = false;
    @Comment("Should the Chat announce Player quits ?")
    public boolean Announce_PlayerQuit = false;
    @Comment("Disabled Servers (they handle Chat on their own)")
    public ArrayList<String> DontHandleForServers = new ArrayList<>();
    @Comment("Delay Chat (to prevent Bots from spamming) for x Amount Seconds")
    public Integer DelayFor = 0;
}
