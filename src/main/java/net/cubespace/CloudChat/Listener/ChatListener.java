package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Created by Fabian on 28.11.13.
 */
public class ChatListener implements Listener {
    private CloudChatPlugin plugin;

    public ChatListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(ChatEvent event) {
        //Check if sender is a Player
        if(!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        AsyncChatEvent asyncChatEvent = (AsyncChatEvent) plugin.getAsyncEventBus().callEventSync(new AsyncChatEvent((ProxiedPlayer) event.getSender(), event.getMessage(), event.isCommand()));
        if(asyncChatEvent.isCancelParent()) {
            event.setCancelled(true);
        }
    }
}
