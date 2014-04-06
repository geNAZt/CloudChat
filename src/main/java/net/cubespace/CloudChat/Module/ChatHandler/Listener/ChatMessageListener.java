package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.Chat.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
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
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;

    public ChatMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(final ChatMessageEvent event) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        ProxiedPlayer sendPlayer = plugin.getProxy().getPlayer(event.getSender().getPlayerDatabase().Realname);

        if (sendPlayer != null && plugin.getPermissionManager().has(sendPlayer, "cloudchat.cannot.write." + event.getSender().getChannel().Name)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Channels_PlayerCantWrite)).send(sendPlayer);

            return;
        }

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + event.getSender().getNick());

        ClickEvent clickEvent1 = new ClickEvent();
        clickEvent1.setAction(ClickAction.RUN_COMMAND);
        clickEvent1.setValue("/focus " + event.getSender().getChannel().Name);

        final List<String> receiptens = event.getReceiptens();
        if (receiptens.size() == 0) {
            return;
        }

        final MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);
        messageBuilder.setText(event.getMessage());

        if (receiptens.get(0).equals("§ALL")) {
            for(ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender(), event.getMessage()));
            }
        } else {
            for(String playerName : receiptens) {
                ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);

                if (player == null) {
                    continue;
                }

                plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender(), event.getMessage()));
            }
        }

        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(event.getMessage());
        plugin.getPluginLogger().info(legacyMessageBuilder.getString());
    }
}
