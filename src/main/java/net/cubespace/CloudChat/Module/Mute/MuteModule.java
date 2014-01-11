package net.cubespace.CloudChat.Module.Mute;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Module.Mute.Command.Mute;
import net.cubespace.CloudChat.Module.Mute.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PMListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerJoinListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:29
 */
public class MuteModule {
    private MuteManager muteManager;

    public MuteModule(CloudChatPlugin plugin) {
        plugin.getPluginLogger().info("Starting Mute Module...");

        this.muteManager = new MuteManager();

        plugin.getManagerRegistry().registerManager("muteManager", muteManager);

        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "mute"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "unmute"));

        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "cc:mute"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "cc:unmute"));

        plugin.getCommandExecutor().add(new Mute(this, plugin));

        plugin.getAsyncEventBus().addListener(new ChatMessageListener(this));
        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(this, plugin));
        plugin.getAsyncEventBus().addListener(new PlayerQuitListener(this, plugin));
        plugin.getAsyncEventBus().addListener(new PMListener(this));
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }
}
