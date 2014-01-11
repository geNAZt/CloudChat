package net.cubespace.CloudChat.Module.PlayerManager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PluginMessageListener;
import net.cubespace.CloudChat.Module.PlayerManager.Command.Nick;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerChangeAFKListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerNickchangeListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.ServerConnectListener;
import net.cubespace.PluginMessages.AFKMessage;
import net.cubespace.PluginMessages.AffixMessage;
import net.cubespace.PluginMessages.IgnoreMessage;
import net.cubespace.PluginMessages.OutputMessage;
import net.cubespace.PluginMessages.WorldMessage;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class PlayerManagerModule {
    public PlayerManagerModule(CloudChatPlugin plugin) {
        plugin.getPluginLogger().info("Starting Player Manager Module...");

        //Register the PlayerManager
        plugin.getManagerRegistry().registerManager("playerManager", new PlayerManager(plugin));

        //Register the Listener
        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerNickchangeListener(plugin));
        plugin.getAsyncEventBus().addListener(new ServerConnectListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerChangeAFKListener(plugin));
        plugin.getAsyncEventBus().addListener(new AsyncChatListener(plugin));

        //Register the Commands
        new Nick(plugin);

        //Register the Packets and the Listeners
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(AffixMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(AFKMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(WorldMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(IgnoreMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(OutputMessage.class);

        plugin.getPluginMessageManager("CloudChat").addListenerToRegister(new PluginMessageListener(plugin));
    }
}
