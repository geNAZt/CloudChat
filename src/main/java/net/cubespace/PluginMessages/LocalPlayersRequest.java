package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import java.io.DataInputStream;
import java.io.IOException;

public class LocalPlayersRequest extends StandardPacket {
    private String message;
    private String channel;
    private Integer range;

    public LocalPlayersRequest() {}

    public LocalPlayersRequest(String message, String channel, Integer range) {
        this.message = message;
        this.channel = channel;
        this.range = range;
    }

    public Integer getRange() {
        return range;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getChannel()
    {
        return channel;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
        this.channel = dataInputStream.readUTF();
        this.range = dataInputStream.readInt();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(message);
        packetWriter.writeUTF(channel);
        packetWriter.writeInt(range);
        
        return packetWriter;
    }
}
