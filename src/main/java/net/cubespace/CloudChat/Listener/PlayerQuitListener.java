package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerQuitListener implements Listener {
    private final CloudChatPlugin plugin;

    public PlayerQuitListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        plugin.getAsyncEventBus().callEvent(new PlayerQuitEvent(event.getPlayer()));
    }
}
