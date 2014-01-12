package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.ChatHandlerModule;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerSendMessageListener implements Listener {
    private PlayerManager playerManager;
    private ChatHandlerModule chatHandlerModule;

    public PlayerSendMessageListener(ChatHandlerModule chatHandlerModule, CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.chatHandlerModule = chatHandlerModule;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSendMessage(PlayerSendMessageEvent event) {
        if(event.getPlayer() == null) return;

        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        if(playerDatabase.Output)
            event.getMessage().send(event.getPlayer());
        else
            chatHandlerModule.getChatBuffer().addToBuffer(event.getPlayer().getName(), event.getMessage());
    }
}
