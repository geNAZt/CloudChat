package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 11:24
 */
public class PlayerQuitListener implements Listener {
    private final CloudChatPlugin plugin;

    public PlayerQuitListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        //Unload the Player
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getChannelManager().remove(event.getPlayer());
                plugin.getPlayerManager().remove(event.getPlayer());
            }
        });
    }
}
