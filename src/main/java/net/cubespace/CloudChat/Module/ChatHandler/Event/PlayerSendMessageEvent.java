package net.cubespace.CloudChat.Module.ChatHandler.Event;

import net.cubespace.CloudChat.Module.ChatHandler.Sender.ISender;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.ref.WeakReference;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 16:19
 */
public class PlayerSendMessageEvent implements Event {
    private final WeakReference<ProxiedPlayer> player;
    private final MessageBuilder message;
    private final ISender sender;

    public PlayerSendMessageEvent(ProxiedPlayer player, MessageBuilder message, ISender sender) {
        this.player = new WeakReference<>(player);
        this.message = message;
        this.sender = sender;
    }

    public ProxiedPlayer getPlayer() {
        return player.get();
    }

    public MessageBuilder getMessage() {
        return message;
    }

    public ISender getSender() {
        return sender;
    }
}
