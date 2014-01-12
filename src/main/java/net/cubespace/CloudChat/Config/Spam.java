package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.CloudChat.Config.Sub.SpamEntry;
import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:34
 */
public class Spam extends Config {
    public Spam(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "spam.yml");
        CONFIG_HEADER = "Config for Spam Rules";

        SpamRules.add(new SpamEntry());
    }

    @Comment("Spam Rules")
    public ArrayList<SpamEntry> SpamRules = new ArrayList<>();
}
