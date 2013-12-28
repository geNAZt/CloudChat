package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:21
 */
public class PlayerJoinListener implements Listener {
    private CloudChatPlugin plugin;

    public PlayerJoinListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Load the Player
        System.out.println(plugin.getManagerRegistry().getManager("playerManager"));
        System.out.println(event.getPlayer().getName());
        ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).load(event.getPlayer().getName());
    }
}
