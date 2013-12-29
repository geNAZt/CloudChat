package net.cubespace.CloudChat.Module.Spam.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.Spam.SpamModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 01:00
 */
public class ChatMessageListener implements Listener {
    private CloudChatPlugin plugin;
    private SpamModule spamModule;

    public ChatMessageListener(SpamModule spamModule, CloudChatPlugin plugin) {
        this.spamModule = spamModule;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatMessage(ChatMessageEvent event) {
        final ProxiedPlayer player = plugin.getProxy().getPlayer(event.getSender().getNick());
        if(player == null) return;

        spamModule.getSpamManager().addOneMessage(player);
    }
}
