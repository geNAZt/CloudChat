package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlaySound extends StandardPacket {
    public String sound;

    public PlaySound() { }

    public PlaySound(String sound) {
        this.sound = sound;
    }

    @Override
    protected void handle(DataInputStream inputStream) throws IOException {
        sound = inputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(sound);
        return packetWriter;
    }
}
