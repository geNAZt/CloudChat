package net.cubespace.CloudChat.Module.IRC.Event;

import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 20:22
 */
public class ChatIRCMessageEvent implements Event {
    private String ircNick;
    private String channel;
    private String ircChannel;
    private String message;

    public ChatIRCMessageEvent(String ircNick, String channel, String ircChannel, String message) {
        this.ircNick = ircNick;
        this.channel = channel;
        this.ircChannel = ircChannel;
        this.message = message;
    }

    public String getIrcNick() {
        return ircNick;
    }

    public void setIrcNick(String ircNick) {
        this.ircNick = ircNick;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIrcChannel() {
        return ircChannel;
    }

    public void setIrcChannel(String ircChannel) {
        this.ircChannel = ircChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
