package net.cubespace.CloudChat.Module.PM.Event;

import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ConversationMessageEvent implements Event {
    private final String from;
    private String to;
    private String message;

    public ConversationMessageEvent(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
