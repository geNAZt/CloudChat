package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:05
 */
public class DispatchScmdMessage extends StandardPacket {
    private String command;
    private Integer scmdSessionId;

    public DispatchScmdMessage() {

    }

    public DispatchScmdMessage(String command, Integer scmdSessionId) {
        this.command = command;
        this.scmdSessionId = scmdSessionId;
    }

    public String getCommand() {
        return command;
    }

    public Integer getScmdSessionId() {
        return scmdSessionId;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.command = dataInputStream.readUTF();
        this.scmdSessionId = dataInputStream.readInt();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(command);
        packetWriter.writeInt(scmdSessionId);
        return packetWriter;
    }
}
