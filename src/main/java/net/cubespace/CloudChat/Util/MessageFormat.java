package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.IRC.IRCSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 22:44
 */
public class MessageFormat {
    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase) {
        return format(message, channel, playerDatabase, false);
    }

    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase, boolean useMessageAsFormat) {
        //Channel things
        String output = channel.Format;
        if(useMessageAsFormat) {
            output = message;
        }

        output = output.replace("%channel_short", channel.Short);
        output = output.replace("%channel_name", channel.Name);

        //Player things
        output = output.replace("%nick", playerDatabase.Nick);
        output = output.replace("%prefix", playerDatabase.Prefix);
        output = output.replace("%suffix", playerDatabase.Suffix);

        //Message things
        if(!useMessageAsFormat) output = output.replace("%message", message);

        //World things
        output = output.replace("%world_alias", playerDatabase.WorldAlias);
        output = output.replace("%world", playerDatabase.World);

        return FontFormat.translateString(output);
    }

    public static String format(String message, ChannelDatabase channel, IRCSender sender, boolean format) {
        //Channel things
        String output = channel.Format;
        if(!format) {
            output = message;
        }

        output = output.replace("%channel_short", channel.Short);
        output = output.replace("%channel_name", channel.Name);

        //Player things
        output = output.replace("%nick", sender.getNick());
        output = output.replace("%prefix", "");
        output = output.replace("%suffix", "");

        //Message things
        if(format) output = output.replace("%message", message);

        //World things
        output = output.replace("%world_alias", "");
        output = output.replace("%world", "");

        return FontFormat.translateString(output);
    }
}
