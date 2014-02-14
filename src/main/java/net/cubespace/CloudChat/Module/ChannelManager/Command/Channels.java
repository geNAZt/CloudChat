package net.cubespace.CloudChat.Module.ChannelManager.Command;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Channels implements CLICommand {
    private CubespacePlugin plugin;
    private ChannelManager channelManager;
    private PlayerManager playerManager;

    public Channels(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "join", arguments = 1)
    public void joinCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Join_OnlyPlayers)).send(sender);

            return;
        }

        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Join_ChannelDoesNotExist)).send(sender);

            return;
        }

        if(!channelDatabase.Password.equals("")) {
            if(args.length == 1) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Join_PasswordProtected)).send(sender);

                return;
            }

            String password = args[1];
            if(!channelDatabase.Password.equals(password)) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Join_WrongPassword)).send(sender);

                return;
            }
        }

        if(channelManager.join((ProxiedPlayer) sender, channelDatabase)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Join_JoinedChannel.replace("%channel", channelDatabase.Name))).send(sender);
        }
    }

    @Command(command = "leave", arguments = 1)
    public void leaveCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Leave_OnlyPlayers)).send(sender);

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Leave_ChannelDoesNotExist)).send(sender);

            return;
        }

        if((channelDatabase.ForceIntoWhenPermission || channelDatabase.Forced) && !(plugin.getPermissionManager().has(player, "cloudchat.channel." + channelDatabase.Name + ".canleave") || plugin.getPermissionManager().has(player, "cloudchat.canleaveforced"))) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Leave_ForcedChannel)).send(sender);

            return;
        }

        channelManager.leave((ProxiedPlayer) sender, channelDatabase);
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Leave_LeftChannel.replace("%channel", channelDatabase.Name))).send(sender);

        if(playerManager.get(player.getName()).Focus.equals(channelDatabase.Name)) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(FontFormat.translateString(messages.Command_Channel_Leave_FocusChannel.replace("%channel", channelDatabase.Name))).send(sender);

            playerManager.get(player.getName()).Focus = ((Main) plugin.getConfigManager().getConfig("main")).Global;
        }
    }

    @Command(command = "focus", arguments = 1)
    public void focusCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        if(!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_OnlyPlayers)).send(sender);

            return;
        }

        ChannelDatabase channelDatabase = channelManager.getViaShortOrName(args[0]);
        if(channelDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_ChannelDoesNotExist)).send(sender);

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ArrayList<ProxiedPlayer> playersInChannel = channelManager.getAllInChannel(channelDatabase);
        if(!playersInChannel.contains(player)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_NotIn)).send(sender);

            return;
        }

        playerManager.get(player.getName()).Focus = channelDatabase.Name.toLowerCase();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_FocusChannel.replace("%channel", channelDatabase.Name))).send(sender);
    }

    @SuppressWarnings("unused")
    @Command(command = "list channel", arguments = 0)
    public void listChannelCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        ChannelDatabase channelDatabase;
        if(args.length < 1 && !(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_ListChannel_ArgumentMissing)).send(sender);

            return;
        } else if(args.length >= 1) {
            channelDatabase = channelManager.getViaShortOrName(args[0]);
        } else {
            channelDatabase = channelManager.get(playerManager.get(sender.getName()).Focus);
        }

        if(channelDatabase == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_ListChannel_ChannelDoesNotExist)).send(sender);

            return;
        }

        ArrayList<ProxiedPlayer> playersInChannel = channelManager.getAllInChannel(channelDatabase);
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_ListChannel_Header.replace("%channel", channelDatabase.Name))).send(sender);

        StringBuilder sb = new StringBuilder();
        for(ProxiedPlayer player : playersInChannel) {
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            sb.append(MessageFormat.format(messages.Command_Channel_ListChannel_Player, null, playerDatabase));

            if(playersInChannel.size() - playersInChannel.indexOf(player) != 1) {
                sb.append(", ");
            }

            if(sb.length() > 64) {
                MessageBuilder messageBuilder2 = new MessageBuilder();
                messageBuilder2.setText(sb.toString()).send(sender);
                sb = new StringBuilder();
            }
        }

        if(sb.length() > 0) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(sb.toString()).send(sender);
        }
    }

    @SuppressWarnings("unused")
    @Command(command = "list", arguments = 0)
    public void listCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_List_Header)).send(sender);

        StringBuilder sb = new StringBuilder();
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        Iterator<ProxiedPlayer> playerIterator = players.iterator();
        while(playerIterator.hasNext()) {
            ProxiedPlayer player = playerIterator.next();
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            sb.append(MessageFormat.format(messages.Command_Channel_List_Player, null, playerDatabase));

            if(playerIterator.hasNext()) {
                sb.append(", ");
            }

            if(sb.length() > 64) {
                MessageBuilder messageBuilder2 = new MessageBuilder();
                messageBuilder2.setText(sb.toString()).send(sender);
                sb = new StringBuilder();
            }
        }

        if(sb.length() > 0) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(sb.toString()).send(sender);
        }
    }

    @SuppressWarnings("unused")
    @Command(command = "channels", arguments = 0)
    public void channelsCommand(final CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channels_Header)).send(sender);

        Iterable<String> iterable = Iterables.transform(Iterables.filter(channelManager.getChannels(), new Predicate<ChannelDatabase>() {
            @Override
            public boolean apply(ChannelDatabase database) {
                return plugin.getPermissionManager().has(sender, "cloudchat.channel." + database.Name);
            }
        }), new Function<ChannelDatabase, String>() {
            @Override
            public String apply(ChannelDatabase database) {
                return database.Name;
            }
        });

        Iterator<String> iterator = iterable.iterator();
        while(iterator.hasNext()) {
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channels_Channel.replace("%channel", iterator.next()))).send(sender);
        }
    }
}
