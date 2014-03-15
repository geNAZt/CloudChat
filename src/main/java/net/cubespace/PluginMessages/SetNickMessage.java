package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class SetNickMessage extends StandardPacket {
    private String nick;

    public SetNickMessage() {}

    public SetNickMessage(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.nick = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(nick);
        return packetWriter;
    }
}
