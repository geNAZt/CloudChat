package net.cubespace.lib.Permission.Listener;

import net.cubespace.PluginMessages.PermissionRequest;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Permission.PermissionManager;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 23:38
 */
public class PlayerJoinListener implements Listener {
    private final CubespacePlugin plugin;
    private final PermissionManager permissionManager;

    public PlayerJoinListener(CubespacePlugin plugin, PermissionManager permissionManager) {
        this.plugin = plugin;
        this.permissionManager = permissionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnected(final ServerConnectedEvent event) {
        if(permissionManager.get(event.getPlayer().getName()) == null) {
            permissionManager.create(event.getPlayer().getName());
        }

        //Better wait some time since the player need to be in the Playable State
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPluginMessageManager("CubespaceLibrary").sendPluginMessage(event.getPlayer(), new PermissionRequest(permissionManager.getPrefix()));
            }
        }, 50, TimeUnit.MILLISECONDS);
    }
}
