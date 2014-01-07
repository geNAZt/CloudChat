package net.cubespace.PluginMessages;

import com.iKeirNez.PluginMessageApiPlus.PacketWriter;
import com.iKeirNez.PluginMessageApiPlus.StandardPacket;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 07.01.14 21:06
 */
public class PermissionResponse extends StandardPacket {
    private String permission;
    private int mode;

    public PermissionResponse() {}

    public PermissionResponse(String permission, int mode) {
        this.permission = permission;
        this.mode = mode;
    }

    public String getPermission() {
        return permission;
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void handle(DataInputStream dataInputStream) throws IOException {
        permission = dataInputStream.readUTF();
        mode = dataInputStream.readInt();
    }

    @Override
    protected PacketWriter write() throws IOException {
        PacketWriter packetWriter = new PacketWriter(this);
        packetWriter.writeUTF(permission);
        packetWriter.writeInt(mode);

        return packetWriter;
    }
}
