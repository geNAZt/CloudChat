package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.HashMap;

public class UUIDMappings extends Config {
    public UUIDMappings(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "uuidMappings.yml");
        CONFIG_HEADER = new String[]{"Never change this File !!!"};
    }

    public HashMap<String, String> NameToUUID = new HashMap<>();
    public HashMap<String, String> UUIDToName = new HashMap<>();
}
