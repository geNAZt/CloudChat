package net.cubespace.CloudChat.Config;

import com.google.common.collect.ArrayListMultimap;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.lib.Configuration.Config;

import java.io.File;

public class IRC extends Config {
    public IRC(CloudChatPlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "irc.yml");
        CONFIG_HEADER = "Config for IRC";

        Channels.put("global", "#dhjksa");
    }

    @Comment("Is the IRC Bot enabled ?")
    public Boolean Enabled = false;
    @Comment("Which message should be written on Join ?")
    public String JoinMessage = "I AM B-B-BACK";
    @Comment("Which message should be written on Leave ?")
    public String LeaveMessage = "Don't cry, i come back !";
    @Comment("Under which name should the Bot join ?")
    public String Name = "CloudChatIRCBot";
    @Comment("Which Host should the bot join ?")
    public String Host = "irc.esper.net";
    @Comment("Which Channels should the Bot join ?")
    public ArrayListMultimap<String, String> Channels = ArrayListMultimap.create();
    @Comment("Which Ingame Nickname should the IRC Bot have ?")
    public String IngameName = "&8[&2IRC&8]&r";
    @Comment("Should the Actions from IRC be relayed ?")
    public boolean Relay_Action = false;
    @Comment("The prefix for Actions")
    public String Relay_ActionPrefix = "&8[&2%channel_short&8] [&2IRC&8]&r&5 * ";
    @Comment("Should the joins from IRC be relayed ?")
    public boolean Relay_Join = false;
    @Comment("The message for joins")
    public String Relay_JoinMessage = "&8[&2%channel_short&8] %nick&2 joined IRC";
    @Comment("Relay nickchanges ?")
    public boolean Relay_Nickchange = false;
    @Comment("Nickchange message")
    public String Relay_NickchangeMessage = "&8[&2%channel_short&8] %old_nick changed nick to %new_nick";
    @Comment("Relay quit ?")
    public boolean Relay_Quit = false;
    @Comment("Quit message")
    public String Relay_QuitMessage = "&8[&2%channel_short&8] %nick&2 quit IRC";
    @Comment("Which format should be used to relay IRC Messages ?")
    public String Relay_Message = "&8[&2%channel_short&8]&r %nick: %message";
    @Comment("Which format should be used in the .players IRC Command")
    public String Command_Players = "%prefix%nick%suffix&r";
}
