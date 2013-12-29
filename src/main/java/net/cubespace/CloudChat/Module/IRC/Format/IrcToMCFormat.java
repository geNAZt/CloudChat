package net.cubespace.CloudChat.Module.IRC.Format;

import org.jibble.pircbot.Colors;

import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:24
 */
public enum IrcToMCFormat {
    BLACK(Colors.BLACK),
    DARK_BLUE(Colors.DARK_BLUE),
    DARK_GREEN(Colors.DARK_GREEN),
    DARK_AQUA(Colors.CYAN),
    DARK_RED(Colors.RED),
    PURPLE(Colors.PURPLE),
    ORANGE(Colors.BLACK),
    GREY(Colors.LIGHT_GRAY),
    DARK_GREY(Colors.DARK_GRAY),
    BLUE(Colors.BLUE),
    GREEN(Colors.GREEN),
    AQUA(Colors.CYAN),
    RED(Colors.RED),
    PINK(Colors.MAGENTA),
    YELLOW(Colors.YELLOW),
    WHITE(Colors.WHITE),
    RANDOM(Colors.REVERSE),
    BOLD(Colors.BOLD),
    STRIKE("-"),
    UNDERLINED(Colors.UNDERLINE),
    ITALICS(""),
    RESET(Colors.NORMAL);

    private final String value;
    private static final HashMap<String, String> translate = new HashMap<>();

    static {
        createMap();
    }

    private static void createMap() {
        //IRC Colors
        translate.put(Colors.BLACK, "&0");
        translate.put(Colors.DARK_BLUE, "&1");
        translate.put(Colors.DARK_GREEN, "&2");
        translate.put(Colors.CYAN, "&3");
        translate.put(Colors.RED, "&4");
        translate.put(Colors.PURPLE, "&5");
        translate.put(Colors.LIGHT_GRAY, "&7");
        translate.put(Colors.DARK_GRAY, "&8");
        translate.put(Colors.BLUE, "&9");
        translate.put(Colors.GREEN, "&a");
        translate.put(Colors.MAGENTA, "&d");
        translate.put(Colors.BOLD, "&l");
        translate.put(Colors.UNDERLINE, "&n");
        translate.put(Colors.YELLOW, "&e");
        translate.put(Colors.WHITE, "&f");
        translate.put(Colors.REVERSE, "&k");
        translate.put(Colors.UNDERLINE, "&n");
        translate.put(Colors.NORMAL, "&r");

    }

    private IrcToMCFormat(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static String translateString(String value) {
        for (String code : translate.keySet()) {
            value = value.replace(code, translate.get(code));
        }

        return value;
    }
}
