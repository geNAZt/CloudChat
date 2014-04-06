package net.cubespace.CloudChat.Module.IRC;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ScmdSession {
    private String nickname;
    private String channel;
    private final Integer id;
    private final String command;
    private Long lastResponse;

    public ScmdSession(String nickname, String channel, Integer id, String command) {
        this.nickname = nickname;
        this.channel = channel;
        this.id = id;
        this.command = command;
        this.lastResponse = System.currentTimeMillis();
    }

    public String getNickname() {
        return nickname;
    }

    public String getChannel() {
        return channel;
    }

    public Integer getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public void newResponse() {
        this.lastResponse = System.currentTimeMillis();
    }

    public Long getLastResponse() {
        return lastResponse;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
