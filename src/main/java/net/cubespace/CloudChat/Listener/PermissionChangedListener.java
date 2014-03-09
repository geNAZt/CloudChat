package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.Util.Permissions;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.Permission.Event.PermissionChangedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PermissionChangedListener {
    private CubespacePlugin plugin;

    public PermissionChangedListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermissionChanged(PermissionChangedEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());

        if(player == null) return;

        // Check for Permission containers
        Permissions.attachPermissionContainers(player);
    }
}
