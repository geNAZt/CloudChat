package net.cubespace.CloudChat.Module.Spam;

import net.cubespace.CloudChat.Module.Spam.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Spam.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.Spam.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.Spam.Task.SpamDetector;
import net.cubespace.PluginMessages.DispatchCmdMessage;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:37
 */
public class SpamModule extends Module {
    private SpamManager spamManager;
    private ScheduledTask task;

    public SpamModule(CubespacePlugin plugin) {
        super(plugin);
    }

    public SpamManager getSpamManager() {
        return spamManager;
    }

    @Override
    public void onLoad() {
        spamManager = new SpamManager(plugin);
        plugin.getManagerRegistry().registerManager("spamManager", spamManager);
    }

    @Override
    public void onEnable() {
        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(this, plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(this));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(this));

        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, DispatchCmdMessage.class);

        task = plugin.getProxy().getScheduler().schedule(plugin, new SpamDetector(this, plugin), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        plugin.getAsyncEventBus().removeListener(this);

        task.cancel();

        plugin.getPluginMessageManager("CloudChat").removePacket(this);
    }
}
