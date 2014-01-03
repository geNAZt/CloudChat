package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:05
 */
public class DispatchCmdMessage extends StandardPacket {
    private String command;

    public DispatchCmdMessage() {

    }

    public DispatchCmdMessage(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.command = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(command);
        return packetWriter;
    }
}
