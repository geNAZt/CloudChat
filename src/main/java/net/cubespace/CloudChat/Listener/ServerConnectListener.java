package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 23:38
 */
public class ServerConnectListener implements Listener {
    private final CloudChatPlugin plugin;

    public ServerConnectListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnect(final ServerConnectEvent event) {
        plugin.getAsyncEventBus().callEvent(new net.cubespace.CloudChat.Event.ServerConnectEvent(event.getPlayer(), event.getTarget()));
    }
}
