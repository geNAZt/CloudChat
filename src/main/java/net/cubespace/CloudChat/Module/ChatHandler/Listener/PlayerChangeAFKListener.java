package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerChangeAFKEvent;
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
public class PlayerChangeAFKListener implements Listener {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;

    public PlayerChangeAFKListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAFKChange(PlayerChangeAFKEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        ArrayList<ProxiedPlayer> sent = new ArrayList<>();

        for(ChannelDatabase channel : channelManager.getAllJoinedChannels(event.getPlayer())) {
            ArrayList<ProxiedPlayer> inChannel = channelManager.getAllInChannel(channel);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setAction(ClickAction.RUN_COMMAND);
            clickEvent.setValue("/cc:playermenu " + event.getPlayer().getName());

            ClickEvent clickEvent1 = new ClickEvent();
            clickEvent1.setAction(ClickAction.RUN_COMMAND);
            clickEvent1.setValue("/focus " + channel.Name);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);

            String message;
            if(event.isAfk()) {
                message = MessageFormat.format(((Messages) plugin.getConfigManager().getConfig("messages")).PlayerGotAfk, channel, playerDatabase);
            } else {
                message = MessageFormat.format(((Messages) plugin.getConfigManager().getConfig("messages")).PlayerGotOutOfAfk, channel, playerDatabase);
            }

            messageBuilder.setText(message);

            Sender sender = new Sender(event.getPlayer().getName(), channel, playerDatabase);

            for(ProxiedPlayer player : inChannel) {
                if(sent.contains(player)) continue;

                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, sender, message));
                sent.add(player);
            }
        }

        sent.clear();
    }
}
