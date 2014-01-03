package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;
import org.apache.commons.lang.StringUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FactionChatMessage extends StandardPacket {
    private String message;
    private String mode;
    private List<String> players;
    private String factionName;

    public FactionChatMessage() {}

    public FactionChatMessage(String mode, String message, List<String> players, String factionName) {
        this.message = message;
        this.mode = mode;
        this.players = players;
        this.factionName = factionName;
    }

    public String getMessage() {
        return message;
    }

    public String getMode() {
        return mode;
    }

    public String getFactionName() {
        return factionName;
    }

    public List<String> getPlayers() {
        return players;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.mode = dataInputStream.readUTF();
        this.message = dataInputStream.readUTF();

        String players = dataInputStream.readUTF();
        if(players.equals("null")) {
            this.players = null;
        } else {
            String[] playerSplit = players.split("§");
            this.players = new ArrayList<String>();

            for(String playerName : playerSplit) {
                this.players.add(playerName);
            }
        }

        this.factionName = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(mode);
        packetWriter.writeUTF(message);

        if(players == null) {
            packetWriter.writeUTF("null");
        } else {
            packetWriter.writeUTF(StringUtils.join(players, "§"));
        }

        packetWriter.writeUTF(factionName);

        return packetWriter;
    }
}
