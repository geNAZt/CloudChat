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
        //Channel things
        String output = channel.Format;
        output = output.replace("%channel_short", channel.Short);
        output = output.replace("%channel_name", channel.Name);

        //Player things
        output = output.replace("%nick", playerDatabase.Nick);
        output = output.replace("%prefix", playerDatabase.Prefix);
        output = output.replace("%suffix", playerDatabase.Suffix);

        //Message things
        output = output.replace("%message", message);

        return FontFormat.translateString(output);
    }

    public static String format(String message, ChannelDatabase channel, IRCSender sender) {
        //Channel things
        String output = channel.Format;
        output = output.replace("%channel_short", channel.Short);
        output = output.replace("%channel_name", channel.Name);

        //Player things
        output = output.replace("%nick", sender.getNick());
        output = output.replace("%prefix", "");
        output = output.replace("%suffix", "");

        //Message things
        output = output.replace("%message", message);

        return FontFormat.translateString(output);
    }
}
