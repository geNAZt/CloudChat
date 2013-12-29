package net.cubespace.CloudChat.Module.Spam.Message;

import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.PluginMessage.IPluginMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 13:05
 */
public class DispatchCmdMessage implements IPluginMessage{
    private ProxiedPlayer player;
    private String command;

    public DispatchCmdMessage(ProxiedPlayer player, String command) {
        this.player = player;
        this.command = command;
    }

    @Override
    public byte[] send(CubespacePlugin plugin) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bStream);

        try {
            output.writeUTF("DispatchCmd");
            output.writeUTF(command);

            return bStream.toByteArray();
        } catch (IOException e) {
            plugin.getPluginLogger().error("Could not write DispatchCmd Message", e);
            throw new RuntimeException();
        }
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return this.player;
    }
}
