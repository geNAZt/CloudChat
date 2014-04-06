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

    @SuppressWarnings("CanBeFinal")
    public Boolean Enabled = false;
    @SuppressWarnings("CanBeFinal")
    public String JoinMessage = "I AM B-B-BACK";
    @SuppressWarnings("CanBeFinal")
    public String LeaveMessage = "Don't cry, i come back !";
    @SuppressWarnings("CanBeFinal")
    public String Name = "CloudChatIRCBot";
    @SuppressWarnings("CanBeFinal")
    public String Host = "irc.esper.net";
    @SuppressWarnings("CanBeFinal")
    public String Identify = "";
    public HashMap<String, String> Channels = null;
    @SuppressWarnings("CanBeFinal")
    public String IngameName = "&8[&2IRC&8]&r ";
    @SuppressWarnings("CanBeFinal")
    public boolean Relay_Action = false;
    @SuppressWarnings("CanBeFinal")
    public String Relay_ActionPrefix = "&8[&2%channel_short&8] [&2IRC&8]&r&5 * ";
    @SuppressWarnings("CanBeFinal")
    public boolean Relay_Join = false;
    @SuppressWarnings("CanBeFinal")
    public String Relay_JoinMessage = "&8[&2%channel_short&8] %nick&2 joined IRC";
    @SuppressWarnings("CanBeFinal")
    public boolean Relay_Nickchange = false;
    @SuppressWarnings("CanBeFinal")
    public String Relay_NickchangeMessage = "&8[&2%channel_short&8] %old_nick changed nick to %new_nick";
    @SuppressWarnings("CanBeFinal")
    public boolean Relay_Quit = false;
    @SuppressWarnings("CanBeFinal")
    public String Relay_QuitMessage = "&8[&2%channel_short&8] %nick&2 quit IRC";
    @SuppressWarnings("CanBeFinal")
    public String Relay_Message = "&8[&2%channel_short&8]&r %nick: %message";
    @SuppressWarnings("CanBeFinal")
    public String Command_Prefix = ".";
}
