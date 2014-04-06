package net.cubespace.CloudChat.Module.IRC.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.ScmdSession;
import net.cubespace.PluginMessages.RespondScmdMessage;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PluginMessageListener implements PacketListener {
    private final IRCModule ircModule;

    public PluginMessageListener(IRCModule ircModule) {
        this.ircModule = ircModule;
    }

    @SuppressWarnings("unused")
    @PacketHandler
    public void onRespondScmd(RespondScmdMessage respondScmdMessage) {
        //Get the SCMD Session
        ScmdSession scmdSession = ircModule.getIrcBot().getIrcManager().getScmdSession(respondScmdMessage.getId());
        if(scmdSession == null) return;

        scmdSession.newResponse();

        ircModule.getIrcBot().sendToChannel("[SCMD] " + respondScmdMessage.getResponse(), scmdSession.getChannel());
    }
}