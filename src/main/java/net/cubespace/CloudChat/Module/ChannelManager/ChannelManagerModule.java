package net.cubespace.CloudChat.Module.ChannelManager;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.ChannelBinder;
import net.cubespace.CloudChat.Command.Binder.JoinedChannelBinder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Channels;
import net.cubespace.CloudChat.Module.ChannelManager.Command.CreateChannel;
import net.cubespace.CloudChat.Module.ChannelManager.Command.Invite;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PermissionChangedListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.ChannelManager.Listener.PlayerQuitListener;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChannelManagerModule extends Module {
    public ChannelManagerModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        //Register the Manager
        plugin.getManagerRegistry().registerManager("channelManager", new ChannelManager(plugin));
    }

    @Override
    public void onEnable() {
        //Register the Listener
        plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new AsyncChatListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PermissionChangedListener(plugin));

        //Register Commands
        plugin.getBindManager().bind("join", ChannelBinder.class);
        plugin.getBindManager().bind("leave", JoinedChannelBinder.class);
        plugin.getBindManager().bind("createchannel", Binder.class);
        plugin.getBindManager().bind("invite", PlayerBinder.class);
        plugin.getBindManager().bind("focus", JoinedChannelBinder.class);

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("list")) {
            plugin.getBindManager().bind("list", Binder.class);
        }

        plugin.getCommandExecutor().add(this, new Channels(plugin));
        plugin.getCommandExecutor().add(this, new CreateChannel(plugin));
        plugin.getCommandExecutor().add(this, new Invite(plugin));
    }

    @Override
    public void onDisable() {
        //Unbind the Listeners
        plugin.getAsyncEventBus().removeListener(this);

        //Unbind commands
        plugin.getBindManager().unbind("join");
        plugin.getBindManager().unbind("leave");
        plugin.getBindManager().unbind("createchannel");
        plugin.getBindManager().unbind("invite");
        plugin.getBindManager().unbind("focus");

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("list")) {
            plugin.getBindManager().unbind("list");
        }

        plugin.getCommandExecutor().remove(this);
    }
}
