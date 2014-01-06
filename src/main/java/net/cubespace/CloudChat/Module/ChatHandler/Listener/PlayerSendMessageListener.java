package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerSendMessageListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSendMessage(PlayerSendMessageEvent event) {
        if(event.getPlayer() == null) return;

        event.getMessage().send(event.getPlayer());
    }
}
