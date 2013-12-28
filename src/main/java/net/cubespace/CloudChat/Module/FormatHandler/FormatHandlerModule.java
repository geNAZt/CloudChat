package net.cubespace.CloudChat.Module.FormatHandler;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.FormatHandler.Listener.ChatMessageListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 17:34
 */
public class FormatHandlerModule {
    public FormatHandlerModule(CloudChatPlugin plugin) {
        plugin.getAsyncEventBus().addListener(new ChatMessageListener(plugin));
    }
}
