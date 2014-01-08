package net.cubespace.lib.Permission.Event;

import net.cubespace.lib.EventBus.Event;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 07.01.14 21:17
 */
public class PermissionChangedEvent implements Event {
    private String player;

    public PermissionChangedEvent(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
