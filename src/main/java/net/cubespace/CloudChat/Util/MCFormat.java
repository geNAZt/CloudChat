package net.cubespace.CloudChat.Util;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.IRC.IRCSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:27
 */
public class MCFormat {
    public static String format(CloudChatPlugin plugin, IRCSender sender, String message) {
        String format = plugin.getIrcConfig().Format;
        ChannelDatabase channelDatabase = plugin.getChannelManager().get(plugin.getIrcConfig().RelayChannels);

        format = format.replace("%channel", channelDatabase.Short);
        format = format.replace("%name", plugin.getIrcConfig().IngameName);
        format = format.replace("%nick", sender.getNick());
        format = format.replace("%message", message);

        format = FontFormat.translateString(format);
        format = IrcToMCFormat.translateString(format);

        return format;
    }
}
