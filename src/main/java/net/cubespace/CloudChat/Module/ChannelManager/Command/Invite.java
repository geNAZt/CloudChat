package net.cubespace.CloudChat.Module.ChannelManager.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Invite implements CLICommand {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;
    private final ChannelManager channelManager;

    public Invite(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @Command(command = "invite", arguments = 1)
    public void invite(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        String player = args[0];
        ProxiedPlayer rec = NicknameParser.getPlayer(plugin, player);

        if(rec == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Invite_OfflinePlayer)).send(sender);
            return;
        }

        //Check for an optional Argument
        String channel;
        if(args.length > 1) {
            channel = args[1];
        } else {
            if(!(sender instanceof ProxiedPlayer)) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Invite_SecondArgumentError)).send(sender);

                return;
            }

            ProxiedPlayer sen = (ProxiedPlayer) sender;
            PlayerDatabase playerDatabase = playerManager.get(sen.getName());
            channel = playerDatabase.Focus;
        }

        //Get the Channel
        ChannelDatabase channelDatabase = channelManager.get(channel);
        if(channelDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Invite_ChannelDoesNotExist)).send(sender);

            return;
        }

        if(!channelDatabase.CanInvite.contains(sender.getName())) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Invite_CantInvite)).send(sender);

            return;
        }

        //Send out the Invitation to the User
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/join " + channelDatabase.Name + " " + channelDatabase.Password);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.addEvent("joinChannel", clickEvent);
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Invite_SendInvitation.replace("%channel", channelDatabase.Name).replace("%password", channelDatabase.Password))).send(rec);

        rec.setPermission("cloudchat.channel." + channelDatabase.Name, true);

        MessageBuilder messageBuilder2 = new MessageBuilder();
        messageBuilder2.setText(FontFormat.translateString(messages.Command_Channel_Invite_Notification.replace("%name", rec.getName()))).send(sender);
    }
}
