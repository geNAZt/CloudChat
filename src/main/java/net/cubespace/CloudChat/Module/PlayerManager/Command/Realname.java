package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Realname implements CLICommand {
    private final CubespacePlugin plugin;

    public Realname(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "realname", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Realname lokup");

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Realname_OfflinePlayer)).send(sender);
            return;
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Realname_Success.replace("%nick", args[0]).replace("%realname", player.getName()))).send(sender);
    }
}
