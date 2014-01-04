package net.cubespace.CloudChat.Module.IRC;

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
            ircModule.getIrcBot().sendToChannel("!!! "+ from +" you are now talking with " + to + " !!!", from);
        }

        this.to = to;
    }
}
