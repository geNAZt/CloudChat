package net.cubespace.CloudChat.Module.PM.Listener;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 03.01.14 21:46
 */
public class PMListener implements Listener {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public PMListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPM(PMEvent event) {
        final Messages messages = plugin.getConfigManager().getConfig("messages");

        final ProxiedPlayer sen = plugin.getProxy().getPlayer(event.getFrom());
        final ProxiedPlayer rec = plugin.getProxy().getPlayer(event.getTo());

        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(event.getMessage());
        final String message = legacyMessageBuilder.getString();

        //Get Sender and Receiver Databases
        PlayerDatabase senderDB = playerManager.get(sen.getName());
        PlayerDatabase receiverDB = playerManager.get(rec.getName());

        final String sender = MessageFormat.format(messages.Message_Nick, null, senderDB);
        final String receiver = MessageFormat.format(messages.Message_Nick, null, receiverDB);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Message_Receiver.replace("%sender", sender).replace("%message", message))).send(rec);

        MessageBuilder messageBuilder2 = new MessageBuilder();
        messageBuilder2.setText(FontFormat.translateString(messages.Message_Sender.replace("%receiver", receiver).replace("%message", message))).send(sen);

        plugin.getPluginLogger().info(sen.getName() + " -> " + rec.getName() + ": " + event.getMessage());

        playerManager.get(sen.getName()).Reply = rec.getName();
        playerManager.get(rec.getName()).Reply = sen.getName();

        //Let people spy
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                MessageBuilder messageBuilder2 = new MessageBuilder();
                messageBuilder2.setText(FontFormat.translateString(messages.Message_Spy.
                        replace("%receiver", receiver).
                        replace("%sender", sender).
                        replace("%message", message)));


                for (Map.Entry<String, PlayerDatabase> playerDatabase : new HashMap<>(playerManager.getLoadedPlayers()).entrySet()) {
                    if (playerDatabase.getValue().Spy) {
                        messageBuilder2.send(plugin.getProxy().getPlayer(playerDatabase.getKey()));
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, canVeto = true)
    public boolean onPM2(PMEvent event) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        String player = event.getTo();

        ProxiedPlayer sender = plugin.getProxy().getPlayer(event.getFrom());
        if (sender == null) {
            return true;
        }

        ProxiedPlayer rec = NicknameParser.getPlayer(plugin, player);
        if (rec == null) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(FontFormat.translateString(messages.Message_OfflinePlayer)).send(sender);
            return true;
        }

        if (sender.equals(rec)) {
            plugin.getPluginLogger().debug("Sender and Receiver are equal.");
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(FontFormat.translateString(messages.Message_Self)).send(sender);
            return true;
        }

        event.setTo(rec.getName());

        return false;
    }
}
