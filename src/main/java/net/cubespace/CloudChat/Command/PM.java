package net.cubespace.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Util.FontFormat;
import net.cubespace.CloudChat.Util.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

/**
 * Created by Fabian on 30.11.13.
 */
public class PM implements CLICommand {
    private CloudChatPlugin plugin;

    public PM(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "msg", arguments = 2)
    public void msgCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You only can PM as a Player");
            return;
        }

        String player = args[0];
        ProxiedPlayer rec = plugin.getProxy().getPlayer(player);
        ProxiedPlayer sen = (ProxiedPlayer) sender;
        if(rec == null) {
            sender.sendMessage(FontFormat.translateString("&7The player is not online"));
            return;
        }

        if (sen.equals(rec)) {
            sender.sendMessage(FontFormat.translateString("&7You cannot send a pm to yourself"));
            return;
        }

        String message = FontFormat.translateString(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
        rec.sendMessage(FontFormat.translateString("&6" + sen.getName() +" &8 -> &6You&8:&7 " + message));
        sen.sendMessage(FontFormat.translateString("&6You&8 -> &6"+ rec.getName() +"&8:&7 " + message));

        plugin.getPlayerManager().get(sen).Reply = rec.getName();
        plugin.getPlayerManager().get(rec).Reply = sen.getName();
    }

    @Command(command = "reply", arguments = 1)
    public void replyCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You only can PM as a Player");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

        if(playerDatabase.Reply.equals("")) {
            sender.sendMessage(FontFormat.translateString("&7You can't reply because you have no PM conversation"));
            return;
        }

        ProxiedPlayer rec = plugin.getProxy().getPlayer(playerDatabase.Reply);
        if(rec == null) {
            sender.sendMessage(FontFormat.translateString("&7The player is not online"));
            playerDatabase.Reply = "";
            return;
        }

        String message = FontFormat.translateString(StringUtils.join(args, " "));
        rec.sendMessage(FontFormat.translateString("&6" + player.getName() +" &8 -> &6You&8:&7 " + message));
        player.sendMessage(FontFormat.translateString("&6You&8 -> &6"+ rec.getName() +"&8:&7 " + message));

        plugin.getPlayerManager().get(player).Reply = rec.getName();
        plugin.getPlayerManager().get(rec).Reply = player.getName();
    }
}
