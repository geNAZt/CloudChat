package net.cubespace.CloudChat.Module.Logging.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.Logging.Entity.ChatMessage;
import net.cubespace.CloudChat.Module.Logging.LoggingModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.Date;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener {
    private final LoggingModule loggingModule;

    public ChatMessageListener(LoggingModule module) {
        this.loggingModule = module;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setDate(new Date());
        chatMessage.setMessage(event.getMessage());
        chatMessage.setChannel(event.getSender().getChannel().Name);
        chatMessage.setNick(event.getSender().getPlayerDatabase().Nick);
        chatMessage.setSource(ChatMessage.SourceType.PLAYER);

        if(event.getSender().getNick().equals("IRC")) {
            chatMessage.setSource(ChatMessage.SourceType.IRC);
        }

        if(event.getSender().getNick().equals("Twitter")) {
            chatMessage.setSource(ChatMessage.SourceType.TWITTER);
        }

        loggingModule.getAsyncDatabaseLogger().addChatMessage(chatMessage);
    }
}
