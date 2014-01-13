package net.cubespace.CloudChat.Config;

import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CommandAliases extends Config {
    public CommandAliases(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "commandAliases.yml");
        CONFIG_HEADER = "Config for Command Aliases";
    }

    public String[] Msg = new String[]{"m", "t", "tell"};
    public String[] Reply = new String[]{"r"};
    public String[] Nick = new String[]{};
    public String[] Realname = new String[]{};
    public String[] Mute = new String[]{"ignore"};
    public String[] Unmute = new String[]{"unignore"};
    public String[] CCMute = new String[]{};
    public String[] CCUnmute = new String[]{};
    public String[] Mail = new String[]{};
    public String[] IRCReconnect = new String[]{};
    public String[] CCReload = new String[]{};
    public String[] CCReport = new String[]{};
    public String[] Broadcast = new String[]{};
    public String[] Join = new String[]{};
    public String[] Leave = new String[]{};
    public String[] Createchannel = new String[]{};
    public String[] Invite = new String[]{};
    public String[] Focus = new String[]{};
    public String[] List = new String[]{};
}
