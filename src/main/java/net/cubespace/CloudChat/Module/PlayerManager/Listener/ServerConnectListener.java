package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Event.ServerConnectEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * Created by Fabian on 30.12.13.
 */
public class ServerConnectListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;

    public ServerConnectListener(CloudChatPlugin plugin) {
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
