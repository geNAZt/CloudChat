package net.cubespace.lib.Chat.MessageBuilder.ClickEvent;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 00:48
 */
public interface IClickEvent {
    public void setValue(String value);
    public void setAction(ClickAction action);
    public Object get();
}
