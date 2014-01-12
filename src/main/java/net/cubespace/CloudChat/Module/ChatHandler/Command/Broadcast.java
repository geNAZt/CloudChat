package net.cubespace.CloudChat.Module.ChatHandler.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Broadcast implements CLICommand {
    private CubespacePlugin plugin;

    public Broadcast(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "broadcast", arguments = 1)
    public void broadcastCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_BroadCast_Message.replace("%message", StringUtils.join(args, " "))));

        for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            messageBuilder.send(player);
        }
    }
}
