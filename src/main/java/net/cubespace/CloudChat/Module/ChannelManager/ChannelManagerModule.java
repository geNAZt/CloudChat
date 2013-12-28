package net.cubespace.CloudChat.Module.ChannelManager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.ChannelBinder;
import net.cubespace.CloudChat.Command.Binder.JoinedChannelBinder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Channels;
import net.cubespace.CloudChat.Module.ChannelManager.Command.CreateChannel;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Invite;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerQuitListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 16:25
 */
public class ChannelManagerModule {
    public ChannelManagerModule(CloudChatPlugin plugin) {
        //Register the Manager
        plugin.getManagerRegistry().registerManager("channelManager", new ChannelManager(plugin));

        //Register the Listener
        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(new AsyncChatListener(plugin));

        //Register Commands
        plugin.getProxy().getPluginManager().registerCommand(plugin, new ChannelBinder(plugin, "join"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new JoinedChannelBinder(plugin, "leave"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "createchannel"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "invite"));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new JoinedChannelBinder(plugin, "focus"));

        plugin.getCommandExecutor().add(new Channels(plugin));
        plugin.getCommandExecutor().add(new CreateChannel(plugin));
        plugin.getCommandExecutor().add(new Invite(plugin));
    }
}
