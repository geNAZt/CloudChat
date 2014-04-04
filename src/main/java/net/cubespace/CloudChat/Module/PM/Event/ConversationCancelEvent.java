package net.cubespace.CloudChat.Module.PM.Event;

import net.cubespace.CloudChat.Module.PM.CancelReason;
import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ConversationCancelEvent implements Event {
    private final String from;
    private String to;
    private final CancelReason cancelReason;

    public ConversationCancelEvent(String from, String to, CancelReason cancelReason) {
        this.from = from;
        this.to = to;
        this.cancelReason = cancelReason;
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

    public CancelReason getCancelReason() {
        return cancelReason;
    }
}
