package net.cubespace.CloudChat.Event;

import net.cubespace.CloudChat.IRC.IRCSender;
import net.md_5.bungee.api.plugin.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:30
 */
public class CloudChatIRCChatEvent extends Event {
    private String message;
    private IRCSender sender;
    private boolean format;

    public CloudChatIRCChatEvent(String message, IRCSender sender, boolean format) {
        this.message = message;
        this.sender = sender;
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public IRCSender getSender() {
        return sender;
    }

    public boolean isFormat() {
        return format;
    }
}
