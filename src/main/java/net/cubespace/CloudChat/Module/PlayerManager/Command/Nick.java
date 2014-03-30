package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Nick implements CLICommand {
    private CubespacePlugin plugin;

    public Nick(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "nick", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Nickchange");

        if (args.length > 1) {
            if (sender instanceof ProxiedPlayer && !plugin.getPermissionManager().has(sender, "cloudchat.command.nick.other")) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_NoPermissionToChangeOther)).send(sender);
                return;
            }

            ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
            if (player == null) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_OfflinePlayer)).send(sender);
                return;
            }

            PlayerDatabase playerDatabase = ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).get(player.getName());

            //Check if this is a "off" Request
            if (args[1].equalsIgnoreCase("off")) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_ResetNick)).send(sender);

                playerDatabase.Nick = playerDatabase.Realname;
                player.setDisplayName(FontFormat.translateString(playerDatabase.Realname));
            } else {
                //Fire the correct Event to check if this is ok
                plugin.getAsyncEventBus().callEvent(new PlayerNickchangeEvent(player, playerDatabase.Nick, args[1]));
            }
        } else {
            //Check if the Sender is a Player since we only can change Players Nicknames
            if (!(sender instanceof ProxiedPlayer)) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_NotPlayer)).send(sender);

                return;
            }

            PlayerDatabase playerDatabase = ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).get(sender.getName());

            if (args[0].equalsIgnoreCase("off")) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_ResetNick)).send(sender);

                playerDatabase.Nick = playerDatabase.Realname;
                ((ProxiedPlayer) sender).setDisplayName(FontFormat.translateString(playerDatabase.Realname));
            } else {
                //Fire the correct Event to check if this is ok
                plugin.getAsyncEventBus().callEvent(new PlayerNickchangeEvent(sender, playerDatabase.Nick, args[0]));
            }
        }
    }
}
