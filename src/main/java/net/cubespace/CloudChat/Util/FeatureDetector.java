package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class FeatureDetector {
    private static boolean useUUID;
    private static boolean useTabCompleteListener;

    public static void init(CubespacePlugin plugin) {
        try {
            ProxiedPlayer.class.getMethod("getUUID");
            useUUID = !((Main) plugin.getConfigManager().getConfig("main")).OverwriteUUIDs;
        } catch (NoSuchMethodException e) {
            useUUID = false;
        }

        try {
            Class.forName("net.md_5.bungee.api.event.TabCompleteEvent");
            useTabCompleteListener = !((Main) plugin.getConfigManager().getConfig("main")).OverwriteTabComplete;
        } catch (ClassNotFoundException e) {
            useTabCompleteListener = false;
        }
    }

    /**
     * Either or not the Player Object has an UUID (UUIDs only work in 1.7+)
     * @return false when not, true when it has
     */
    public static boolean canUseUUID() {
        return useUUID;
    }

    /**
     * Either or not this BungeeCord version has support for the TabComplete Event
     * @return false => no support, true => support
     */
    public static boolean canUseTabCompleteListener() {
        return useTabCompleteListener;
    }
}
