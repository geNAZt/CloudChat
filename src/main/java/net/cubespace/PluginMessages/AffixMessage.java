package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class AffixMessage extends StandardPacket {
    private String prefix;
    private String suffix;
    private String town;
    private String nation;
    private String faction;
    private String group;

    public AffixMessage() {}

    public AffixMessage(String prefix, String suffix, String town, String nation, String faction, String group) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.town = town;
        this.nation = nation;
        this.faction = faction;
        this.group = group;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTown() {
        return town;
    }

    public String getNation() {
        return nation;
    }

    public String getFaction() {
        return faction;
    }

    public String getGroup() {
        return group;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        this.prefix = dataInputStream.readUTF();
        this.suffix = dataInputStream.readUTF();
        this.town = dataInputStream.readUTF();
        this.nation = dataInputStream.readUTF();
        this.faction = dataInputStream.readUTF();
        this.group = dataInputStream.readUTF();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(prefix);
        packetWriter.writeUTF(suffix);
        packetWriter.writeUTF(town);
        packetWriter.writeUTF(nation);
        packetWriter.writeUTF(faction);
        packetWriter.writeUTF(group);
        return packetWriter;
    }
}
