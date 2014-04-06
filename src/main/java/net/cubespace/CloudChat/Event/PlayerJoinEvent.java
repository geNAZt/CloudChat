package net.cubespace.CloudChat.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinEvent implements Event {
    private final ProxiedPlayer player;

    public PlayerJoinEvent(ProxiedPlayer player) {
        this.player = player;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }
}
