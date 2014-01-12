package net.cubespace.lib.PluginMessage;

import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import com.iKeirNez.PluginMessageApiPlus.PacketManager;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import com.iKeirNez.PluginMessageApiPlus.implementations.BungeeCordPacketManager;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:29
 */
public class PluginMessageManager {
    private PacketManager packetManager;
    private CubespacePlugin plugin;
    private LinkedBlockingQueue<IPluginMessage> queue = new LinkedBlockingQueue<>();
    private HashMap<Module, ArrayList<Class<? extends StandardPacket>>> packetsToRegister = new HashMap<>();
    private HashMap<Module, ArrayList<PacketListener>> listenerToRegister = new HashMap<>();

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

    public void addPacketToRegister(Module module, Class<? extends StandardPacket> clazz) {
        if(!packetsToRegister.containsKey(module)) {
            packetsToRegister.put(module, new ArrayList<Class<? extends StandardPacket>>());
        }

        packetsToRegister.get(module).add(clazz);
    }

    public void addListenerToRegister(Module module, PacketListener listener) {
        if(!listenerToRegister.containsKey(module)) {
            listenerToRegister.put(module, new ArrayList<PacketListener>());
        }

        listenerToRegister.get(module).add(listener);
    }

    public void removeListener(PacketListener packetListener) {
        for(Map.Entry<Module, ArrayList<PacketListener>> listeners : new HashMap<>(listenerToRegister).entrySet()) {
            listenerToRegister.get(listeners.getKey()).remove(packetListener);
        }

        getPacketManager().unregisterListener(packetListener);
    }

    public void removeListener(Module module) {
        if(!listenerToRegister.containsKey(module)) return;

        for(PacketListener packetListener : listenerToRegister.get(module)) {
            getPacketManager().unregisterListener(packetListener);
        }

        listenerToRegister.remove(module);
    }

    public void removePacket(Class<? extends StandardPacket> clazz) {
        for(Map.Entry<Module, ArrayList<Class<? extends StandardPacket>>> packets : new HashMap<>(packetsToRegister).entrySet()) {
            packetsToRegister.get(packets.getKey()).remove(clazz);
        }

        getPacketManager().unregisterPacket(clazz);
    }

    public void removePacket(Module module) {
        if(!packetsToRegister.containsKey(module)) return;

        for(Class<? extends StandardPacket> packet : packetsToRegister.get(module)) {
            getPacketManager().unregisterPacket(packet);
        }

        packetsToRegister.remove(module);
    }

    public void finish() {
        for(ArrayList<Class<? extends StandardPacket>> classes : packetsToRegister.values()) {
            for(Class<? extends StandardPacket> clazz : classes) {
                getPacketManager().registerPacket(clazz);
            }
        }

        for(ArrayList<PacketListener> listeners : listenerToRegister.values()) {
            for(PacketListener listener : listeners) {
                getPacketManager().registerListener(listener);
            }
        }
    }
}
