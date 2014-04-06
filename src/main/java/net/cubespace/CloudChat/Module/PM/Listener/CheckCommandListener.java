package net.cubespace.CloudChat.Module.PM.Listener;

import net.cubespace.CloudChat.Event.CheckCommandEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationMessageEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CheckCommandListener implements Listener {
    private final PlayerManager playerManager;
    private final CubespacePlugin plugin;

    public CheckCommandListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, canVeto = true)
    public boolean onCommandCheck(CheckCommandEvent event) {
        if (event.isCommand()) return false;

        PlayerDatabase playerDatabase = playerManager.get(event.getSender().getName());

        if (!playerDatabase.Conversation_Current.equals("")) {
            ConversationMessageEvent conversationMessageEvent = new ConversationMessageEvent(event.getSender().getName(), playerDatabase.Conversation_Current, event.getMessage());
            plugin.getAsyncEventBus().callEvent(conversationMessageEvent);

            event.setCancelParent(true);

            return true;
        }

        return false;
    }
}
