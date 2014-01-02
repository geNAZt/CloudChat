package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Message.FactionChatMessage;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
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
        ChannelDatabase channelDatabase = channelManager.get(playerDatabase.Focus);

        Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, factionChatMessage.getMessage()));

        plugin.getPluginLogger().debug("Got Faction Chat message for " + player.getName() + ": " + factionChatMessage.getMessage());
    }
}