package net.cubespace.CloudChat.Event;

import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 10:13
 */
public class CloudChatFormattedChatEvent extends Event {
    private String message;
    private ChannelDatabase channel;
    private ProxiedPlayer player;

    public CloudChatFormattedChatEvent(String message, ChannelDatabase channel, ProxiedPlayer player) {
        this.message = message;
        this.channel = channel;
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public ChannelDatabase getChannel() {
        return channel;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }
}
