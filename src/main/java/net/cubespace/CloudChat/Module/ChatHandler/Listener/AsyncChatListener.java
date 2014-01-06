package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AsyncChatListener {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;
    private PlayerManager playerManager;

    public AsyncChatListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsynChat(AsyncChatEvent event) {
        //Format the Message
        ChannelDatabase channelDatabase = channelManager.get(playerManager.get(event.getSender().getName()).Focus);
        String message = channelDatabase.Format.replace("%message", event.getMessage());
        Sender sender = new Sender(event.getSender().getName(), channelDatabase, playerManager.get(event.getSender().getName()));

        //Secure the message against click events
        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(message);

        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, legacyMessageBuilder.getString()));
    }
}
