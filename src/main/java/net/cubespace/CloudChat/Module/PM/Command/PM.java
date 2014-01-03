package net.cubespace.CloudChat.Module.PM.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.AutoComplete;
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
            plugin.getPluginLogger().debug("Registering the /msg command");
            plugin.getProxy().getPluginManager().registerCommand(plugin, new PlayerBinder(plugin, "msg", "m", "t", "tell"));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("reply")) {
            plugin.getPluginLogger().debug("Registering the /reply command");
            plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "reply", "r"));
        }

        plugin.getCommandExecutor().add(this);
    }

    @Command(command = "msg", arguments = 2)
    public void msgCommand(CommandSender sender, String[] args) {
        plugin.getPluginLogger().debug("Got a new PM");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But sender was not a Player");
            sender.sendMessage("You only can PM as a Player");
            return;
        }

        String player = args[0];
        ProxiedPlayer rec = plugin.getProxy().getPlayer(player);
        ProxiedPlayer sen = (ProxiedPlayer) sender;
        if(rec == null) {
            plugin.getPluginLogger().debug("Direct lookup returned null");

            //Check for autocomplete
            player = AutoComplete.completeUsername(player);
            rec = plugin.getProxy().getPlayer(player);

            if(rec == null) {
                plugin.getPluginLogger().debug("Autocomplete lookup returned null");
                rec = NicknameParser.getPlayer(plugin, player);

                if(rec == null) {
                    plugin.getPluginLogger().debug("Nickname parsing returned null");
                    sender.sendMessage(FontFormat.translateString("&7The player is not online"));
                    return;
                }
            }
        }

        if (sen.equals(rec)) {
            plugin.getPluginLogger().debug("Sender and Receiver are equal.");
            sender.sendMessage(FontFormat.translateString("&7You cannot send a pm to yourself"));
            return;
        }

        String message = FontFormat.translateString(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
        PMEvent event = new PMEvent(sen.getName(), rec.getName(), message);
        plugin.getAsyncEventBus().callEvent(event);
    }

    @Command(command = "reply", arguments = 1)
    public void replyCommand(CommandSender sender, String[] args) {
        plugin.getPluginLogger().debug("Got a PM Reply");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But it was not send from a Player");
            sender.sendMessage("You only can PM as a Player");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        if(playerDatabase.Reply.equals("")) {
            plugin.getPluginLogger().debug("Can't be replied because the Player has no conversation up");
            sender.sendMessage(FontFormat.translateString("&7You can't reply because you have no PM conversation"));
            return;
        }

        ProxiedPlayer rec = plugin.getProxy().getPlayer(playerDatabase.Reply);
        if(rec == null) {
            plugin.getPluginLogger().debug("The Reply Player is not online anymore");
            sender.sendMessage(FontFormat.translateString("&7The player is not online"));
            playerDatabase.Reply = "";
            return;
        }

        String message = FontFormat.translateString(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
        PMEvent event = new PMEvent(player.getName(), rec.getName(), message);
        plugin.getAsyncEventBus().callEvent(event);
    }
}
