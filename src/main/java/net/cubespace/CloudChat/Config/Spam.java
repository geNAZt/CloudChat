package net.cubespace.CloudChat.Config;

import net.cubespace.CloudChat.Config.Sub.SpamEntry;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Spam extends Config {
    public Spam(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "spam.yml");
        CONFIG_HEADER = new String[]{"Config for Spam Rules"};

        SpamRules.add(new SpamEntry());
    }

    @SuppressWarnings("CanBeFinal")
    public ArrayList<SpamEntry> SpamRules = new ArrayList<>();
}
