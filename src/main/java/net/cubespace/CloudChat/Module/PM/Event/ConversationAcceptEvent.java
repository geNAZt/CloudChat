package net.cubespace.CloudChat.Module.PM.Event;

import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ConversationAcceptEvent implements Event {
    private final String from;
    private String to;

    public ConversationAcceptEvent(String from, String to) {
        this.from = from;
        this.to = to;
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
}
