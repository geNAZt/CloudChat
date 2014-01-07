package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
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
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + event.getSender().getNick());

        for(ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("playerMenu", clickEvent);
            messageBuilder.setText(event.getMessage());
            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender()));
        }

        plugin.getPluginLogger().info(event.getMessage());
    }
}
