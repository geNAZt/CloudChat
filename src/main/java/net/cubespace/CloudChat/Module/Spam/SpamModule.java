package net.cubespace.CloudChat.Module.Spam;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.Spam.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Spam.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.Spam.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.Spam.Task.SpamDetector;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:37
 */
public class SpamModule {
    private SpamManager spamManager;

    public SpamModule(CloudChatPlugin plugin) {
        spamManager = new SpamManager(plugin);

        plugin.getAsyncEventBus().addListener(new ChatMessageListener(this, plugin));
        plugin.getAsyncEventBus().addListener(new PlayerJoinListener(this));
        plugin.getAsyncEventBus().addListener(new PlayerQuitListener(this));

        plugin.getProxy().getScheduler().schedule(plugin, new SpamDetector(this, plugin), 1, 1, TimeUnit.SECONDS);
    }

    public SpamManager getSpamManager() {
        return spamManager;
    }
}
