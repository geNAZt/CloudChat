package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.SetNickMessage;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerNickchangeListener implements Listener {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;

    public PlayerNickchangeListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNickchangeEvent(PlayerNickchangeEvent event) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().info(event.getSender().getName() + " changed its nick to " + event.getNewNick());
        playerManager.get(event.getSender().getName()).Nick = event.getNewNick();
        ((ProxiedPlayer) event.getSender()).setDisplayName(FontFormat.translateString(event.getNewNick()));

        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(((ProxiedPlayer) event.getSender()), new SetNickMessage(FontFormat.translateString(event.getNewNick())));

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_ChangedNick.replace("%nick", event.getNewNick()))).send(event.getSender());
    }
}
