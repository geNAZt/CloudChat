package net.cubespace.CloudChat.Module.PlayerManager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.Command.Nick;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerNickchangeListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerQuitListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class PlayerManagerModule {
    public PlayerManagerModule(CloudChatPlugin plugin) {
        //Register the PlayerManager
        plugin.getManagerRegistry().registerManager("playerManager", new PlayerManager(plugin));

        //Register the Listener
        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerNickchangeListener(plugin));

        //Register the Commands
        new Nick(plugin);
    }
}
