package net.cubespace.lib.EventBus;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 17:46
 */
public enum EventPriority {
    HIGHEST(128),
    HIGH(64),
    NORMAL(0),
    LOW(-64),
    LOWEST(-128);

    private Integer prio;

    EventPriority(Integer prio) {
        this.prio = prio;
    }

    public Integer getPrio() {
        return this.prio;
    }
}
