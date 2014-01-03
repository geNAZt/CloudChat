package net.cubespace.lib.PluginMessage;

import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import com.iKeirNez.PluginMessageApiPlus.PacketManager;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import com.iKeirNez.PluginMessageApiPlus.implementations.BungeeCordPacketManager;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:29
 */
public class PluginMessageManager {
    private PacketManager packetManager;
    private CubespacePlugin plugin;
    private LinkedBlockingQueue<IPluginMessage> queue = new LinkedBlockingQueue<>();
    private ArrayList<Class> packetsToRegister = new ArrayList<>();
    private ArrayList<PacketListener> listenerToRegister = new ArrayList<>();

    public PluginMessageManager(CubespacePlugin plugin, String channel) {
        this.plugin = plugin;

        packetManager = new BungeeCordPacketManager(plugin, channel);

        plugin.getPluginLogger().info("Creating new PluginMessageManager for channel " + channel);
        plugin.getProxy().getScheduler().runAsync(plugin, new PluginMessageTask(this, plugin, channel));
    }

    public synchronized void sendPluginMessage(ProxiedPlayer player, StandardPacket packet) {
        plugin.getPluginLogger().debug("Got new PluginMessage for Player " + player.getName() + ": " + packet.toString());
        queue.add(new PluginMessage(player, packet));
    }

    public LinkedBlockingQueue<IPluginMessage> getQueue() {
        return queue;
    }

    public synchronized PacketManager getPacketManager() {
        return packetManager;
    }

    public void addPacketToRegister(Class clazz) {
        packetsToRegister.add(clazz);
    }

    public void addListenerToRegister(PacketListener listener) {
        listenerToRegister.add(listener);
    }

    public void finish() {
        for(Class clazz : packetsToRegister) {
            getPacketManager().registerPacket(clazz);
        }

        for(PacketListener listener : listenerToRegister) {
            getPacketManager().registerListener(listener);
        }

        packetsToRegister = null;
        listenerToRegister = null;
    }
}
