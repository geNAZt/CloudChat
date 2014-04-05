package net.cubespace.CloudChat.Module.ChatHandler.Event;

import net.cubespace.CloudChat.Module.ChatHandler.Sender.ISender;
import net.cubespace.lib.EventBus.Event;

import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageEvent implements Event {
    private final ISender sender;
    private String message;
    private List<String> receiptens;
    private String rawMessage;

    public ChatMessageEvent(ISender sender, String message, List<String> receiptens) {
        this.sender = sender;
        this.message = message;
        this.receiptens = receiptens;
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

    public List<String> getReceiptens() {
        return receiptens;
    }

    public void setReceiptens(List<String> receiptens) {
        this.receiptens = receiptens;
    }
}

