package net.cubespace.CloudChat.Module.Logging.Listener;

import net.cubespace.CloudChat.Module.Logging.Entity.PrivateMessage;
import net.cubespace.CloudChat.Module.Logging.LoggingModule;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.Date;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMListener implements Listener {
    private final LoggingModule loggingModule;

    public PMListener(LoggingModule module) {
        this.loggingModule = module;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreVeto = true)
    public void onPM(PMEvent event) {
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessage(event.getMessage());
        privateMessage.setDate(new Date());
        privateMessage.setFrom(event.getFrom());
        privateMessage.setTo(event.getTo());

        loggingModule.getAsyncDatabaseLogger().addPrivateMessage(privateMessage);
    }
}
