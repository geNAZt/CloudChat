package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.Config.PermissionContainers;
import net.cubespace.CloudChat.Config.Sub.PermissionContainer;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Permission.PermissionStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Permissions {
    private static CubespacePlugin plugin;

    public static void init(CubespacePlugin plugin) {
        Permissions.plugin = plugin;
    }

    public static void attachPermissionContainers(ProxiedPlayer proxiedPlayer) {
        attachPermissionContainers(proxiedPlayer, new HashSet<String>());
    }

    private static void attachPermissionContainers(ProxiedPlayer proxiedPlayer, Set<String> alreadyAttached) {
        PermissionStorage permissionStorage = plugin.getPermissionManager().get(proxiedPlayer.getName());
        PermissionContainers permissionContainers = plugin.getConfigManager().getConfig("permissionContainers");

        for (PermissionContainer permission : permissionContainers.PermissionContainers) {
            if (!alreadyAttached.contains(permission.Access) && permissionStorage.has(permission.Access)) {
                for (String p : permission.Sub) {
                    if (!permissionStorage.has(p)) {
                        permissionStorage.add(p);
                    }
                }

                alreadyAttached.add(permission.Access);
                attachPermissionContainers(proxiedPlayer, alreadyAttached);
            }
        }
    }
}
