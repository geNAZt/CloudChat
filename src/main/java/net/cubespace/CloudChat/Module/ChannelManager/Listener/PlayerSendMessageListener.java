package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerSendMessageListener implements Listener {
    private final CubespacePlugin plugin;

    public PlayerSendMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, canVeto = true)
    public boolean onPlayerSendMessage(PlayerSendMessageEvent event) {
        return event.getPlayer() == null || (((Main) plugin.getConfigManager().getConfig("main")).DontHandleForServers.contains(event.getPlayer().getServer().getInfo().getName()));
    }
}
