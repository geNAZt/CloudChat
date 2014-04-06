package net.cubespace.CloudChat.Module.Spam.Listener;

import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.Spam.SpamModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerJoinListener implements Listener {
    private final SpamModule spamModule;

    public PlayerJoinListener(SpamModule spamModule) {
        this.spamModule = spamModule;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        spamModule.getSpamManager().prepareSpamEntry(event.getPlayer());
    }
}
