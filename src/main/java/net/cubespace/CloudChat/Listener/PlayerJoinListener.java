package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 23:38
 */
public class PlayerJoinListener implements Listener {
    private final CloudChatPlugin plugin;

    public PlayerJoinListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PostLoginEvent event) {
        ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).load(event.getPlayer().getName());
    }
}
