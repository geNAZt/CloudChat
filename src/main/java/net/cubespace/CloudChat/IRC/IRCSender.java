package net.cubespace.CloudChat.IRC;

/**
 * Created by Fabian on 29.11.13.
 */
public class IRCSender {
    private String nick;
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
