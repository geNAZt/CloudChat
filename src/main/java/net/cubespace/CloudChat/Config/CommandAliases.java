package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CommandAliases extends Config {
    public CommandAliases(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "commandAliases.yml");
        CONFIG_HEADER = new String[]{"Config for Command Aliases"};

        Msg.add("m");
        Msg.add("t");
        Msg.add("tell");

        Reply.add("r");

        Mute.add("ignore");

        Unmute.add("unignore");

        BaseCommands.put("msg", "msg");
        BaseCommands.put("reply", "reply");
        BaseCommands.put("nick", "nick");
        BaseCommands.put("realname", "realname");
        BaseCommands.put("mute", "mute");
        BaseCommands.put("unmute", "unmute");
        BaseCommands.put("cc:mute", "cc:mute");
        BaseCommands.put("cc:unmute", "cc:unmute");
        BaseCommands.put("mail", "mail");
        BaseCommands.put("irc:reconnect", "irc:reconnect");
        BaseCommands.put("cc:reload", "cc:reload");
        BaseCommands.put("cc:report", "cc:report");
        BaseCommands.put("broadcast", "broadcast");
        BaseCommands.put("join", "join");
        BaseCommands.put("leave", "leave");
        BaseCommands.put("createchannel", "createchannel");
        BaseCommands.put("invite", "invite");
        BaseCommands.put("focus", "focus");
        BaseCommands.put("list", "list");
        BaseCommands.put("socialspy", "socialspy");
        BaseCommands.put("clearchat", "clearchat");
        BaseCommands.put("channels", "channels");
        BaseCommands.put("togglepm", "togglepm");
    }

    public HashMap<String, String> BaseCommands = new HashMap<>();

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
    public ArrayList<String> Socialspy = new ArrayList<>();
    public ArrayList<String> Clearchat = new ArrayList<>();
    public ArrayList<String> Channels = new ArrayList<>();
    public ArrayList<String> TogglePM = new ArrayList<>();
    public ArrayList<String> IRCMute = new ArrayList<>();
    public ArrayList<String> IRCUnmute = new ArrayList<>();
    public ArrayList<String> ChatSpy = new ArrayList<>();
}
