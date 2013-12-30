package net.cubespace.CloudChat.Module.FormatHandler.Format;

import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 22:44
 */
public class MessageFormat {
    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase) {
        return format(message, channel, playerDatabase, false);
    }

    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase, boolean stipcolor) {
        //Channel things
        String output = message;

        if(channel != null) {
            output = output.replace("%channel_short", channel.Short);
            output = output.replace("%channel_name", channel.Name);
        }

        //Player things
        output = output.replace("%nick", playerDatabase.Nick);
        output = output.replace("%prefix", playerDatabase.Prefix);
        output = output.replace("%suffix", playerDatabase.Suffix);

        //Server things
        output = output.replace("%server", playerDatabase.Server);

        //World things
        output = output.replace("%world_alias", playerDatabase.WorldAlias);
        output = output.replace("%world", playerDatabase.World);

        if(stipcolor)
            return FontFormat.stripColor(output);
        else
            return FontFormat.translateString(output);
    }
}
