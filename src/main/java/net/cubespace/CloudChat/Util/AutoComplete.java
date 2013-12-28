package net.cubespace.CloudChat.Util;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:25
 */
public class AutoComplete {
    public static String completeUsername(String user) {
        for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
            if(player.getName().startsWith(user)) {
                return player.getName();
            }
        }

        return user;
    }
}
