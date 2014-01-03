package net.cubespace.CloudChat.Module.Logging.Entity;

import com.j256.ormlite.field.DatabaseField;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity()
public class PrivateMessage {
    @DatabaseField(generatedId = true)
    private Integer id;
    @Column
    private String from;
    @Column
    private String to;
    @DatabaseField(canBeNull = true)
    private String message;
    @DatabaseField
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
