package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fabian on 29.11.13.
 */
public class IRC extends Config {
    public IRC(CloudChatPlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "irc.yml");
        CONFIG_HEADER = "Config for IRC";
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
    public ArrayList<String> Channels = new ArrayList<String>();
    @Comment("Which Channels should be relayed into IRC ?")
    public String RelayChannels = "global";
    @Comment("Which Ingame Nickname should the IRC Bot have ?")
    public String IngameName = "IRC";
    @Comment("Which Format should the bot use to print messages into MC ?")
    public String Format = "&8[&2%name8] &6%nick&7: %message";
}
