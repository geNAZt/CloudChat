package net.cubespace.CloudChat.Module.ChatHandler;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerChangeAFKListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerSendMessageListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PluginMessageListener;
import net.cubespace.PluginMessages.FactionChatMessage;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:22
 */
public class ChatHandlerModule {
    public ChatHandlerModule(CloudChatPlugin plugin) {
        Main config = plugin.getConfigManager().getConfig("main");

        //Register the Listener
        if(config.Announce_PlayerJoin)
            plugin.getAsyncEventBus().addListener(new PlayerJoinListener(plugin));

        if(config.Announce_PlayerQuit)
            plugin.getAsyncEventBus().addListener(new PlayerQuitListener(plugin));

        plugin.getAsyncEventBus().addListener(new PlayerChangeAFKListener(plugin));
        plugin.getAsyncEventBus().addListener(new AsyncChatListener(plugin));
        plugin.getAsyncEventBus().addListener(new ChatMessageListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerSendMessageListener());

        //Register Packets and Listener
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(FactionChatMessage.class);
        plugin.getPluginMessageManager("CloudChat").addListenerToRegister(new PluginMessageListener(plugin));
    }
}
