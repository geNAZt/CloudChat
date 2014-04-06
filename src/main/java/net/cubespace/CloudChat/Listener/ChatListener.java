package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.CheckCommandEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {
    private final CloudChatPlugin plugin;

    public ChatListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(ChatEvent event) {
        //Check if sender is a Player
        if(!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        CheckCommandEvent commandEvent = (CheckCommandEvent) plugin.getAsyncEventBus().callEventSync(new CheckCommandEvent((ProxiedPlayer) event.getSender(), event.getMessage(), event.isCommand()));
        if(commandEvent.isCancelParent()) {
            event.setCancelled(true);
        }
    }
}
