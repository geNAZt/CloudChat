package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.cubespace.lib.Permission.Event.PermissionLoadedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 07.01.14 21:49
 */
public class PermissionLoadedListener implements Listener {
    private CloudChatPlugin plugin;

    public PermissionLoadedListener(CloudChatPlugin plugin) {
        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermissionsLoaded(final PermissionLoadedEvent event) {
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());

                if (player == null) return;
                plugin.getAsyncEventBus().callEvent(new PlayerJoinEvent(player));
            }
        }, 1 + (((Main) plugin.getConfigManager().getConfig("main")).DelayFor * 1000), TimeUnit.MILLISECONDS);
    }
}
