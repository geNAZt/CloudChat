package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.HashMap;

public class IRC extends Config {
    public IRC(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "irc.yml");
        CONFIG_HEADER = new String[]{"Config for IRC"};

        Channels = new HashMap<>();
        Channels.put("global", "#dhjksa");
    }

    public Boolean Enabled = false;
    public String JoinMessage = "I AM B-B-BACK";
    public String LeaveMessage = "Don't cry, i come back !";
    public String Name = "CloudChatIRCBot";
    public String Host = "irc.esper.net";
    public String Identify = "";
    public HashMap<String, String> Channels = null;
    public String IngameName = "&8[&2IRC&8]&r ";
    public boolean Relay_Action = false;
    public String Relay_ActionPrefix = "&8[&2%channel_short&8] [&2IRC&8]&r&5 * ";
    public boolean Relay_Join = false;
    public String Relay_JoinMessage = "&8[&2%channel_short&8] %nick&2 joined IRC";
    public boolean Relay_Nickchange = false;
    public String Relay_NickchangeMessage = "&8[&2%channel_short&8] %old_nick changed nick to %new_nick";
    public boolean Relay_Quit = false;
    public String Relay_QuitMessage = "&8[&2%channel_short&8] %nick&2 quit IRC";
    public String Relay_Message = "&8[&2%channel_short&8]&r %nick: %message";
    public String Command_Prefix = ".";
}
