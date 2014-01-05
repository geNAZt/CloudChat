package net.cubespace.CloudChat.Module.ChannelManager.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

    @Command(command = "list channel", arguments = 0)
    public void listChannelCommand(CommandSender sender, String[] args) {
        ChannelDatabase channelDatabase;
        if(args.length < 1 && !(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You can't list Players in a channel from console if you don't provide a channel. Issue /list channel <channel>");
            return;
        } else if(args.length >= 1) {
            channelDatabase = channelManager.getViaShortOrName(args[0]);
        } else {
            channelDatabase = channelManager.get(playerManager.get(sender.getName()).Focus);
        }

        if(channelDatabase == null) {
            sender.sendMessage("This Channel does not exist");
            return;
        }

        Main config = plugin.getConfigManager().getConfig("main");
        ArrayList<ProxiedPlayer> playersInChannel = channelManager.getAllInChannel(channelDatabase);
        sender.sendMessage("Players currently in channel " + channelDatabase.Name);

        StringBuilder sb = new StringBuilder();
        for(ProxiedPlayer player : playersInChannel) {
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            sb.append(MessageFormat.format(config.Command_ListChannel_Player, null, playerDatabase));

            if(playersInChannel.size() - playersInChannel.indexOf(player) != 1) {
                sb.append(", ");
            }

            if(sb.length() > 64) {
                sender.sendMessage(sb.toString());
                sb = new StringBuilder();
            }
        }

        if(sb.length() > 0) {
            sender.sendMessage(sb.toString());
        }
    }

    @Command(command = "list", arguments = 0)
    public void listCommand(CommandSender sender, String[] args) {
        Main config = plugin.getConfigManager().getConfig("main");

        sender.sendMessage("Players currently online");

        StringBuilder sb = new StringBuilder();
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        Iterator<ProxiedPlayer> playerIterator = players.iterator();
        while(playerIterator.hasNext()) {
            ProxiedPlayer player = playerIterator.next();
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            sb.append(MessageFormat.format(config.Command_List_Player, null, playerDatabase));

            if(playerIterator.hasNext()) {
                sb.append(", ");
            }

            if(sb.length() > 64) {
                sender.sendMessage(sb.toString());
                sb = new StringBuilder();
            }
        }

        if(sb.length() > 0) {
            sender.sendMessage(sb.toString());
        }
    }
}
