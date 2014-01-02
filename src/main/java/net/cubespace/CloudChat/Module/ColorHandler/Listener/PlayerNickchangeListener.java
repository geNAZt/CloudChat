package net.cubespace.CloudChat.Module.ColorHandler.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:47
 */
public class PlayerNickchangeListener implements Listener {
    private CloudChatPlugin plugin;

    public PlayerNickchangeListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerNickchange(PlayerNickchangeEvent event) {
        if(!event.getSender().hasPermission("cloudchat.use.color") && !event.getSender().hasPermission("cloudchat.use.color.nick")) {
            event.setNewNick(FontFormat.stripColor(event.getNewNick()));
            plugin.getPluginLogger().debug("Stripped Colors away");
        }
    }
}
