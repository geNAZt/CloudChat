package net.cubespace.CloudChat.Module.Metrics.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener {
    private static Integer ircMessageCount = 0;
    private static Integer ingameMessageCount = 0;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event) {
        if(event.getSender().getNick().equals("IRC")) {
            ircMessageCount++;
        } else {
            ingameMessageCount++;
        }
    }

    public static Integer getIrcMessageCount() {
        Integer tempIrc = ircMessageCount;
        ircMessageCount = 0;
        return tempIrc;
    }

    public static Integer getIngameMessageCount() {
        Integer tempIngame = ingameMessageCount;
        ingameMessageCount = 0;
        return tempIngame;
    }
}
