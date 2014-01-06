package net.cubespace.lib.Chat.MessageBuilder.ClickEvent;

import net.md_5.bungee.api.chat.ClickEvent;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 01:40
 */
public class BungeeClickEvent implements IClickEvent {
    private String value;
    private ClickAction action;

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setAction(ClickAction action) {
        this.action = action;
    }

    @Override
    public Object get() {
        ClickEvent.Action eventAction = ClickEvent.Action.SUGGEST_COMMAND;

        if(action.equals(ClickAction.OPEN_FILE)) {
            eventAction = ClickEvent.Action.OPEN_FILE;
        }

        if(action.equals(ClickAction.OPEN_URL)) {
            eventAction = ClickEvent.Action.OPEN_URL;
        }

        if(action.equals(ClickAction.RUN_COMMAND)) {
            eventAction = ClickEvent.Action.RUN_COMMAND;
        }

        if(action.equals(ClickAction.SUGGEST_COMMAND)) {
            eventAction = ClickEvent.Action.SUGGEST_COMMAND;
        }

        return new ClickEvent(eventAction, value);
    }
}
