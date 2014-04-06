package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMListener implements Listener {
    private final MuteModule muteModule;

    public PMListener(MuteModule muteModule) {
        this.muteModule = muteModule;
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onPM(PMEvent event) {
        return muteModule.getMuteManager().isPlayerMute(event.getTo(), event.getFrom());
    }
}
