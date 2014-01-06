package net.cubespace.lib.Chat.MessageBuilder;

import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.IClickEvent;
import net.md_5.bungee.api.CommandSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 00:42
 */
public interface IMessageBuilder {
    public IMessageBuilder setText(String text);
    public IMessageBuilder addEvent(String ident, IClickEvent event);
    public void send(CommandSender sender);
}
