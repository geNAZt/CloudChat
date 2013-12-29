package net.cubespace.CloudChat.Module.Spam;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 01:06
 */
public class SpamCounter {
    private Integer messageCount = 0;

    public Integer getMessageCount() {
        return messageCount;
    }

    public synchronized void addOne() {
        messageCount++;
    }

    public synchronized void removeOne() {
        messageCount--;
    }
}
