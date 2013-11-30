package net.cubespace.CloudChat.Util;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 13:26
 */
public class StringUtils {
    public static String join(String[] strings, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Integer length = strings.length;

        for(Integer i = 0; i < length; i++) {
            builder.append(strings[i]);

            if(i == length - 1) {
                break;
            }

            builder.append(delimiter);
        }

        return builder.toString();
    }
}
