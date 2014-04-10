package net.cubespace.CloudChat.Module.ChatHandler.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Broadcast implements CLICommand {
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;

    public Broadcast(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "broadcast", arguments = 1)
    public void broadcastCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_BroadCast_Message.replace("%message", StringUtils.join(args, " "))));

        // Check if the second argument is a Channel
        for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
             messageBuilder.send(player);
        }
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "broadcast to", arguments = 1)
    public void broadcastToCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        MessageBuilder messageBuilder = new MessageBuilder();

        // Check if the second argument is a Channel
        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if (channelDatabase == null) {
            for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                messageBuilder.setText(MessageFormat.format(messages.Command_BroadCastToChannel_Message.replace("%message", StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ")), null, null));
                messageBuilder.send(player);
            }
        } else {
            for(ProxiedPlayer player : channelManager.getAllInChannel(channelDatabase)) {
                messageBuilder.setText(MessageFormat.format(messages.Command_BroadCastToChannel_Message.replace("%message", StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ")), channelDatabase, null));
                messageBuilder.send(player);
            }
        }
    }
}
