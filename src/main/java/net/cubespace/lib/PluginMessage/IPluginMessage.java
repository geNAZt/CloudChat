package net.cubespace.lib.PluginMessage;

import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:45
 */
public interface IPluginMessage {
    public StandardPacket getPacket();
    public ProxiedPlayer getPlayer();
}
