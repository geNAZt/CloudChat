package net.cubespace.CloudChat.Module.PM.Listener;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PM.CancelReason;
import net.cubespace.CloudChat.Module.PM.Event.ConversationAcceptEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationCancelEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationMessageEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationRequestEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ConversationListener implements Listener {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public ConversationListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH, canVeto = true)
    public boolean onConversationRequest(ConversationRequestEvent event) {
        ProxiedPlayer target = NicknameParser.getPlayer(plugin, event.getTo());
        ProxiedPlayer sender = plugin.getProxy().getPlayer(event.getFrom());

        Messages messages = plugin.getConfigManager().getConfig("messages");

        if (target == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NoTarget)).send(sender);

            return true;
        }

        if (sender == null) {
            return true;
        }

        boolean sameUser = sender.equals(target);

        if (sameUser) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_SamePlayer)).send(sender);
        }

        return sameUser;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onConversationRequest2(final ConversationRequestEvent event) {
        ProxiedPlayer target = NicknameParser.getPlayer(plugin, event.getTo());
        ProxiedPlayer sender = plugin.getProxy().getPlayer(event.getFrom());

        Messages messages = plugin.getConfigManager().getConfig("messages");
        Main config = plugin.getConfigManager().getConfig("main");

        if (target == null) {
            if (sender != null) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_TargetIsOffline)).send(sender);
            }

            return;
        }

        final PlayerDatabase playerDatabase = playerManager.get(target.getName());
        playerDatabase.Conversation_Request = sender.getName();

        final PlayerDatabase playerDatabase1 = playerManager.get(sender.getName());
        playerDatabase1.Conversation_Request = target.getName();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_GotRequest.replace("%sender", sender.getDisplayName()))).send(target);
        messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_SendRequest.replace("%target", target.getDisplayName()))).send(sender);

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if (playerDatabase.Conversation_Request.equals("") || !playerDatabase.Conversation_Request.equals(playerDatabase1.Conversation_Request)) return;

                ProxiedPlayer target = NicknameParser.getPlayer(plugin, event.getTo());
                ProxiedPlayer sender = plugin.getProxy().getPlayer(event.getFrom());

                if (target == null) {
                    ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(event.getFrom(), event.getTo(), CancelReason.TIMEOUT);
                    plugin.getAsyncEventBus().callEvent(conversationCancelEvent);
                    playerDatabase.Conversation_Request = "";
                    playerDatabase1.Conversation_Request = "";

                    return;
                }

                if (sender == null) {
                    ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(event.getTo(), event.getFrom(), CancelReason.TIMEOUT);
                    plugin.getAsyncEventBus().callEvent(conversationCancelEvent);

                    playerDatabase1.Conversation_Request = "";
                    playerDatabase.Conversation_Request = "";

                    return;
                }

                PlayerDatabase playerDatabase = playerManager.get(target.getName());

                if (!playerDatabase.Conversation_Current.equals(sender.getName())) {
                    ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(event.getFrom(), event.getTo(), CancelReason.TIMEOUT);
                    plugin.getAsyncEventBus().callEvent(conversationCancelEvent);

                    playerDatabase.Conversation_Request = "";
                    playerDatabase1.Conversation_Request = "";
                }
            }
        }, config.ConversationTimeout, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onConversationCancel(ConversationCancelEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getFrom());
        ProxiedPlayer target = plugin.getProxy().getPlayer(event.getTo());

        Messages messages = plugin.getConfigManager().getConfig("messages");

        if (player != null) {
            MessageBuilder messageBuilder = new MessageBuilder();

            switch (event.getCancelReason()) {
                case TIMEOUT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_RequestTimedOut)).send(player);

                    break;

                case TARGET_LEFT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_TargetLeft)).send(player);

                    break;

                case SENDER_LEFT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_SenderLeft)).send(player);

                    break;

                case PLAYER_CANCEL:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_PlayerCanceled)).send(player);

                    break;
            }
        }

        if (target != null) {
            MessageBuilder messageBuilder = new MessageBuilder();

            switch (event.getCancelReason()) {
                case TIMEOUT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_RequestTimedOut)).send(target);

                    break;

                case TARGET_LEFT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_TargetLeft)).send(target);

                    break;

                case SENDER_LEFT:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_SenderLeft)).send(target);

                    break;

                case PLAYER_CANCEL:
                    messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_PlayerCanceled)).send(target);

                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onConversationAccept(ConversationAcceptEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getFrom());
        ProxiedPlayer target = plugin.getProxy().getPlayer(event.getTo());

        Messages messages = plugin.getConfigManager().getConfig("messages");

        if (target != null) {
            PlayerDatabase playerDatabase = playerManager.get(target.getName());
            playerDatabase.Conversation_Current = playerDatabase.Conversation_Request;
            playerDatabase.Conversation_Request = "";

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_AcceptedTarget.replace("%target", playerDatabase.Conversation_Current))).send(target);
        }

        if (player != null) {
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            playerDatabase.Conversation_Current = playerDatabase.Conversation_Request;
            playerDatabase.Conversation_Request = "";

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_AcceptedSender)).send(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onConversationMessage(ConversationMessageEvent event) {
        PlayerDatabase sender = playerManager.get(event.getFrom());

        ProxiedPlayer target = plugin.getProxy().getPlayer(event.getTo());
        if (target == null) return;

        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(event.getMessage());

        Messages messages = plugin.getConfigManager().getConfig("messages");

        String message = MessageFormat.format(messages.Conversation_Format.replace("%message", legacyMessageBuilder.getString()), null, sender);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(message).send(target);
        messageBuilder.send(plugin.getProxy().getPlayer(event.getFrom()));
    }
}
