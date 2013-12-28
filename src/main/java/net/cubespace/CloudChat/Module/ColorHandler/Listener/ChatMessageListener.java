package net.cubespace.CloudChat.Module.ColorHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 17:50
 */
public class ChatMessageListener implements Listener {
    private CloudChatPlugin plugin;

    public ChatMessageListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(ChatMessageEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getSender().getNick());

        if(player == null) return;

        if(!player.hasPermission("cloudchat.use.color")) {
            event.setMessage(FontFormat.stripColor(event.getMessage()));
        }
    }
}
