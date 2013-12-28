package net.cubespace.CloudChat.Module.IRC.Format;

import org.jibble.pircbot.Colors;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:24
 */
public enum IrcToMCFormat {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    PURPLE("5"),
    ORANGE("6"),
    GREY("7"),
    DARK_GREY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    PINK("d"),
    YELLOW("e"),
    WHITE("f"),
    RANDOM("k"),
    BOLD("l"),
    STRIKE("m"),
    UNDERLINED("n"),
    ITALICS("o"),
    RESET("r");

    private final String value;

    private IrcToMCFormat(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static String translateString(String value) {
        return Colors.removeFormattingAndColors(value);
    }
}
