package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AsyncChatListener {
    private final PlayerManager playerManager;

    public AsyncChatListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onAsynChat(AsyncChatEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getSender().getName());
        return playerDatabase.Ignore;
    }
}
