package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class IgnoreMessage extends StandardPacket {
    private Boolean ignore;

    public IgnoreMessage() {}

    public IgnoreMessage(Boolean ignore) {
        this.ignore = ignore;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.ignore = dataInputStream.readBoolean();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeBoolean(ignore);
        return packetWriter;
    }
}
