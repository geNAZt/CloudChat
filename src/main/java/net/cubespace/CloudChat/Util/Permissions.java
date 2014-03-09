package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.Config.PermissionContainers;
import net.cubespace.CloudChat.Config.Sub.PermissionContainer;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Permission.PermissionStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Permissions {
    private static CubespacePlugin plugin;

    public static void init(CubespacePlugin plugin) {
        Permissions.plugin = plugin;
    }

    public static void attachPermissionContainers(ProxiedPlayer proxiedPlayer) {
        PermissionStorage permissionStorage = plugin.getPermissionManager().get(proxiedPlayer.getName());
        PermissionContainers permissionContainers = plugin.getConfigManager().getConfig("permissionContainers");

        for (PermissionContainer permission : permissionContainers.PermissionContainers) {
            if (permissionStorage.has(permission.Access)) {
                for (String p : permission.Sub) {
                    if (!permissionStorage.has(p)) {
                        permissionStorage.add(p);
                    }
                }

                attachPermissionContainers(proxiedPlayer);
            }
        }
    }
}
