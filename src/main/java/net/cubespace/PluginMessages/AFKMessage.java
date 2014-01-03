package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class AFKMessage extends StandardPacket {
    private boolean afk;

    public AFKMessage() {}

    public AFKMessage(boolean afk) {
        this.afk = afk;
    }

    public boolean isAfk() {
        return afk;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.afk = dataInputStream.readBoolean();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeBoolean(afk);
        return packetWriter;
    }
}
