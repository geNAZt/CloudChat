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
        translate.put("1", "&0");
        translate.put("2", "&1");
        translate.put("3", "&2");
        translate.put("10", "&3");
        translate.put("4", "&4");
        translate.put("6", "&5");
        translate.put("15", "&7");
        translate.put("14", "&8");
        translate.put("12", "&9");
        translate.put("9", "&a");
        translate.put("11", "&b");
        translate.put("13", "&d");
        translate.put("8", "&e");
        translate.put("0", "&f");
        translate.put("5", "&0");
        translate.put("7", "&c");
    }

    private IrcToMCFormat(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static String translateString(String message) {
        int length = message.length();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char ch = message.charAt(i);
            if ((byte) ch == 3) {
                String color = "";
                i++;

                while (i < length) {
                    char d = message.charAt(i);
                    if ((d >= '0') && (d <= '9')) {
                        if (color.length() == 2) {
                            i--;
                            break;
                        }

                        color += d;
                        i++;
                    } else {
                        i--;
                        break;
                    }
                }


                if (translate.containsKey(color)) {
                    buffer.append(translate.get(color));
                    break;
                }
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
}
