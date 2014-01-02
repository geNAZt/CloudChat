package net.cubespace.CloudChat.Module.ChatHandler.Message;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:07
 */
public class FactionChatMessage extends StandardPacket {
    private String message;

    public FactionChatMessage() {}

    public FactionChatMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(message);
        return packetWriter;
    }
}
