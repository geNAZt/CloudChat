package net.cubespace.lib.PluginMessage;

import com.iKeirNez.PluginMessageApiPlus.PacketPlayer;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
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
                if(pluginMessage.getPlayer() == null) continue;
                StandardPacket message = pluginMessage.getPacket();

                if(plugin.getPluginLogger().getLogLevel().getLevelCode().equals(Level.DEBUG.getLevelCode())) {
                    plugin.getPluginLogger().debug("Sending PluginMessage on '" + channel + "': " + message.toString());
                }

                pluginMessageManager.getPacketManager().sendPacket(new PacketPlayer(pluginMessage.getPlayer()), message);
            }
        } catch(Exception e) {
            plugin.getPluginLogger().error("PluginMessageTask was interrupted", e);
            throw new RuntimeException();
        }
    }

}
