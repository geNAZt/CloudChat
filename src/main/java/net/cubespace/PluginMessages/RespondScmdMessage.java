package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:05
 */
public class RespondScmdMessage extends StandardPacket {
    private String response;
    private Integer scmdSessionId;

    public RespondScmdMessage() {

    }

    public RespondScmdMessage(String response, Integer scmdSessionId) {
        this.response = response;
        this.scmdSessionId = scmdSessionId;
    }

    public String getResponse() {
        return response;
    }

    public Integer getId() {
        return scmdSessionId;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.response = dataInputStream.readUTF();
        this.scmdSessionId = dataInputStream.readInt();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(response);
        packetWriter.writeInt(scmdSessionId);
        return packetWriter;
    }
}
