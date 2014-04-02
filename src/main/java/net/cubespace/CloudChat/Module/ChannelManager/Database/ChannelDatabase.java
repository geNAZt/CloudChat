package net.cubespace.CloudChat.Module.ChannelManager.Database;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChannelDatabase extends Config {
    public ChannelDatabase(CubespacePlugin plugin, String channel) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "channels" + File.separator + channel + ".yml");
        CONFIG_HEADER = new String[]{"Channel Database for " + channel};

        SpyFormat = "&8[&4SPY&8] " + Format;
    }

    public ChannelDatabase(File channelFile) {
        CONFIG_FILE = channelFile;
    }

    public String Name;
    public String Short;
    public String Format;
    public String SpyFormat;
    public Boolean Forced;
    public Boolean ForceIntoWhenPermission = false;
    public String Password = "";
    public Boolean IsLocal = false;
    public Integer LocalRange = 100;
    public Boolean FocusOnJoin = false;
    public ArrayList<String> CanInvite = new ArrayList<>();
    public HashMap<String, String> Formats = new HashMap<>();
}
