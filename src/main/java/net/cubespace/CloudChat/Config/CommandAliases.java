package net.cubespace.CloudChat.Config;

import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CommandAliases extends Config {
    public CommandAliases(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "commandAliases.yml");
        CONFIG_HEADER = "Config for Command Aliases";

        Msg.add("m");
        Msg.add("t");
        Msg.add("tell");

        Reply.add("r");

        Mute.add("ignore");

        Unmute.add("unignore");
    }

    public ArrayList<String> Msg = new ArrayList<String>();
    public ArrayList<String> Reply = new ArrayList<String>();
    public ArrayList<String> Nick = new ArrayList<String>();
    public ArrayList<String> Realname = new ArrayList<String>();
    public ArrayList<String> Mute = new ArrayList<String>();
    public ArrayList<String> Unmute = new ArrayList<String>();
    public ArrayList<String> CCMute = new ArrayList<String>();
    public ArrayList<String> CCUnmute = new ArrayList<String>();
    public ArrayList<String> Mail = new ArrayList<String>();
    public ArrayList<String> IRCReconnect = new ArrayList<String>();
    public ArrayList<String> CCReload = new ArrayList<String>();
    public ArrayList<String> CCReport = new ArrayList<String>();
    public ArrayList<String> Broadcast = new ArrayList<String>();
    public ArrayList<String> Join = new ArrayList<String>();
    public ArrayList<String> Leave = new ArrayList<String>();
    public ArrayList<String> Createchannel = new ArrayList<String>();
    public ArrayList<String> Invite = new ArrayList<String>();
    public ArrayList<String> Focus = new ArrayList<String>();
    public ArrayList<String> List = new ArrayList<String>();
}
