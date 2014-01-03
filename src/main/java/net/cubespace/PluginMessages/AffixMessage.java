package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class AffixMessage extends StandardPacket {
    private String prefix;
    private String suffix;

    public AffixMessage() {}

    public AffixMessage(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.prefix = dataInputStream.readUTF();
        this.suffix = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(prefix);
        packetWriter.writeUTF(suffix);
        return packetWriter;
    }
}
