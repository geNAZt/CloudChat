package net.cubespace.CloudChat.Module.PlayerManager.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerChangeAFKEvent implements Event {
    private final ProxiedPlayer player;
    private final boolean afk;

    public PlayerChangeAFKEvent(ProxiedPlayer player, boolean afk) {
        this.player = player;
        this.afk = afk;
    }

    public boolean isAfk() {
        return afk;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }
}
