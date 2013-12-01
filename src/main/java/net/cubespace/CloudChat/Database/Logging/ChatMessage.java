package net.cubespace.CloudChat.Database.Logging;

import com.j256.ormlite.field.DatabaseField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Fabian on 01.12.13.
 */
@Entity()
public class ChatMessage {
    public enum SourceType {
        PLAYER,
        IRC
    }

    @DatabaseField(generatedId = true)
    private Integer id;
    @Column
    private String nick;
    @Column
    private String channel;
    @Column
    private String message;
    @DatabaseField
    private Date date;
    @DatabaseField(unknownEnumName = "PLAYER")
    private SourceType source;
    @DatabaseField(canBeNull = true)
    private String ircChannel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SourceType getSource() {
        return source;
    }

    public void setSource(SourceType source) {
        this.source = source;
    }

    public String getIrcChannel() {
        return ircChannel;
    }

    public void setIrcChannel(String ircChannel) {
        this.ircChannel = ircChannel;
    }
}
