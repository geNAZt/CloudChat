package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class FeatureDetector {
    private static boolean useUUID;

    public static void init(CubespacePlugin plugin) {
        try {
            ProxiedPlayer.class.getMethod("getUUID");
            useUUID = !((Main) plugin.getConfigManager().getConfig("main")).OverwriteUUIDs;
        } catch (NoSuchMethodException e) {
            useUUID = false;
        }
    }

    /**
     * Either or not the Player Object has an UUID (UUIDs only work in 1.7+)
     * @return false when not, true when it has
     */
    public static boolean canUseUUID() {
        return useUUID;
    }
}
