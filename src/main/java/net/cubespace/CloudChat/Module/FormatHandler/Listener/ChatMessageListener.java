package net.cubespace.CloudChat.Module.FormatHandler.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 17:35
 */
public class ChatMessageListener implements Listener {
    private CubespacePlugin plugin;

    public ChatMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatMessage(ChatMessageEvent event) {
        event.setMessage(MessageFormat.format(event.getMessage(), event.getSender().getChannel(), event.getSender().getPlayerDatabase()));
        plugin.getPluginLogger().debug("Formatted message");
    }
}
