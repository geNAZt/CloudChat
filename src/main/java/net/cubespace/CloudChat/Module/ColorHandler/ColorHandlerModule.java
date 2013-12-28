package net.cubespace.CloudChat.Module.ColorHandler;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ColorHandler.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.ColorHandler.Listener.PlayerNickchangeListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:47
 */
public class ColorHandlerModule {
    public ColorHandlerModule(CloudChatPlugin plugin) {
        plugin.getAsyncEventBus().addListener(new ChatMessageListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerNickchangeListener(plugin));
    }
}
