package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:39
 */
public class PlayerNickchangeListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;

    public PlayerNickchangeListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNickchangeEvent(PlayerNickchangeEvent event) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().info(event.getSender().getName() + " changed its nick to " + event.getNewNick());
        playerManager.get(event.getSender().getName()).Nick = event.getNewNick();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_ChangedNick.replace("%nick", event.getNewNick()))).send(event.getSender());
    }
}
