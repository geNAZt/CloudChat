package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;

    public PlayerJoinListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(plugin.getPermissionManager().has(event.getPlayer(), "cloudchat.bypass.announce.join")) return;

        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        ArrayList<ProxiedPlayer> sent = new ArrayList<>();

        for(ChannelDatabase channel : channelManager.getAllJoinedChannels(event.getPlayer())) {
            ArrayList<ProxiedPlayer> inChannel = channelManager.getAllInChannel(channel);
            String message = MessageFormat.format(((Messages) plugin.getConfigManager().getConfig("messages")).PlayerJoin, channel, playerDatabase);
            Sender sender = new Sender(event.getPlayer().getName(), channel, playerDatabase);

            for(ProxiedPlayer player : inChannel) {
                if(sent.contains(player)) continue;

                ClickEvent clickEvent = new ClickEvent();
                clickEvent.setAction(ClickAction.RUN_COMMAND);
                clickEvent.setValue("/cc:playermenu " + event.getPlayer().getName());

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(message).addEvent("playerMenu", clickEvent);

                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, sender));
                sent.add(player);
            }
        }

        sent.clear();
    }
}
