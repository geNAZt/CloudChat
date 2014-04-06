package net.cubespace.CloudChat.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerConnectEvent implements Event {
    private final ProxiedPlayer player;
    private final ServerInfo serverInfo;

    public ServerConnectEvent(ProxiedPlayer player, ServerInfo serverInfo) {
        this.player = player;
        this.serverInfo = serverInfo;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}
