package net.cubespace.CloudChat.Module.IRC.Format;

import org.jibble.pircbot.Colors;

import java.util.HashMap;

/**
 * Created by Fabian on 29.11.13.
 */
public enum MCToIrcFormat {
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
    private static final HashMap<String, String> translate = new HashMap<>();

    private MCToIrcFormat(String value) {
        this.value = value;
    }

    static {
        createMap();
    }

    public String toString() {
        return this.value;
    }

    private static void createMap() {
        //Essentials Colors
        translate.put("&0", "");
        translate.put("&1", Colors.DARK_BLUE);
        translate.put("&2", Colors.DARK_GREEN);
        translate.put("&3", Colors.CYAN);
        translate.put("&4", Colors.RED);
        translate.put("&5", Colors.PURPLE);
        translate.put("&6", Colors.BLACK);
        translate.put("&7", Colors.LIGHT_GRAY);
        translate.put("&8", Colors.DARK_GRAY);
        translate.put("&9", Colors.BLUE);
        translate.put("&a", Colors.GREEN);
        translate.put("&b", Colors.CYAN);
        translate.put("&c", Colors.RED);
        translate.put("&d", Colors.MAGENTA);
        translate.put("&e", Colors.BLACK);
        translate.put("&f", "");
        translate.put("&k", "RAND");
        translate.put("&l", Colors.BOLD);
        translate.put("&m", "-");
        translate.put("&n", Colors.UNDERLINE);
        translate.put("&o", "");
        translate.put("&r", Colors.NORMAL);

        //MC Colors
        translate.put("§0", "");
        translate.put("§1", Colors.DARK_BLUE);
        translate.put("§2", Colors.DARK_GREEN);
        translate.put("§3", Colors.CYAN);
        translate.put("§4", Colors.RED);
        translate.put("§5", Colors.PURPLE);
        translate.put("§6", Colors.BLACK);
        translate.put("§7", Colors.LIGHT_GRAY);
        translate.put("§8", Colors.DARK_GRAY);
        translate.put("§9", Colors.BLUE);
        translate.put("§a", Colors.GREEN);
        translate.put("§b", Colors.CYAN);
        translate.put("§c", Colors.RED);
        translate.put("§d", Colors.MAGENTA);
        translate.put("§e", Colors.BLACK);
        translate.put("§f", "");
        translate.put("§k", "RAND");
        translate.put("§l", Colors.BOLD);
        translate.put("§m", "-");
        translate.put("§n", Colors.UNDERLINE);
        translate.put("§o", "");
        translate.put("§r", Colors.NORMAL);
    }

    public static String translateString(String value) {
        for (String code : translate.keySet()) {
            value = value.replace(code, translate.get(code));
        }
        return value;
    }

    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }

        for (String code : translate.keySet()) {
            input = input.replace(code, "");
        }

        return input;
    }

}
