package net.cubespace.CloudChat.Module.ChannelManager.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:51
 */
public class Channels implements CLICommand {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;
    private PlayerManager playerManager;

    public Channels(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "join", arguments = 1)
    public void joinCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can join Channels");
            return;
        }

        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
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

        if(channelManager.join((ProxiedPlayer) sender, channelDatabase)) {
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
        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        if((channelDatabase.ForceIntoWhenPermission || channelDatabase.Forced) && !(sender.hasPermission("cloudchat.channel." + channelDatabase.Name + ".canleave") || sender.hasPermission("cloudchat.canleaveforced"))) {
            sender.sendMessage("You can't leave a forced Channel");
            return;
        }

        channelManager.leave((ProxiedPlayer) sender, channelDatabase);
        sender.sendMessage("You have left Channel: " + channelDatabase.Name);

        if(playerManager.get(player.getName()).Focus.equals(channelDatabase.Name)) {
            sender.sendMessage("You now focus Channel: " + ((Main) plugin.getConfigManager().getConfig("main")).Global);
            playerManager.get(player.getName()).Focus = ((Main) plugin.getConfigManager().getConfig("main")).Global;
        }
    }

    @Command(command = "focus", arguments = 1)
    public void focusCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only Players can join Channels");
            return;
        }

        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ArrayList<ProxiedPlayer> playersInChannel = channelManager.getAllInChannel(channelDatabase);
        if(!playersInChannel.contains(player)) {
            sender.sendMessage("You can't focus a Channel where you aren't in");
            return;
        }

        playerManager.get(player.getName()).Focus = channelDatabase.Name;
        sender.sendMessage("You have focused Channel: " + channelDatabase.Name);
    }
}
