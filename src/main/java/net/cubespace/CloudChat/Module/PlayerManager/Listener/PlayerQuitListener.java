package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerQuitListener implements Listener {
    private final PlayerManager playerManager;

    public PlayerQuitListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Unload Player
        playerManager.remove(event.getPlayer());
    }
}
