package net.cubespace.CloudChat.Module.Mute;

import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Module.Mute.Command.Mute;
import net.cubespace.CloudChat.Module.Mute.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PMListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerQuitListener;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:29
 */
public class MuteModule extends Module {
    private MuteManager muteManager;

    public MuteModule(CubespacePlugin plugin) {
        super(plugin);
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    @Override
    public void onLoad() {
        this.muteManager = new MuteManager();
        plugin.getManagerRegistry().registerManager("muteManager", muteManager);
    }

    @Override
    public void onEnable() {
        plugin.getBindManager().bind("mute", PlayerBinder.class);
        plugin.getBindManager().bind("unmute", PlayerBinder.class);
        plugin.getBindManager().bind("cc:mute", PlayerBinder.class);
        plugin.getBindManager().bind("cc:unmute", PlayerBinder.class);

        plugin.getCommandExecutor().add(this, new Mute(this, plugin));

        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(this));
        plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(this, plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(this, plugin));
        plugin.getAsyncEventBus().addListener(this, new PMListener(this));
    }

    @Override
    public void onDisable() {
        plugin.getBindManager().unbind("mute");
        plugin.getBindManager().unbind("unmute");
        plugin.getBindManager().unbind("cc:mute");
        plugin.getBindManager().unbind("cc:unmute");

        plugin.getCommandExecutor().remove(this);

        plugin.getAsyncEventBus().removeListener(this);
    }
}
