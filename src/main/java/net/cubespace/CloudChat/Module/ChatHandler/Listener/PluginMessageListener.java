package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Factions;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.PluginMessages.FactionChatMessage;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:09
 */
public class PluginMessageListener implements PacketListener {
    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private CloudChatPlugin plugin;

    public PluginMessageListener(CloudChatPlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.plugin = plugin;
    }

    @PacketHandler
    public void onFactionChatMessage(FactionChatMessage factionChatMessage){
        ProxiedPlayer player = factionChatMessage.getSender().getBungeePlayer();
        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        Factions factions = plugin.getConfigManager().getConfig("factions");

        if(factionChatMessage.getMode().equals("global")) {
            ChannelDatabase channelDatabase = channelManager.get(playerDatabase.Focus);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = channelDatabase.Format.replace("%message", factionChatMessage.getMessage());

            plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, message));

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }

        if(factionChatMessage.getMode().equals("faction")) {
            ChannelDatabase channelDatabase = channelManager.get(factions.FactionChannel);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(message);
            for(String playerToSend : factionChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }

        if(factionChatMessage.getMode().equals("ally")) {
            ChannelDatabase channelDatabase = channelManager.get(factions.AllyChannel);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(message);
            for(String playerToSend : factionChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }

        if(factionChatMessage.getMode().equals("allyandtruce")) {
            ChannelDatabase channelDatabase = channelManager.get(factions.AllyAndTruceChannel);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(message);
            for(String playerToSend : factionChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }

        if(factionChatMessage.getMode().equals("truce")) {
            ChannelDatabase channelDatabase = channelManager.get(factions.TruceChannel);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(message);
            for(String playerToSend : factionChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }

        if(factionChatMessage.getMode().equals("enemy")) {
            ChannelDatabase channelDatabase = channelManager.get(factions.EnemyChannel);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(message);
            for(String playerToSend : factionChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        }
    }
}