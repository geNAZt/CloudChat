package net.cubespace.CloudChat.Module.PlayerManager.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.CommandSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerNickchangeEvent implements Event {
    private final CommandSender sender;
    private final String oldNick;
    private String newNick;

    public PlayerNickchangeEvent(CommandSender sender, String oldNick, String newNick) {
        this.sender = sender;
        this.oldNick = oldNick;
        this.newNick = newNick;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getOldNick() {
        return oldNick;
    }

    public String getNewNick() {
        return newNick;
    }

    public void setNewNick(String newNick) {
        this.newNick = newNick;
    }
}
