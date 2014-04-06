package net.cubespace.CloudChat.Module.ChannelManager.Database;

import net.cubespace.Yamler.Config.Comments;
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
    public final Boolean IsLocal = false;
    public final Integer LocalRange = 100;
    public final Boolean FocusOnJoin = false;
    public final ArrayList<String> CanInvite = new ArrayList<>();
    public final HashMap<String, String> Formats = new HashMap<>();
    @Comments({
        "This can be used to configure multiple Formats for different Permission groups",
        "Please care that only Bukkit Groups work. The format is: key => groupname, value => format to use",
        "When the Groupname of a Player is not found in here the default Format will be used"
    })
    public final HashMap<String, String> GroupFormats = new HashMap<>();
}
