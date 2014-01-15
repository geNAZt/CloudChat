package net.cubespace.CloudChat.Module.Metrics.Listener;

import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMListener implements Listener {
    private static Integer amountOfPms = 0;

    public static int getPMs() {
        Integer tempPMs = new Integer(amountOfPms.intValue());
        amountOfPms = 0;
        return tempPMs;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPM(PMEvent event) {
        amountOfPms++;
    }


}
