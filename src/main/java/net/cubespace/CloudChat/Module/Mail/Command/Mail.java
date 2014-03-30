package net.cubespace.CloudChat.Module.Mail.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 04.01.14 17:29
 */
public class Mail implements CLICommand {
    private PlayerManager playerManager;
    private CubespacePlugin plugin;

    public Mail(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @Command(command = "mail", arguments = 0)
    public void mailDefaultCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        for(String line : messages.Command_Mail_HelpText.split("\n")) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(line)).send(sender);
        }
    }

    @Command(command = "mail send", arguments = 2)
    public void mailSendCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        boolean save = false;

        //Try to load the PlayerDatabase if not already loaded
        if(!playerManager.isLoaded(args[0])) {
            save = true;

            if(!playerManager.exists(args[0])) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Send_UnknownPlayer)).send(sender);
                return;
            }

            playerManager.load(args[0]);
        }

        PlayerDatabase playerDatabase = playerManager.get(args[0]);
        if (playerDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Send_UnknownPlayer)).send(sender);
            return;
        }

        net.cubespace.CloudChat.Module.Mail.Database.Mail mail = new net.cubespace.CloudChat.Module.Mail.Database.Mail();
        mail.date = new Date();
        mail.sender = sender.getName();

        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
        mail.message = legacyMessageBuilder.getString();

        playerDatabase.Mails.add(mail);

        if(save) {
            playerManager.save(args[0]);
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Send_Success)).send(sender);
    }

    @Command(command = "mail read", arguments = 0)
    public void mailReadCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        //Check if the Mail comes from the Console
        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Read_NotPlayer)).send(sender);
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        //Get all mails
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Read_Header)).send(sender);
        for(net.cubespace.CloudChat.Module.Mail.Database.Mail mail : playerDatabase.Mails) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(FontFormat.translateString(mail.sender + ": " + mail.message)).send(sender);
        }
    }

    @Command(command = "mail clear", arguments = 0)
    public void mailClearCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        //Check if the Mail comes from the Console
        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Clear_NotPlayer)).send(sender);
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        playerDatabase.Mails = new ArrayList<>();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Mail_Clear_Success)).send(sender);

    }
}
