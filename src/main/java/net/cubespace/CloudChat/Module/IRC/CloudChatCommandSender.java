package net.cubespace.CloudChat.Module.IRC;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Collection;
import java.util.Collections;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 11.01.14 01:02
 */
public class CloudChatCommandSender implements CommandSender {
    private IRCModule ircModule;
    private IRCSender sender;

    public CloudChatCommandSender(IRCModule ircModule, IRCSender sender) {
        this.ircModule = ircModule;
        this.sender = sender;
    }

    @Override
    public String getName() {
        return "CloudChat";
    }

    @Override
    public void sendMessage(String s) {
        ircModule.getIrcBot().sendToChannel("[SCMD] " + s, sender.getChannel());
    }

    @Override
    public void sendMessages(String... strings) {
        for (String string : strings)
            sendMessage(string);
    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        for (BaseComponent component : baseComponents) {
            sendMessage(component.toLegacyText());
        }
    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {
        sendMessage(baseComponent.toLegacyText());
    }

    @Override
    public Collection<String> getGroups() {
        return Collections.emptySet();
    }

    @Override
    public void addGroups(String... strings) {
    }

    @Override
    public void removeGroups(String... strings) {
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public void setPermission(String s, boolean b) {

    }

    public Collection<String> getPermissions() {
        return Collections.emptySet();
    }
}