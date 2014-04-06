package net.cubespace.CloudChat.Module.PlayerManager.Database;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.Mail.Database.Mail;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerDatabase extends Config {
    public PlayerDatabase(CubespacePlugin plugin, String storageKey, String userName) {
        if (storageKey.length() > 4) {
            String folder = storageKey.substring(0, 2);

            CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + folder + File.separator + storageKey + ".yml");
        } else {
            CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + storageKey + ".yml");
        }

        CONFIG_HEADER = new String[]{"User Database for " + userName};

        Nick = userName;
        Realname = userName;
        Focus = ((Main) plugin.getConfigManager().getConfig("main")).Global;
    }

    public ArrayList<String> JoinedChannels = new ArrayList<>();
    public String Focus;
    public String Nick = "";
    public String Prefix = "";
    public String Suffix = "";
    public String Town = "";
    public String Nation = "";
    public String Faction = "";
    public String Reply = "";
    public String World = "";
    public String WorldAlias = "";
    public String Realname = "";
    public String Group = "";
    public Boolean AFK = false;
    public Boolean IgnorePM = false;
    public String Server = "";
    public Boolean Ignore = false;
    public Boolean Output = true;
    public Boolean Muted = false;
    public Integer MutedFor = 0;
    public Boolean Spy = false;
    public Boolean ChatSpy = false;
    public String Conversation_Request = "";
    public String Conversation_Current = "";
    public Boolean Highlight = false;
    public final String HighlightColor = "&4&n";
    public String HighlightSound = "GHAST_SHOOT";
    public Boolean HighlightSoundEnabled = false;
    public final ArrayList<String> HighlightPattern = new ArrayList<>();
    public final HashMap<String, String> CustomFormats = new HashMap<>();
    public ArrayList<Mail> Mails = new ArrayList<>();

    public ArrayList<Pattern> PatternCache = new ArrayList<>();
}
