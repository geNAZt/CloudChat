package net.cubespace.CloudChat.Module.Mute;

import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Module.Mute.Command.Mute;
import net.cubespace.CloudChat.Module.Mute.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PMListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.Mute.Listener.PlayerQuitListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:29
 */
public class MuteModule extends Module {
    private MuteManager muteManager;

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
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        plugin.getBindManager().bind("mute", PlayerBinder.class, commandAliases.Mute.toArray(new String[0]));
        plugin.getBindManager().bind("unmute", PlayerBinder.class, commandAliases.Unmute.toArray(new String[0]));
        plugin.getBindManager().bind("cc:mute", PlayerBinder.class, commandAliases.CCMute.toArray(new String[0]));
        plugin.getBindManager().bind("cc:unmute", PlayerBinder.class, commandAliases.CCUnmute.toArray(new String[0]));

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
