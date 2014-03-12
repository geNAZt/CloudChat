package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.Config.Factions;
import net.cubespace.CloudChat.Config.Towny;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.ChatMessage;
import net.cubespace.PluginMessages.FactionChatMessage;
import net.cubespace.PluginMessages.LocalPlayersResponse;
import net.cubespace.PluginMessages.TownyChatMessage;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
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
            plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, townyChatMessage.getMessage(), townyChatMessage.getPlayers()));
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

        List<String> receipents = new ArrayList<String>(){{
            add("§ALL");
        }};

        ChannelDatabase channelDatabase = null;
        if(factionChatMessage.getMode().equals("global")) {
            channelDatabase = channelManager.get(playerDatabase.Focus);
            plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
        } else {
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
                receipents = factionChatMessage.getPlayers();

                plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
            } else {
                return;
            }
        }

        Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, factionChatMessage.getMessage(), receipents));
    }

    @PacketHandler
    public void onLocalPlayersResponse(LocalPlayersResponse localPlayersResponse) {
        ProxiedPlayer player = localPlayersResponse.getSender().getBungeePlayer();
        if (player == null) return; // Race condition -> Player disconnected before the Message has come in

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        ChannelDatabase channelDatabase = channelManager.get(localPlayersResponse.getChannel());
        Sender sender = new Sender(localPlayersResponse.getSender().getName(), channelDatabase, playerDatabase);

        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, localPlayersResponse.getMessage(), localPlayersResponse.getTo()));
    }

    @PacketHandler
    public void onChatMessage(ChatMessage chatMessage) {
        plugin.getAsyncEventBus().callEvent(new AsyncChatEvent(chatMessage.getSender().getBungeePlayer(), chatMessage.getMessage()));
    }
}