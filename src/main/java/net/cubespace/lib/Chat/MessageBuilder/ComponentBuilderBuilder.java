package net.cubespace.lib.Chat.MessageBuilder;

import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.IClickEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 06.01.14 00:41
 */
public class ComponentBuilderBuilder implements IMessageBuilder {
    private String message;
    private TreeMap<String, IClickEvent> clickEventHashMap = new TreeMap<>();
    private static final Pattern url = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");

    private BaseComponent[] fromLegacyText(String message) {
        ArrayList<BaseComponent> components = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();
        Matcher matcher = url.matcher(message);

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == ChatColor.COLOR_CHAR) {
                i++;
                c = message.charAt(i);

                if (c >= 'A' && c <= 'Z') {
                    c += 32;
                }

                if (builder.length() > 0) {
                    TextComponent old = component;
                    component = new TextComponent(old);
                    old.setText(builder.toString());
                    builder = new StringBuilder();
                    components.add(old);
                }

                ChatColor format = ChatColor.getByChar(c);
                switch (format) {
                    case BOLD:
                        component.setBold(true);
                        break;
                    case ITALIC:
                        component.setItalic(true);
                        break;
                    case UNDERLINE:
                        component.setUnderlined(true);
                        break;
                    case STRIKETHROUGH:
                        component.setStrikethrough(true);
                        break;
                    case MAGIC:
                        component.setObfuscated(true);
                        break;
                    case RESET:
                        if(component.isBold()) component.setBold(false);
                        if(component.isUnderlined()) component.setUnderlined(false);
                        if(component.isItalic()) component.setBold(false);
                        if(component.isStrikethrough()) component.setStrikethrough(false);
                        if(component.isObfuscated()) component.setObfuscated(false);

                        format = ChatColor.WHITE;
                    default:
                        component.setColor(format);
                        break;
                }

                continue;
            }

            //We found a {
            if ((byte)c == 123) {
                i++;

                StringBuilder sb = new StringBuilder();
                while(i < message.length()) {
                    c = message.charAt(i);
                    i++;
                    if(c == '\u007D') {
                        break;
                    } else {
                        sb.append(c);
                    }
                }
                i--;

                String identifier = sb.toString().replace("click:", "");
                if(!clickEventHashMap.containsKey(identifier)) {
                    builder.append("{" + sb.toString() + "}");
                    continue;
                }

                ClickEvent clickEvent = (ClickEvent) clickEventHashMap.get(identifier).get();
                if (builder.length() > 0) {
                    StringBuilder whitespaces = new StringBuilder();
                    StringBuilder strBui = new StringBuilder();
                    StringBuilder whitespacee = new StringBuilder();
                    boolean end = false;

                    for(int ia = 0; ia < builder.length(); ia++) {
                        if((byte) builder.charAt(ia) == 32) {
                            if(!end) {
                                whitespaces.append(builder.charAt(ia));
                            } else {
                                boolean append = true;
                                for(int ia2 = ia; ia2 < builder.length(); ia2++) {
                                    if((byte) builder.charAt(ia2) != 32) {
                                        append = false;
                                    }
                                }

                                if(append)
                                    whitespacee.append(builder.charAt(ia));
                                else
                                    strBui.append(builder.charAt(ia));
                            }
                        } else {
                            strBui.append(builder.charAt(ia));
                            end = true;
                        }
                    }

                    if(whitespaces.length() > 0) {
                        TextComponent old = component;
                        component = new TextComponent(old);
                        old.setText(whitespaces.toString());
                        components.add(old);
                    }

                    TextComponent oldb = component;
                    component = new TextComponent(oldb);
                    oldb.setText(strBui.toString());
                    oldb.setClickEvent(clickEvent);
                    builder = new StringBuilder();
                    components.add(oldb);

                    if(whitespacee.length() > 0) {
                        TextComponent olde = component;
                        component = new TextComponent(olde);
                        olde.setText(whitespacee.toString());
                        components.add(component);
                    }
                }

                continue;
            }

            int pos = message.indexOf(' ', i);
            if (pos == -1) {
                pos = message.length();
            }

            if (matcher.region(i, pos).find()) { //Web link handling

                if (builder.length() > 0) {
                    TextComponent old = component;
                    component = new TextComponent(old);
                    old.setText(builder.toString());
                    builder = new StringBuilder();
                    components.add(old);
                }

                TextComponent old = component;
                component = new TextComponent(old);
                String urlString = message.substring(i, pos);
                component.setText(urlString);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        urlString.startsWith("http") ? urlString : "http://" + urlString));
                components.add(component);
                i += pos - i - 1;
                component = old;
                continue;
            }

            builder.append(c);
        }

        if (builder.length() > 0) {
            component.setText(builder.toString());
            components.add(component);
        }

        //The client will crash if the array is empty
        if (components.size() == 0) {
            components.add(new TextComponent(""));
        }

        return components.toArray(new BaseComponent[components.size()]);
    }


    @Override
    public IMessageBuilder setText(String text) {
        this.message = text;

        return this;
    }

    @Override
    public IMessageBuilder addEvent(String ident, IClickEvent event) {
        clickEventHashMap.put(ident, event);

        return this;
    }

    @Override
    public void send(CommandSender sender) {
        sender.sendMessage(fromLegacyText(message));
    }
}
