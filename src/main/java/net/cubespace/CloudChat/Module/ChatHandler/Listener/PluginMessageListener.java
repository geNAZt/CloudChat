package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.Config.Factions;
import net.cubespace.CloudChat.Config.Towny;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.ChatMessage;
import net.cubespace.PluginMessages.FactionChatMessage;
import net.cubespace.PluginMessages.SendChatMessage;
import net.cubespace.PluginMessages.TownyChatMessage;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:09
 */
public class PluginMessageListener implements PacketListener {
    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private CubespacePlugin plugin;

    public PluginMessageListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.plugin = plugin;
    }

    @PacketHandler
    public void onTownyChatMessage(TownyChatMessage townyChatMessage) {
        ProxiedPlayer player = townyChatMessage.getSender().getBungeePlayer();
        if (player == null) return; // Race condition -> Player disconnected before the Message has come in

        Towny towny = plugin.getConfigManager().getConfig("towny");
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + townyChatMessage.getSender().getName());

        ChannelDatabase channelDatabase = null;
        if(townyChatMessage.getMode().equals("town")) {
            channelDatabase = channelManager.get(towny.TownChannel);
        }

        if(townyChatMessage.getMode().equals("nation")) {
            channelDatabase = channelManager.get(towny.NationChannel);
        }

        if(channelDatabase != null) {
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            String message = MessageFormat.format(channelDatabase.Format.replace("%message", townyChatMessage.getMessage()).replace("%nation", townyChatMessage.getNationName()).replace("%town", townyChatMessage.getTownName()), channelDatabase, playerDatabase);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("playerMenu", clickEvent);
            messageBuilder.setText(message);
            for(String playerToSend : townyChatMessage.getPlayers()) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
            }
        }
    }

    @PacketHandler
    public void onFactionChatMessage(FactionChatMessage factionChatMessage){
        ProxiedPlayer player = factionChatMessage.getSender().getBungeePlayer();
        if (player == null) return; // Race condition -> Player disconnected before the Message has come in


        
        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        Factions factions = plugin.getConfigManager().getConfig("factions");

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + factionChatMessage.getSender().getName());

        if(factionChatMessage.getMode().equals("global")) {
            ChannelDatabase channelDatabase = channelManager.get(playerDatabase.Focus);
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);

            plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, factionChatMessage.getMessage()));

            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        } else {
            ChannelDatabase channelDatabase = null;
            if(factionChatMessage.getMode().equals("faction")) {
                channelDatabase = channelManager.get(factions.FactionChannel);
            }

            if(factionChatMessage.getMode().equals("ally")) {
                channelDatabase = channelManager.get(factions.AllyChannel);
            }

            if(factionChatMessage.getMode().equals("allyandtruce")) {
                channelDatabase = channelManager.get(factions.AllyAndTruceChannel);
            }

            if(factionChatMessage.getMode().equals("truce")) {
                channelDatabase = channelManager.get(factions.TruceChannel);
            }

            if(factionChatMessage.getMode().equals("enemy")) {
                channelDatabase = channelManager.get(factions.EnemyChannel);
            }

            if(channelDatabase != null) {
                Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
                String message = MessageFormat.format(channelDatabase.Format.replace("%faction", factionChatMessage.getFactionName()).replace("%message", factionChatMessage.getMessage()), channelDatabase, playerDatabase);

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.addEvent("playerMenu", clickEvent);
                messageBuilder.setText(message);
                for(String playerToSend : factionChatMessage.getPlayers()) {
                    plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerToSend), messageBuilder, sender));
                }

                plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
            }
        }
    }

    @PacketHandler
    public void onSendChatMessage(SendChatMessage sendChatMessage) {
        ProxiedPlayer player = sendChatMessage.getSender().getBungeePlayer();
        if (player == null) return; // Race condition -> Player disconnected before the Message has come in


        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        ChannelDatabase channelDatabase = channelManager.get(playerDatabase.Focus);
        Sender sender = new Sender(sendChatMessage.getSender().getName(), channelDatabase, playerDatabase);

        //Secure the message against click events
        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(sendChatMessage.getMessage());


        //JR start
        // Not best way to do it, but will prevent dirty double formatting.
        // You will probably want to do something a bit more clean.
        String message;

        if (ChatColor.stripColor(sendChatMessage.getMessage()).equalsIgnoreCase(sendChatMessage.getMessage()))
        {
            message = MessageFormat.format(channelDatabase.Format.replace("%message", legacyMessageBuilder.getString()), channelDatabase, playerDatabase);   
        }
        else
        {
            message = sendChatMessage.getMessage();
        }
        
        //JR end

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + sendChatMessage.getSender().getName());

        for(String playerToSend : sendChatMessage.getTo()) {
            ProxiedPlayer proxyPlayer = plugin.getProxy().getPlayer(playerToSend);
            if(proxyPlayer == null) continue;

            if(!channelManager.getAllJoinedChannels(proxyPlayer).contains(channelDatabase)) continue;

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("playerMenu", clickEvent);
            messageBuilder.setText(message);

            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(proxyPlayer, messageBuilder, sender));
        }

        plugin.getPluginLogger().info(message);
    }

    @PacketHandler
    public void onChatMessage(ChatMessage chatMessage) {
        plugin.getAsyncEventBus().callEvent(new AsyncChatEvent(chatMessage.getSender().getBungeePlayer(), chatMessage.getMessage()));
    }
}