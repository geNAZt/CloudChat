package net.cubespace.CloudChat.Module.FormatHandler.Format;

import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 22:44
 */
public class MessageFormat {
    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase) {
        //Channel things
        String output = message;

        output = output.replace("%channel_short", channel.Short);
        output = output.replace("%channel_name", channel.Name);

        //Player things
        output = output.replace("%nick", playerDatabase.Nick);
        output = output.replace("%prefix", playerDatabase.Prefix);
        output = output.replace("%suffix", playerDatabase.Suffix);

        //Server things
        output = output.replace("%server", playerDatabase.Server);

        //World things
        output = output.replace("%world_alias", playerDatabase.WorldAlias);
        output = output.replace("%world", playerDatabase.World);

        return FontFormat.translateString(output);
    }
}
