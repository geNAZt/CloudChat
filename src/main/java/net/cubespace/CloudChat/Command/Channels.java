package net.cubespace.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:51
 */
public class Channels implements CLICommand {
    private CloudChatPlugin plugin;

    public Channels(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "join", arguments = 1)
    public void joinCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can join Channels");
            return;
        }

        ChannelDatabase channelDatabase = plugin.getChannelManager().getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        if(!channelDatabase.Password.equals("")) {
            if(args.length == 1) {
                sender.sendMessage("This Channel is password protected");
                return;
            }

            String password = args[1];
            if(!channelDatabase.Password.equals(password)) {
                sender.sendMessage("The password for this Channel is wrong");
                return;
            }
        }

        if(plugin.getChannelManager().join((ProxiedPlayer) sender, channelDatabase)) {
            sender.sendMessage("You have joined Channel: " + channelDatabase.Name);
        }
    }

    @Command(command = "leave", arguments = 1)
    public void leaveCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can leave Channels");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ChannelDatabase channelDatabase = plugin.getChannelManager().getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        if(channelDatabase.Forced) {
            sender.sendMessage("You can't leave a forced Channel");
            return;
        }

        plugin.getChannelManager().leave((ProxiedPlayer) sender, channelDatabase);
        sender.sendMessage("You have left Channel: " + channelDatabase.Name);

        if(plugin.getPlayerManager().get(player).Focus.equals(channelDatabase.Name)) {
            sender.sendMessage("You now focus Channel: global");
            plugin.getPlayerManager().get(player).Focus = "global";
        }
    }

    @Command(command = "focus", arguments = 1)
    public void focusCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can join Channels");
            return;
        }

        ChannelDatabase channelDatabase = plugin.getChannelManager().getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(channelDatabase);
        if(!playersInChannel.contains(player)) {
            sender.sendMessage("You can't focus a Channel where you aren't in");
            return;
        }

        plugin.getPlayerManager().get(player).Focus = channelDatabase.Name;
        sender.sendMessage("You have focused Channel: " + channelDatabase.Name);
    }
}
