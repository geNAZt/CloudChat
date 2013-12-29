package net.cubespace.CloudChat.Module.Mute.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:32
 */
public class Mute implements CLICommand {
    private CloudChatPlugin plugin;
    private MuteModule muteModule;

    public Mute(MuteModule muteModule, CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.muteModule = muteModule;
    }

    @Command(command = "mute", arguments = 1)
    public void muteCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You only can mute as Player");
            return;
        }

        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                sender.sendMessage("You can't mute offline Players");
                return;
            }
        }

        muteModule.getMuteManager().addPlayerMute(sender.getName(), player.getName());
        sender.sendMessage("You muted " + player.getName());
    }

    @Command(command = "unmute", arguments = 1)
    public void unMuteCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You only can mute as Player");
            return;
        }

        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                sender.sendMessage("You can't unmute offline Players");
                return;
            }
        }

        muteModule.getMuteManager().removePlayerMute(sender.getName(), player.getName());
        sender.sendMessage("You unmuted " + player.getName());
    }

    @Command(command = "cc:mute", arguments = 1)
    public void ccMuteCommand(CommandSender sender, String[] args) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                sender.sendMessage("You can't mute offline Players");
                return;
            }
        }

        muteModule.getMuteManager().addGlobalMute(player.getName());
        sender.sendMessage("You muted " + player.getName());
    }

    @Command(command = "cc:unmute", arguments = 1)
    public void ccUnMuteCommand(CommandSender sender, String[] args) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                sender.sendMessage("You can't unmute offline Players");
                return;
            }
        }

        muteModule.getMuteManager().removeGlobalMute(player.getName());
        sender.sendMessage("You unmuted " + player.getName());
    }
}
