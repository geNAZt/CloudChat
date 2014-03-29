package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CustomFormatMessage extends StandardPacket {
    private String format;
    private String value;

    public CustomFormatMessage() {}

    public CustomFormatMessage(String format, String value) {
        this.format = format;
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void handle(DataInputStream inputStream) throws IOException {
        format = inputStream.readUTF();
        value = inputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(format);
        packetWriter.writeUTF(value);
        return packetWriter;
    }
}
