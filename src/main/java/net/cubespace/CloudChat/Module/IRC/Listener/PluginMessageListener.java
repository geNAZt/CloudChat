package net.cubespace.CloudChat.Module.IRC.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.ScmdSession;
import net.cubespace.PluginMessages.RespondScmdMessage;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:09
 */
public class PluginMessageListener implements PacketListener {
    private IRCModule ircModule;
    private CloudChatPlugin plugin;

    public PluginMessageListener(IRCModule ircModule, CloudChatPlugin plugin) {
        this.ircModule = ircModule;
        this.plugin = plugin;
    }

    @PacketHandler
    public void onRespondScmd(RespondScmdMessage respondScmdMessage) {
        //Get the SCMD Session
        ScmdSession scmdSession = ircModule.getIrcBot().getIrcManager().getScmdSession(respondScmdMessage.getId());
        if(scmdSession == null) return;

        scmdSession.newResponse();

        ircModule.getIrcBot().sendToChannel("[SCMD] " + respondScmdMessage.getResponse(), scmdSession.getChannel());
    }
}