package net.cubespace.CloudChat.Module.Mail.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
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

    public Mail(CloudChatPlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "mail send", arguments = 2)
    public void mailSendCommand(CommandSender sender, String[] args) {
        //Check if the Mail comes from the Console
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can send mails");
            return;
        }

        boolean save = false;

        //Try to load the PlayerDatabase if not already loaded
        if(!playerManager.isLoaded(args[0])) {
            save = true;

            if(!playerManager.exists(args[0])) {
                sender.sendMessage("This Player is not known. Can not send a Mail to it");
                return;
            }

            playerManager.load(args[0]);
        }

        PlayerDatabase playerDatabase = playerManager.get(args[0]);
        net.cubespace.CloudChat.Module.Mail.Database.Mail mail = new net.cubespace.CloudChat.Module.Mail.Database.Mail();
        mail.date = new Date();
        mail.sender = sender.getName();
        mail.message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

        playerDatabase.Mails.add(mail);

        if(save) {
            playerManager.save(args[0]);
        }

        sender.sendMessage("Mail was sent");
    }

    @Command(command = "mail read", arguments = 0)
    public void mailReadCommand(CommandSender sender, String[] args) {
        //Check if the Mail comes from the Console
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can read mails");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        //Get all mails
        player.sendMessage("=== Mails ===");
        for(net.cubespace.CloudChat.Module.Mail.Database.Mail mail : playerDatabase.Mails) {
            player.sendMessage(mail.sender + ": " + mail.message);
        }
    }

    @Command(command = "mail clear", arguments = 0)
    public void mailClearCommand(CommandSender sender, String[] args) {
        //Check if the Mail comes from the Console
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can clear mails");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        playerDatabase.Mails = new ArrayList<>();

        player.sendMessage("All Mails have been cleared");
    }
}
