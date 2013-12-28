package net.cubespace.CloudChat.Module.PM.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

/**
 * Created by Fabian on 30.11.13.
 */
public class PM implements CLICommand {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;

    public PM(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("msg")) {
            plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "msg", "m", "t", "tell"));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("reply")) {
            plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "reply", "r"));
        }

        plugin.getCommandExecutor().add(this);
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

        playerManager.get(sen.getName()).Reply = rec.getName();
        playerManager.get(rec.getName()).Reply = sen.getName();
    }

    @Command(command = "reply", arguments = 1)
    public void replyCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You only can PM as a Player");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

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

        playerManager.get(player.getName()).Reply = rec.getName();
        playerManager.get(rec.getName()).Reply = player.getName();
    }
}
