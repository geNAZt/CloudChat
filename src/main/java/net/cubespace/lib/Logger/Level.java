package net.cubespace.lib.Logger;

/**
 * Enum which holds all Supported Levels in this Logger
 */
public enum Level {
    DEBUG(3),
    INFO(2),
    WARN(1),
    ERROR(0);

    private Integer levelCode;
    public Integer getLevelCode() {
        return levelCode;
    }

    private Level(Integer errorCode) {
        this.levelCode = errorCode;
    }
}
