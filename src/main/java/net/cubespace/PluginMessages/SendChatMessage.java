package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SendChatMessage extends StandardPacket {
    private String message;
    private List<String> to;

    public SendChatMessage() {}

    public SendChatMessage(String message, List<String> to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getTo() {
        return to;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
        this.to = Arrays.asList(dataInputStream.readUTF().split("§"));
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(message);

        StringBuilder sb = new StringBuilder();
        Iterator<String> stringIterator = to.iterator();
        while(stringIterator.hasNext()) {
            sb.append(stringIterator.next());

            if(stringIterator.hasNext()) {
                sb.append("§");
            }
        }

        packetWriter.writeUTF(sb.toString());
        return packetWriter;
    }
}
