package net.cubespace.CloudChat.Module.IRC;

import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 13.12.13 21:22
 */
public class IRCManager {
    //Handle the Nicks the Bot can see
    private ArrayListMultimap<String, String> nickJoinedChannels = ArrayListMultimap.create();
    //Handle the Bots joined Channels
    private ArrayList<String> joinedChannels = new ArrayList<>();

    public boolean hasJoined(String channel) {
        return joinedChannels.contains(channel);
    }

    public void addJoinedChannel(String channel) {
        joinedChannels.add(channel);
    }

    public void removeBotJoinedChannel(String channel) {
        joinedChannels.remove(channel);
    }

    public ArrayList<String> getJoinedChannels() {
        return joinedChannels;
    }

    public List<String> getJoinedChannels(String nick) {
        return nickJoinedChannels.get(nick);
    }

    public void addJoinedChannel(String nick, String channel) {
        nickJoinedChannels.put(nick, channel);
    }

    public void removeJoinedChannels(String nick) {
        nickJoinedChannels.removeAll(nick);
    }

    public void removeValuesFromNickJoinedChannels(String channel) {
        for(String nick : nickJoinedChannels.keySet())
            nickJoinedChannels.remove(nick, channel);
    }
}
