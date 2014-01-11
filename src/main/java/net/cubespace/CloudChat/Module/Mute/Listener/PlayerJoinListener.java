package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:21
 */
public class PlayerJoinListener implements Listener {
    private PlayerManager playerManager;
    private MuteModule muteModule;

    public PlayerJoinListener(MuteModule muteModule, CloudChatPlugin plugin) {
        this.muteModule = muteModule;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(playerManager.get(event.getPlayer().getName()).Muted) {
            muteModule.getMuteManager().addGlobalMute(event.getPlayer().getName());
        }
    }
}
