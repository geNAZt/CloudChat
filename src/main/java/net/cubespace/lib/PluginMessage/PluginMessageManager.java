package net.cubespace.lib.PluginMessage;

import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:29
 */
public class PluginMessageManager {
    private CubespacePlugin plugin;
    private ScheduledTask pluginMessageTask;
    private LinkedBlockingQueue<IPluginMessage> queue = new LinkedBlockingQueue<>();

    public PluginMessageManager(CubespacePlugin plugin, String channel) {
        this.plugin = plugin;

        plugin.getPluginLogger().info("Creating new PluginMessageManager for channel " + channel);

        pluginMessageTask = plugin.getProxy().getScheduler().runAsync(plugin, new PluginMessageTask(this, plugin, channel));
    }

    public synchronized void sendPluginMessage(IPluginMessage pluginMessage) {
        plugin.getPluginLogger().debug("Got new PluginMessage: " + pluginMessage.toString());
        queue.add(pluginMessage);
    }

    public LinkedBlockingQueue<IPluginMessage> getQueue() {
        return queue;
    }
}
