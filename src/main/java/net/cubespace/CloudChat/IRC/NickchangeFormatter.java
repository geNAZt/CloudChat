package net.cubespace.CloudChat.IRC;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 12.12.13 19:14
 */
public class NickchangeFormatter {
    public static String format(String message, String oldNick, String newNick) {
        message.replace("%old_nick", oldNick);
        message.replace("%new_nick", newNick);

        return message;
    }
}