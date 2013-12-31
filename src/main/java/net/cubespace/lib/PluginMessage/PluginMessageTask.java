package net.cubespace.lib.PluginMessage;

import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Logger.Level;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:18
 */
public class PluginMessageTask implements Runnable {
    private final CubespacePlugin plugin;
    private final PluginMessageManager pluginMessageManager;
    private final String channel;

    public PluginMessageTask(PluginMessageManager pluginMessageManager, CubespacePlugin plugin, String channel) {
        this.plugin = plugin;
        this.pluginMessageManager = pluginMessageManager;
        this.channel = channel;
    }

    public void run() {
        try {
            while(true) {
                IPluginMessage pluginMessage = pluginMessageManager.getQueue().take();
                byte[] message = pluginMessage.send(plugin);

                if(plugin.getPluginLogger().getLogLevel().getLevelCode().equals(Level.DEBUG.getLevelCode())) {
                    StringBuilder sb = new StringBuilder();
                    for (byte theByte : message) {
                        sb.append(Integer.toHexString(theByte));
                        sb.append(" ");
                    }

                    plugin.getPluginLogger().debug("Sending PluginMessage on '" + channel + "': " + sb.toString());
                }

                pluginMessage.getPlayer().getServer().sendData(channel, message);
            }
        } catch(Exception e) {
            plugin.getPluginLogger().error("PluginMessageTask was interrupted", e);
            throw new RuntimeException();
        }
    }

}
