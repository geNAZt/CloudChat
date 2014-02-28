package net.cubespace.CloudChat.Util;

import java.util.LinkedHashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class StringUtils {
    private static LinkedHashMap<Character, Integer> timeMultiplier = new LinkedHashMap<Character, Integer>() {{
        put('d', 86400);
        put('h', 3600);
        put('m', 60);
        put('s', 1);
    }};

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

    public static int getTime(String timestring) {
        int resultSeconds = 0;

        try {
            resultSeconds = Integer.parseInt(timestring);
        } catch (NumberFormatException e) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < timestring.length(); i++) {
                char c = timestring.charAt(i);
                byte b = (byte) c;

                if (b > 47 && b < 58) {
                    sb.append(c);
                } else {
                    if(timeMultiplier.containsKey(c)) {
                        int cur = Integer.parseInt(sb.toString());
                        resultSeconds = cur * timeMultiplier.get(c);
                    }

                    sb = new StringBuilder();
                }
            }
        }

        return resultSeconds;
    }
}
