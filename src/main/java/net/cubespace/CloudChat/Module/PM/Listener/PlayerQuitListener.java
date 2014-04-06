package net.cubespace.CloudChat.Module.PM.Listener;

import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.PM.CancelReason;
import net.cubespace.CloudChat.Module.PM.Event.ConversationCancelEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerQuitListener implements Listener {
    private final PlayerManager playerManager;
    private final CubespacePlugin plugin;

    public PlayerQuitListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Map.Entry<String, PlayerDatabase> playerDatabaseEntry : playerManager.getLoadedPlayers().entrySet()) {
            if (playerDatabaseEntry.getValue().Conversation_Request.equals(event.getPlayer().getName())) {
                ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(playerDatabaseEntry.getKey(), event.getPlayer().getName(), CancelReason.SENDER_LEFT);
                plugin.getAsyncEventBus().callEvent(conversationCancelEvent);

                playerDatabaseEntry.getValue().Conversation_Request = "";
                playerManager.get(event.getPlayer().getName()).Conversation_Request = "";
            }

            if (playerDatabaseEntry.getValue().Conversation_Current.equals(event.getPlayer().getName())) {
                playerDatabaseEntry.getValue().Conversation_Current = "";
                playerManager.get(event.getPlayer().getName()).Conversation_Current = "";
            }
        }

        //Unload Player
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        if (!playerDatabase.Conversation_Request.equals("")) {
            ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(playerDatabase.Conversation_Request, event.getPlayer().getName(), CancelReason.TARGET_LEFT);
            plugin.getAsyncEventBus().callEvent(conversationCancelEvent);

            playerManager.get(event.getPlayer().getName()).Conversation_Request = "";
        }

        playerDatabase.Conversation_Current = "";
        playerDatabase.Conversation_Request = "";
    }
}
