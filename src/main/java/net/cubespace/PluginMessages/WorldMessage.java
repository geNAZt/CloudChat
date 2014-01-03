package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class WorldMessage extends StandardPacket {
    private String name;
    private String alias;

    public WorldMessage() {}

    public WorldMessage(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.name = dataInputStream.readUTF();
        this.alias = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(name);
        packetWriter.writeUTF(alias);
        return packetWriter;
    }
}
