package net.cubespace.CloudChat.Module.Spam.Listener;

import net.cubespace.CloudChat.Event.PlayerQuitEvent;
import net.cubespace.CloudChat.Module.Spam.SpamModule;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerQuitListener implements Listener {
    private final SpamModule spamModule;

    public PlayerQuitListener(SpamModule spamModule) {
        this.spamModule = spamModule;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        spamModule.getSpamManager().removeSpamEntries(event.getPlayer());
    }
}
