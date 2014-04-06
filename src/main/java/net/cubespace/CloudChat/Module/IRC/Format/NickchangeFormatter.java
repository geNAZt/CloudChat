package net.cubespace.CloudChat.Module.IRC.Format;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class NickchangeFormatter {
    public static String format(String message, String oldNick, String newNick) {
        message = message.replace("%old_nick", oldNick);
        message = message.replace("%new_nick", newNick);

        return message;
    }
}
