package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.Event.ServerConnectEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

public class ServerConnectListener implements Listener {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;

    public ServerConnectListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onServerConnect(ServerConnectEvent event) {
        plugin.getPluginLogger().debug("Player " + event.getPlayer().getName() + " changed its Server Format to " + event.getServerInfo().getName());

        if(playerManager.get(event.getPlayer().getName()) != null) {
            playerManager.get(event.getPlayer().getName()).Server = event.getServerInfo().getName();
        }
    }
}
