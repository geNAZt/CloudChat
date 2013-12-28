package net.cubespace.CloudChat.Module.ChatHandler.Sender;

import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 16:00
 */
public interface ISender {
    public String getNick();
    public ChannelDatabase getChannel();
    public PlayerDatabase getPlayerDatabase();
}
