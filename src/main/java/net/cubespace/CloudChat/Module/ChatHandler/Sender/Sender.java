package net.cubespace.CloudChat.Module.ChatHandler.Sender;

import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Sender implements ISender {
    private String nick;
    private ChannelDatabase channel;
    private PlayerDatabase playerDatabase;

    public Sender(String nick, ChannelDatabase channel, PlayerDatabase playerDatabase) {
        this.nick = nick;
        this.channel = channel;
        this.playerDatabase = playerDatabase;
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public ChannelDatabase getChannel() {
        return channel;
    }

    @Override
    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }
}
