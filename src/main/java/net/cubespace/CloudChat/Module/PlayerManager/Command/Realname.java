package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 13:13
 */
public class Realname implements CLICommand {
    private CubespacePlugin plugin;

    public Realname(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command="realname", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Realname lokup");

        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            plugin.getPluginLogger().debug("Direct lookup returned null");
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                plugin.getPluginLogger().debug("Autocomplete lookup returned null");
                player = NicknameParser.getPlayer(plugin, args[0]);

                if(player == null) {
                    plugin.getPluginLogger().debug("Nickname Parser returned null");
                    MessageBuilder messageBuilder = new MessageBuilder();
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Realname_OfflinePlayer)).send(sender);
                    return;
                }
            }
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Realname_Success.replace("%nick", args[0]).replace("%realname", player.getName()))).send(sender);
    }
}
