package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Event.ServerConnectEvent;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ServerConnectedListener implements Listener {
    private final CubespacePlugin plugin;

    public ServerConnectedListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnected(final ServerConnectedEvent event) {
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if (event.getPlayer() == null) {
                    plugin.getPluginLogger().debug("Player race condition. Maybe the Player left.");
                    return;
                }

                if (event.getServer() == null) {
                    plugin.getPluginLogger().error("Got a ServerConnected Event without a Server (getPlayer().getServer() is null)");
                    return;
                }

                if (event.getServer().getInfo() == null) {
                    plugin.getPluginLogger().error("No Server informations loaded");
                    return;
                }

                plugin.getAsyncEventBus().callEvent(new ServerConnectEvent(event.getPlayer(), event.getServer().getInfo()));
            }
        }, 1 + (((Main) plugin.getConfigManager().getConfig("main")).DelayFor * 1000), TimeUnit.MILLISECONDS);
    }
}
