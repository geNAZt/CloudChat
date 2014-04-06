package net.cubespace.CloudChat.Module.ChatHandler;

import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatBuffer {
    private final HashMap<String, ArrayList<MessageBuilder>> chatBuffer = new HashMap<>();

    public void addToBuffer(String buffer, MessageBuilder message) {
        if(!chatBuffer.containsKey(buffer)) {
            chatBuffer.put(buffer, new ArrayList<MessageBuilder>(50));
        }

        if(chatBuffer.get(buffer).size() > 50) {
            chatBuffer.get(buffer).remove(chatBuffer.get(buffer).get(0));
        }

        chatBuffer.get(buffer).add(message);
    }

    public ArrayList<MessageBuilder> getBuffer(String buffer) {
        return chatBuffer.get(buffer);
    }

    public void removeBuffer(String buffer) {
        chatBuffer.remove(buffer);
    }
}
