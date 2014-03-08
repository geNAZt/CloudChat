package net.cubespace.CloudChat.Module.ChannelManager;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.ChannelBinder;
import net.cubespace.CloudChat.Command.Binder.JoinedChannelBinder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Channels;
import net.cubespace.CloudChat.Module.ChannelManager.Command.CreateChannel;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Invite;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PermissionChangedListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerSendMessageListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChannelManagerModule extends Module {
    @Override
    public void onLoad() {
        //Register the Manager
        plugin.getManagerRegistry().registerManager("channelManager", new ChannelManager(plugin, this));
    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        //Register the Listener
        plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new AsyncChatListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PermissionChangedListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerSendMessageListener(plugin));

        //Register Commands
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("join"), ChannelBinder.class, commandAliases.Join.toArray(new String[0]));
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("channels"), ChannelBinder.class, commandAliases.Channels.toArray(new String[0]));
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("leave"), JoinedChannelBinder.class, commandAliases.Leave.toArray(new String[0]));
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("createchannel"), Binder.class, commandAliases.Createchannel.toArray(new String[0]));
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("invite"), PlayerBinder.class, commandAliases.Invite.toArray(new String[0]));
        plugin.getBindManager().bind(commandAliases.BaseCommands.get("focus"), JoinedChannelBinder.class, commandAliases.Focus.toArray(new String[0]));

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("list"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("list"), Binder.class, commandAliases.List.toArray(new String[0]));
        }

        plugin.getCommandExecutor().add(this, new Channels(plugin));
        plugin.getCommandExecutor().add(this, new CreateChannel(plugin));
        plugin.getCommandExecutor().add(this, new Invite(plugin));
    }

    @Override
    public void onDisable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        //Unbind the Listeners
        plugin.getAsyncEventBus().removeListener(this);

        //Unbind commands
        plugin.getBindManager().unbind(commandAliases.BaseCommands.get("join"));
        plugin.getBindManager().unbind(commandAliases.BaseCommands.get("leave"));
        plugin.getBindManager().unbind(commandAliases.BaseCommands.get("createchannel"));
        plugin.getBindManager().unbind(commandAliases.BaseCommands.get("invite"));
        plugin.getBindManager().unbind(commandAliases.BaseCommands.get("focus"));

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("list"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("list"));
        }

        plugin.getCommandExecutor().remove(this);
    }
}
