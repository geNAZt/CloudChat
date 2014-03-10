package net.cubespace.CloudChat.Module.ChannelManager.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 19:48
 */
public class ChannelDatabase extends Config {
    public ChannelDatabase(CubespacePlugin plugin, String channel) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "channels" + File.separator + channel + ".yml");
        CONFIG_HEADER = new String[]{"Channel AsyncDatabaseLogger for " + channel};
    }

    public ChannelDatabase(File channelFile) {
        CONFIG_FILE = channelFile;
    }

    public String Name;
    public String Short;
    public String Format;
    public Boolean Forced;
    public Boolean ForceIntoWhenPermission = false;
    public String Password = "";
    public Boolean IsLocal = false;
    public Boolean FocusOnJoin = false;
    public ArrayList<String> CanInvite = new ArrayList<>();
    public HashMap<String, String> Formats = new HashMap<>();
}
