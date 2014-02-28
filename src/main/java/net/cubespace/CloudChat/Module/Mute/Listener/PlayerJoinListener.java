package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private PlayerManager playerManager;
    private MuteModule muteModule;

    public PlayerJoinListener(MuteModule muteModule, CubespacePlugin plugin) {
        this.muteModule = muteModule;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());

        if(playerDatabase.Muted) {
            muteModule.getMuteManager().addGlobalMute(event.getPlayer().getName(), playerDatabase.MutedFor);
        }
    }
}
