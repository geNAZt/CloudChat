package net.cubespace.CloudChat.Module.ColorHandler.Listener;

import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerNickchangeListener implements Listener {
    private final CubespacePlugin plugin;

    public PlayerNickchangeListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerNickchange(PlayerNickchangeEvent event) {
        if(!plugin.getPermissionManager().has(event.getSender(), "cloudchat.use.color") && !plugin.getPermissionManager().has(event.getSender(), "cloudchat.use.color.nick")) {
            event.setNewNick(FontFormat.stripColor(event.getNewNick()));
            plugin.getPluginLogger().debug("Stripped Colors away");
        }
    }
}
