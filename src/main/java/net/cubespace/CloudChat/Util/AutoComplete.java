package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AutoComplete {
    private static CloudChatPlugin plugin;

    public static void init(CloudChatPlugin plugin) {
        AutoComplete.plugin = plugin;
    }

    public static String completeUsername(String user) {
        for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if(player.getName().toLowerCase().startsWith(user.toLowerCase())) {
                return player.getName();
            }
        }

        return user;
    }
}
