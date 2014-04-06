package net.cubespace.CloudChat.Module.Mute.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener {
    private final MuteModule muteModule;

    public ChatMessageListener(MuteModule muteModule) {
        this.muteModule = muteModule;
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onChatMessage(ChatMessageEvent event) {
        return muteModule.getMuteManager().isGlobalMute(event.getSender().getNick());
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onPlayerSendMessage(PlayerSendMessageEvent event) {
        return muteModule.getMuteManager().isPlayerMute(event.getPlayer().getName(), event.getSender().getNick());
    }
}
