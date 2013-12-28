package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:48
 */
public class PlayerQuitListener implements Listener {
    private MuteModule muteModule;

    public PlayerQuitListener(MuteModule muteModule) {
        this.muteModule = muteModule;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        muteModule.getMuteManager().remove(event.getPlayer().getName());
    }
}
