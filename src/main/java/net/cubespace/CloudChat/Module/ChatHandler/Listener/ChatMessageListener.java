package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 16:13
 */
public class ChatMessageListener implements Listener {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public ChatMessageListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event) {
        for(ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, event.getMessage(), event.getSender()));
        }

        plugin.getPluginLogger().info(event.getMessage());
    }
}
