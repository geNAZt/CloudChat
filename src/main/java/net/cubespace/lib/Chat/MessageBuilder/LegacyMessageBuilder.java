package net.cubespace.lib.Chat.MessageBuilder;

import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.IClickEvent;
import net.md_5.bungee.api.CommandSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 02:12
 */
public class LegacyMessageBuilder implements IMessageBuilder {
    private String message;

    @Override
    public IMessageBuilder setText(String text) {
        this.message = text;

        return this;
    }

    @Override
    public IMessageBuilder addEvent(String ident, IClickEvent event) {

        return this;
    }

    @Override
    public void send(CommandSender sender) {
        sender.sendMessage(message.replace("\\{click:([^}]*)\\}", ""));
    }

    public String getString() {
        return message.replace("\\{click:([^}]*)\\}", "");
    }
}
