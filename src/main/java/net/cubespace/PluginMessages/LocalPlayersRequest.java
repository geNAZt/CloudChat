package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import java.io.DataInputStream;
import java.io.IOException;

public class LocalPlayersRequest extends StandardPacket {
    private String message;
    private Integer range;

    public LocalPlayersRequest() {}

    public LocalPlayersRequest(String message, Integer range) {
        this.message = message;
        this.range = range;
    }

    public Integer getRange() {
        return range;
    }
    
    public String getMessage() {
        return message;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
        this.range = dataInputStream.readInt();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(message);
        packetWriter.writeInt(range);
        
        return packetWriter;
    }
}
