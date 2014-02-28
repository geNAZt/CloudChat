package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:48
 */
public class PlayerQuitListener implements Listener {
    private MuteModule muteModule;
    private PlayerManager playerManager;

    public PlayerQuitListener(MuteModule muteModule, CubespacePlugin plugin) {
        this.muteModule = muteModule;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());

        playerDatabase.Muted = muteModule.getMuteManager().isGlobalMute(event.getPlayer().getName());
        playerDatabase.MutedFor = muteModule.getMuteManager().getRestTimeGlobalMute(event.getPlayer().getName());

        muteModule.getMuteManager().remove(event.getPlayer().getName());
    }
}
