package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.SetNickMessage;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;

    public PlayerJoinListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Load the Player
        if(!playerManager.isLoaded(event.getPlayer().getName())) {
            playerManager.load(event.getPlayer().getName());
        }

        event.getPlayer().setDisplayName(FontFormat.translateString(playerManager.get(event.getPlayer().getName()).Nick));
        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(event.getPlayer(), new SetNickMessage(FontFormat.translateString(playerManager.get(event.getPlayer().getName()).Nick)));
    }
}
