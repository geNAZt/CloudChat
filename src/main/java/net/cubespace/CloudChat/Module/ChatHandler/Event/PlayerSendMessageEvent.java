package net.cubespace.CloudChat.Module.ChatHandler.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.ref.WeakReference;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 16:19
 */
public class PlayerSendMessageEvent implements Event {
    private final WeakReference<ProxiedPlayer> player;
    private final String message;

    public PlayerSendMessageEvent(ProxiedPlayer player, String message) {
        this.player = new WeakReference<>(player);
        this.message = message;
    }

    public ProxiedPlayer getPlayer() {
        return player.get();
    }

    public String getMessage() {
        return message;
    }
}
