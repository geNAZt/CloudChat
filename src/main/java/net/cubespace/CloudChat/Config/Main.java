package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

public class Main extends Config {
    public Main(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
        CONFIG_HEADER = new String[]{"CloudChat by geNAZt"};
    }

    @SuppressWarnings("CanBeFinal")
    public int MaxChannelsPerChatter = 3;
    @SuppressWarnings("CanBeFinal")
    public String Global = "global";
    public Boolean PrivateMode = false;
    @SuppressWarnings("CanBeFinal")
    public String NicknamePrefix = "";
    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> DoNotBind = new ArrayList<>();
    @SuppressWarnings("CanBeFinal")
    public boolean Announce_PlayerJoin = false;
    @SuppressWarnings("CanBeFinal")
    public boolean Announce_PlayerQuit = false;
    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> DontHandleForServers = new ArrayList<>();
    @SuppressWarnings("CanBeFinal")
    public Integer DelayFor = 0;
    @SuppressWarnings("CanBeFinal")
    @Comment("When you set this over 0 it will ask the CloudChatBukkit to reload the Permissions every x Minutes")
    public Integer AskForNewPermissionsEvery = 0;
    @SuppressWarnings("CanBeFinal")
    @Comment("When you have a offline Server set this to true")
    public Boolean OverwriteUUIDs = false;
    @SuppressWarnings("CanBeFinal")
    @Comment("When you want that Bukkit takes over control over the Tab Complete set this to true")
    public Boolean OverwriteTabComplete = false;
    @SuppressWarnings("CanBeFinal")
    @Comment("If you don't want the /<channelshort> Focus set this to false")
    public Boolean UseChannelShortFocus = true;
    @Comment("Never alter this please")
    public Boolean FirstStart = true;
    @SuppressWarnings("CanBeFinal")
    @Comment("Timeout after which Conversation requests should timeout (in seconds)")
    public Integer ConversationTimeout = 10;
}
