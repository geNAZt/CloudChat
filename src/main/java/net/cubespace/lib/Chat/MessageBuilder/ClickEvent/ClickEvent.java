package net.cubespace.lib.Chat.MessageBuilder.ClickEvent;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 01:38
 */
public class ClickEvent implements IClickEvent {
    private static boolean useClickEvent = false;
    private IClickEvent clickEvent;

    static {
        try {
            Class.forName("net.md_5.bungee.api.chat.ClickEvent");
            useClickEvent = true;
        } catch (ClassNotFoundException e) {
            useClickEvent = false;
        }
    }

    public ClickEvent() {
        if(useClickEvent) {
            clickEvent = new BungeeClickEvent();
        } else {
            clickEvent = new NullClickEvent();
        }
    }

    @Override
    public void setValue(String value) {
        clickEvent.setValue(value);
    }

    @Override
    public void setAction(ClickAction action) {
        clickEvent.setAction(action);
    }

    @Override
    public Object get() {
        return clickEvent.get();
    }
}
