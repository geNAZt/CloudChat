package net.cubespace.CloudChat.Module.ChatHandler.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Clearchat implements CLICommand {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public Clearchat(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "clearchat", arguments = 0)
    public void broadcastCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(messages.Command_Clearchat_NotPlayer).send(sender);
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(" ");

        if(args.length == 0) {
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            Sender sender1 = new Sender(player.getName(), null, playerDatabase);

            //Fire off some rounds of empty Messages :)
            for(int i = 0; i < 30; i++) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, sender1, ""));
            }
        } else {
            switch(args[0]) {
                case "player":
                    if(!plugin.getPermissionManager().has(player, "cloudchat.command.clearchat.other")) {
                        MessageBuilder messageBuilder2 = new MessageBuilder();
                        messageBuilder2.setText(messages.Command_Clearchat_NoPermissionForOther).send(sender);
                        return;
                    }

                    if(args.length < 2) {
                        MessageBuilder messageBuilder2 = new MessageBuilder();
                        messageBuilder2.setText(messages.Command_Clearchat_NotEnoughArguments).send(sender);
                        return;
                    }

                    //Check if Player exists
                    ProxiedPlayer player1 = NicknameParser.getPlayer(plugin, args[1]);
                    if(player1 == null) {
                        MessageBuilder messageBuilder2 = new MessageBuilder();
                        messageBuilder2.setText(messages.Command_Clearchat_PlayerOffline).send(sender);
                        return;
                    }

                    PlayerDatabase playerDatabase = playerManager.get(player.getName());
                    Sender sender1 = new Sender(player.getName(), null, playerDatabase);
                    for(int i = 0; i < 30; i++) {
                        plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player1, messageBuilder, sender1, ""));
                    }

                    MessageBuilder messageBuilder2 = new MessageBuilder();
                    messageBuilder2.setText(messages.Command_Clearchat_ChatCleared).send(sender);
                    break;

                case "server":
                    if(!plugin.getPermissionManager().has(player, "cloudchat.command.clearchat.server")) {
                        MessageBuilder messageBuilder3 = new MessageBuilder();
                        messageBuilder3.setText(messages.Command_Clearchat_NoPermissionForServer).send(sender);
                        return;
                    }

                    if(args.length < 2) {
                        MessageBuilder messageBuilder3 = new MessageBuilder();
                        messageBuilder3.setText(messages.Command_Clearchat_NotEnoughArguments).send(sender);
                        return;
                    }

                    ServerInfo serverInfo = plugin.getProxy().getServerInfo(args[1]);
                    if(serverInfo == null) {
                        MessageBuilder messageBuilder3 = new MessageBuilder();
                        messageBuilder3.setText(messages.Command_Clearchat_NoServer).send(sender);
                        return;
                    }

                    for(ProxiedPlayer player2 : serverInfo.getPlayers()) {
                        for(int i = 0; i < 30; i++) {
                            PlayerDatabase playerDatabase1 = playerManager.get(player2.getName());
                            Sender sender2 = new Sender(player.getName(), null, playerDatabase1);
                            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player2, messageBuilder, sender2, ""));
                        }
                    }

                    break;

                case "global":
                    if(!plugin.getPermissionManager().has(player, "cloudchat.command.clearchat.global")) {
                        MessageBuilder messageBuilder3 = new MessageBuilder();
                        messageBuilder3.setText(messages.Command_Clearchat_NoPermissionForGlobal).send(sender);
                        return;
                    }

                    for(ProxiedPlayer player2 : plugin.getProxy().getPlayers()) {
                        for(int i = 0; i < 30; i++) {
                            PlayerDatabase playerDatabase1 = playerManager.get(player2.getName());
                            Sender sender2 = new Sender(player.getName(), null, playerDatabase1);
                            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player2, messageBuilder, sender2, ""));
                        }
                    }

                    break;

                default:
                    MessageBuilder messageBuilder3 = new MessageBuilder();
                    messageBuilder3.setText(messages.Command_Clearchat_InvalidMode).send(sender);
            }
        }
    }
}
