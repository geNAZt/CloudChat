package net.cubespace.CloudChat.Module.FormatHandler.Listener;

import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;

    public ChatMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatMessage(final ChatMessageEvent event) {
        final LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(event.getMessage());

        String message;
        if (event.getSender().getChannel().GroupFormats.containsKey(event.getSender().getPlayerDatabase().Group)) {
            message = event.getSender().getChannel().GroupFormats.get(event.getSender().getPlayerDatabase().Group).replace("%message", legacyMessageBuilder.getString());
        } else {
            message = event.getSender().getChannel().Format.replace("%message", legacyMessageBuilder.getString());
        }

        //Let people spy
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                ClickEvent clickEvent = new ClickEvent();
                clickEvent.setAction(ClickAction.RUN_COMMAND);
                clickEvent.setValue("/cc:playermenu " + event.getSender().getNick());

                ClickEvent clickEvent1 = new ClickEvent();
                clickEvent1.setAction(ClickAction.RUN_COMMAND);
                clickEvent1.setValue("/focus " + event.getSender().getChannel().Name);

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);
                messageBuilder.setText(MessageFormat.format(event.getSender().getChannel().Format.replace("%message", legacyMessageBuilder.getString()), event.getSender().getChannel(), event.getSender().getPlayerDatabase()));

                for (Map.Entry<String, PlayerDatabase> playerDatabase : new HashMap<>(playerManager.getLoadedPlayers()).entrySet()) {
                    if (playerDatabase.getValue().ChatSpy && !channelManager.getAllInChannel(event.getSender().getChannel()).contains(plugin.getProxy().getPlayer(playerDatabase.getValue().Realname))) {
                        plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(plugin.getProxy().getPlayer(playerDatabase.getValue().Realname), messageBuilder, event.getSender()));
                    }
                }
            }
        });

        event.setMessage(MessageFormat.format(message, event.getSender().getChannel(), event.getSender().getPlayerDatabase()));
        plugin.getPluginLogger().debug("Formatted message");
    }
}
