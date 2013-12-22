package net.cubespace.CloudChat.Command;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Util.FontFormat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 22.12.13 12:12
 */
public class Invite implements CLICommand {
    private CloudChatPlugin plugin;

    public Invite(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "invite", arguments = 1)
    public void invite(CommandSender sender, String[] args) {
        String player = args[0];
        ProxiedPlayer rec = plugin.getProxy().getPlayer(player);
        if(rec == null) {
            sender.sendMessage(FontFormat.translateString("&7The player is not online"));
            return;
        }

        //Check for an optional Argument
        String channel;
        if(args.length > 1) {
            channel = args[1];
        } else {
            if(!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage("If you are not ingame you must give a channel as second Argument");
                return;
            }

            ProxiedPlayer sen = (ProxiedPlayer) sender;
            PlayerDatabase playerDatabase = plugin.getPlayerManager().get(sen);
            channel = playerDatabase.Focus;
        }

        //Get the Channel
        ChannelDatabase channelDatabase = plugin.getChannelManager().get(channel);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel doesn't exist");
            return;
        }

        if(!channelDatabase.CanInvite.contains(sender.getName())) {
            sender.sendMessage("You can't send invitations for this Channel");
            return;
        }

        //Send out the Invitation to the User
        rec.sendMessage("You got an Invitation to the " + channelDatabase.Name + " Channel. Join with /join " + channelDatabase.Name + " " + channelDatabase.Password);
        rec.setPermission("cloudchat.channel." + channelDatabase.Name, true);
    }
}
