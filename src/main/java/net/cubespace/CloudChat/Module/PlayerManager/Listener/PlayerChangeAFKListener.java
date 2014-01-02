package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerChangeAFKEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:21
 */
public class PlayerChangeAFKListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;

    public PlayerChangeAFKListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangeAFK(PlayerChangeAFKEvent event) {
        //Load the Player
        playerManager.get(event.getPlayer().getName()).AFK = event.isAfk();
    }
}