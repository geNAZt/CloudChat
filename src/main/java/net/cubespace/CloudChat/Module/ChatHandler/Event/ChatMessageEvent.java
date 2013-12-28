package net.cubespace.CloudChat.Module.ChatHandler.Event;

import net.cubespace.CloudChat.Module.ChatHandler.Sender.ISender;
import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:21
 */
public class ChatMessageEvent implements Event {
    private final ISender sender;
    private String message;

    public ChatMessageEvent(ISender sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public ISender getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
