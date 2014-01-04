package net.cubespace.CloudChat.Module.IRC;

public class IRCSender {
    private String nick;
    private String channel;
    private String rawNick;

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

    public String getRawNick() {
        return rawNick;
    }

    public void setRawNick(String rawNick) {
        this.rawNick = rawNick;
    }
}
