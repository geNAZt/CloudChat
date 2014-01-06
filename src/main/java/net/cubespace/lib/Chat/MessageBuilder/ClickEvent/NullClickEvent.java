package net.cubespace.lib.Chat.MessageBuilder.ClickEvent;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 01:45
 */
public class NullClickEvent implements IClickEvent {
    @Override
    public void setValue(String value) {

    }

    @Override
    public void setAction(ClickAction action) {

    }

    @Override
    public Object get() {
        return null;
    }
}
