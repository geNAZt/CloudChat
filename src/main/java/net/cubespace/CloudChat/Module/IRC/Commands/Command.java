package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.Module.IRC.IRCSender;

public interface Command {
    @SuppressWarnings("SameReturnValue")
    public boolean execute(IRCSender sender, String[] args);
}
