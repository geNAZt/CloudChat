package net.cubespace.CloudChat.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AsyncChatEvent implements Event {
    private final ProxiedPlayer sender;
    private String message;

    public AsyncChatEvent(ProxiedPlayer sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
