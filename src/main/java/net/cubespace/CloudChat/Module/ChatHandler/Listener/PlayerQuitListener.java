package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 15:50
 */
public class PlayerQuitListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;

    public PlayerQuitListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        ArrayList<ProxiedPlayer> sent = new ArrayList<>();

        for(ChannelDatabase channel : channelManager.getAllJoinedChannels(event.getPlayer())) {
            ArrayList<ProxiedPlayer> inChannel = channelManager.getAllInChannel(channel);
            String message = MessageFormat.format(((Main) plugin.getConfigManager().getConfig("main")).Announce_PlayerQuitMessage, channel, playerDatabase);
            Sender sender = new Sender(event.getPlayer().getName(), channel, playerDatabase);

            for(ProxiedPlayer player : inChannel) {
                if(sent.contains(player)) continue;

                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, message, sender));
                sent.add(player);
            }
        }

        sent.clear();
    }
}
