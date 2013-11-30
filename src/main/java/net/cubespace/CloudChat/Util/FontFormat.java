package net.cubespace.CloudChat.Util;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 21:47
 */
public enum FontFormat {
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
    private static final String characterValue = "\u00a7";
    private static final HashMap<String, String> translate = new HashMap<>();
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf("&") + "[0-9A-FK-OR]");

    private FontFormat(String value) {
        this.value = characterValue + value;
    }

    static {
        createMap();
    }

    public String toString() {
        return this.value;
    }

    private static void createMap() {
        translate.put("&0", characterValue + "0");
        translate.put("&1", characterValue + "1");
        translate.put("&2", characterValue + "2");
        translate.put("&3", characterValue + "3");
        translate.put("&4", characterValue + "4");
        translate.put("&5", characterValue + "5");
        translate.put("&6", characterValue + "6");
        translate.put("&7", characterValue + "7");
        translate.put("&8", characterValue + "8");
        translate.put("&9", characterValue + "9");
        translate.put("&a", characterValue + "a");
        translate.put("&b", characterValue + "b");
        translate.put("&c", characterValue + "c");
        translate.put("&d", characterValue + "d");
        translate.put("&e", characterValue + "e");
        translate.put("&f", characterValue + "f");
        translate.put("&k", characterValue + "k");
        translate.put("&l", characterValue + "l");
        translate.put("&m", characterValue + "m");
        translate.put("&n", characterValue + "n");
        translate.put("&o", characterValue + "o");
        translate.put("&r", characterValue + "r");
    }

    public static String translateString(String value) {
        if(value == null) return null;

        for (String code : translate.keySet()) {
            value = value.replace(code, translate.get(code));
        }

        return value;
    }

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

}
