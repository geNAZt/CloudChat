package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.IRC.Format.MCToIrcFormat;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 04.01.14 12:49
 */
public class PMSession {
    private String from;
    private String to = "";
    private IRCModule ircModule;

    public PMSession(IRCModule ircModule, String from) {
        this.from = from;
        this.ircModule = ircModule;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        if(!this.to.equals(to)) {
            Messages messages = ircModule.getPlugin().getConfigManager().getConfig("messages");
            ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_PmSessionChangedTo.replace("%from", from).replace("%to", to)), from);
        }

        this.to = to;
    }
}
