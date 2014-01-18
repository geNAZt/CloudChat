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
        translate.put("13", "&d");
        translate.put("8", "&e");
        translate.put("0", "&f");
        translate.put("5", "&0");
        translate.put("7", "&6");
        translate.put("11", "&b");
    }

    private IrcToMCFormat(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static String translateString(String value) {
        int length = value.length();

        boolean bold = false;
        boolean underline = false;
        boolean strike = false;
        boolean italic = false;
        String lastColor = "&f";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);

            switch ((byte) ch) {
                case 3:
                    String color = "";
                    i++;

                    while (i < length) {
                        char d = value.charAt(i);
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

                    if(translate.containsKey(color)) {
                        if(color.startsWith("0")) {
                            color = color.substring(1);
                        }

                        sb.append(translate.get(color));
                        lastColor = translate.get(color);
                    }

                    //if color is "" reset
                    if(color.equals("")) {
                        sb.append("&r");

                        if(bold) {
                            sb.append("&l");
                        }

                        if(strike) {
                            sb.append("&m");
                        }

                        if(italic) {
                            sb.append("&o");
                        }

                        if(underline) {
                            sb.append("&n");
                        }
                    }

                    break;

                case 2:
                    if(!bold) {
                        sb.append("&l");
                        bold = true;
                    } else {
                        sb.append("&r");
                        sb.append(lastColor);

                        if(strike) {
                            sb.append("&m");
                        }

                        if(italic) {
                            sb.append("&o");
                        }

                        if(underline) {
                            sb.append("&n");
                        }

                        bold = false;
                    }

                    break;

                case 15:
                    sb.append("&r");
                    lastColor = "&f";
                    break;

                case 19:
                    if(!strike) {
                        sb.append("&m");
                        strike = true;
                    } else {
                        sb.append("&r");
                        sb.append(lastColor);

                        if(bold) {
                            sb.append("&l");
                        }

                        if(italic) {
                            sb.append("&o");
                        }

                        if(underline) {
                            sb.append("&n");
                        }

                        strike = false;
                    }

                    break;

                case 22:
                case 29:
                    if(!italic) {
                        sb.append("&o");
                        italic = true;
                    } else {
                        sb.append("&r");
                        sb.append(lastColor);

                        if(bold) {
                            sb.append("&l");
                        }

                        if(strike) {
                            sb.append("&m");
                        }

                        if(underline) {
                            sb.append("&n");
                        }
                    }
                    break;

                case 31:
                    if(underline) {
                        sb.append("&n");
                        underline = true;
                    } else {
                        sb.append("&r");
                        sb.append(lastColor);

                        if(bold) {
                            sb.append("&l");
                        }

                        if(strike) {
                            sb.append("&m");
                        }

                        if(italic) {
                            sb.append("&o");
                        }
                    }

                default:
                    sb.append(ch);
            }
        }

        return sb.toString();
    }
}
