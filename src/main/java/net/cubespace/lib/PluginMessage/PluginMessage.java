package net.cubespace.lib.PluginMessage;

import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:02
 */
public class PluginMessage implements IPluginMessage {
    private ProxiedPlayer player;
    private StandardPacket packet;

    public PluginMessage(ProxiedPlayer player, StandardPacket packet) {
        this.player = player;
        this.packet = packet;
    }

    @Override
    public StandardPacket getPacket(CubespacePlugin plugin) {
        return packet;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return player;
    }
}
