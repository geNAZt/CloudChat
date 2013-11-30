package net.cubespace.CloudChat.IRC.Commands;

import net.cubespace.CloudChat.IRC.IRCSender;

/**
 * Created by Fabian on 29.11.13.
 */
public interface Command {
    public boolean execute(IRCSender sender, String[] args);
}
