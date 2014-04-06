package net.cubespace.CloudChat.Module.Mail.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private final PlayerManager playerManager;
    private final CubespacePlugin plugin;

    public PlayerJoinListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());

        if(playerDatabase.Mails.size() > 0) {
            Messages messages = plugin.getConfigManager().getConfig("messages");
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(messages.Mail_JoinMessage.replace("%size", String.valueOf(playerDatabase.Mails.size()))).send(event.getPlayer());
        }
    }
}
