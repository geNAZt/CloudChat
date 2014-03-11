package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener {
    private CubespacePlugin plugin;
    private ChannelManager channelManager;

    public ChatMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event) {
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + event.getSender().getNick());

        ClickEvent clickEvent1 = new ClickEvent();
        clickEvent1.setAction(ClickAction.RUN_COMMAND);
        clickEvent1.setValue("/focus " + event.getSender().getChannel().Name);

        List<String> receiptens = event.getReceiptens();
        if (receiptens.size() == 0) {
            return;
        }

        if (receiptens.get(0).equals("§ALL")) {
            for(ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);
                messageBuilder.setText(event.getMessage());
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender()));
            }
        } else {
            for(String playerName : receiptens) {
                ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);

                if (player == null) {
                    continue;
                }

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);
                messageBuilder.setText(event.getMessage());

                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender()));
            }
        }

        plugin.getPluginLogger().info(event.getMessage());
    }
}
