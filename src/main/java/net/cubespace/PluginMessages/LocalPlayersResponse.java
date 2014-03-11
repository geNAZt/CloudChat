package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LocalPlayersResponse extends StandardPacket {
    private String message;
    private String channel;
    private List<String> to;

    public LocalPlayersResponse() {}

    public LocalPlayersResponse(String message, String channel, List<String> to) {
        this.message = message;
        this.channel = channel;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }
    
    public String getChannel() {
        return channel;
    }

    public List<String> getTo() {
        return to;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.message = dataInputStream.readUTF();
        this.channel = dataInputStream.readUTF();
        this.to = Arrays.asList(dataInputStream.readUTF().split("§"));
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(message);
        packetWriter.writeUTF(channel);

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
