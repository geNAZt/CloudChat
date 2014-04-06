package net.cubespace.CloudChat.Module.Twitter;

import java.util.Date;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class TwitterManager {
    private final HashMap<String, Date> lastTweet = new HashMap<>();

    public boolean isRegistered(String user) {
        return lastTweet.containsKey(user);
    }

    public void updateLastTweet(String user, Date date) {
        lastTweet.put(user, date);
    }

    public Date getLastTweet(String user) {
        return lastTweet.get(user);
    }
}
