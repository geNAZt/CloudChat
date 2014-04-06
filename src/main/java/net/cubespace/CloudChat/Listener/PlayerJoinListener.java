package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private final PlayerManager playerManager;

    public PlayerJoinListener(CloudChatPlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PostLoginEvent event) {
        playerManager.load(event.getPlayer());
        playerManager.get(event.getPlayer().getName()).Realname = event.getPlayer().getName();
    }
}
