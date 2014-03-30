package net.cubespace.CloudChat.Module.FormatHandler.Format;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.lib.CubespacePlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class MessageFormat {
    private static CubespacePlugin plugin;

    public static void setPlugin(CubespacePlugin plugin) {
        MessageFormat.plugin = plugin;
    }

    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase) {
        return format(message, channel, playerDatabase, false);
    }

    private static void preCheckFormats(ChannelDatabase channelDatabase) {
        boolean save = false;

        if(!channelDatabase.Formats.containsKey("channel_short")) {
            channelDatabase.Formats.put("channel_short", "%channel_short");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("channel_name")) {
            channelDatabase.Formats.put("channel_name", "%channel_name");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("nick")) {
            channelDatabase.Formats.put("nick", "%nick");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("prefix")) {
            channelDatabase.Formats.put("prefix", "%prefix");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("suffix")) {
            channelDatabase.Formats.put("suffix", "%suffix");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("faction")) {
            channelDatabase.Formats.put("faction", "%faction");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("town")) {
            channelDatabase.Formats.put("town", "%town");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("nation")) {
            channelDatabase.Formats.put("nation", "%nation");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("server")) {
            channelDatabase.Formats.put("server", "%server");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("world_alias")) {
            channelDatabase.Formats.put("world_alias", "%world_alias");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("world")) {
            channelDatabase.Formats.put("world", "%world");
            save = true;
        }

        if(!channelDatabase.Formats.containsKey("player")) {
            channelDatabase.Formats.put("player", "%player");
            save = true;
        }

        if(save) {
            try {
                channelDatabase.save();
            } catch (InvalidConfigurationException e) { }
        }
    }

    public static String format(String message, ChannelDatabase channel, PlayerDatabase playerDatabase, boolean stipcolor) {
        //Channel things
        String output = message;

        if(channel != null) {
            preCheckFormats(channel);

            //Channel formats
            if(!channel.Short.equals("")) {
                output = output.replace("%channel_short", channel.Formats.get("channel_short").replace("%channel_short", channel.Short.toUpperCase()));
            } else {
                output = output.replace("%channel_short", "");
            }

            if(!channel.Name.equals("")) {
                output = output.replace("%channel_name", channel.Formats.get("channel_name").replace("%channel_name", channel.Name));
            } else {
                output = output.replace("%channel_name", "");
            }

            //Player formats
            if(!playerDatabase.Nick.equals("")) {
                String nick = playerDatabase.Nick;
                if (!playerDatabase.Nick.equals(playerDatabase.Realname)) {
                    nick = ((Main) plugin.getConfigManager().getConfig("main")).NicknamePrefix + nick;
                }

                output = output.replace("%nick", channel.Formats.get("nick").replace("%nick", nick));
            } else {
                output = output.replace("%nick", "");
            }

            if(!playerDatabase.Prefix.equals("")) {
                output = output.replace("%prefix", channel.Formats.get("prefix").replace("%prefix", playerDatabase.Prefix));
            } else {
                output = output.replace("%prefix", "");
            }

            if(!playerDatabase.Suffix.equals("")) {
                output = output.replace("%suffix", channel.Formats.get("suffix").replace("%suffix", playerDatabase.Suffix));
            } else {
                output = output.replace("%suffix", "");
            }

            if(!playerDatabase.Faction.equals("")) {
                output = output.replace("%faction", channel.Formats.get("faction").replace("%faction", playerDatabase.Faction));
            } else {
                output = output.replace("%faction", "");
            }

            if(!playerDatabase.Town.equals("")) {
                output = output.replace("%town", channel.Formats.get("town").replace("%town", playerDatabase.Town));
            } else {
                output = output.replace("%town", "");
            }

            if(!playerDatabase.Nation.equals("")) {
                output = output.replace("%nation", channel.Formats.get("nation").replace("%nation", playerDatabase.Nation));
            } else {
                output = output.replace("%nation", "");
            }

            if(!playerDatabase.Server.equals("")) {
                output = output.replace("%server", channel.Formats.get("server").replace("%server", playerDatabase.Server));
            } else {
                output = output.replace("%server", "");
            }

            if(!playerDatabase.WorldAlias.equals("")) {
                output = output.replace("%world_alias", channel.Formats.get("world_alias").replace("%world_alias", playerDatabase.WorldAlias));
            } else {
                output = output.replace("%world_alias", "");
            }

            if(!playerDatabase.World.equals("")) {
                output = output.replace("%world", channel.Formats.get("world").replace("%world", playerDatabase.World));
            } else {
                output = output.replace("%world", "");
            }

            if(!playerDatabase.Realname.equals("")) {
                output = output.replace("%player", channel.Formats.get("player").replace("%player", playerDatabase.Realname));
            } else {
                output = output.replace("%player", "");
            }
        } else {
            String nick = playerDatabase.Nick;
            if (!playerDatabase.Nick.equals(playerDatabase.Realname)) {
                nick = ((Main) plugin.getConfigManager().getConfig("main")).NicknamePrefix + nick;
            }

            //Player things
            output = output.replace("%nick", nick);
            output = output.replace("%prefix", playerDatabase.Prefix);
            output = output.replace("%suffix", playerDatabase.Suffix);
            output = output.replace("%faction", playerDatabase.Faction);
            output = output.replace("%town", playerDatabase.Town);
            output = output.replace("%nation", playerDatabase.Nation);
            output = output.replace("%player", playerDatabase.Realname);

            //Server things
            output = output.replace("%server", playerDatabase.Server);

            //World things
            output = output.replace("%world_alias", playerDatabase.WorldAlias);
            output = output.replace("%world", playerDatabase.World);
        }

        // Le custom formats
        for (Map.Entry<String, String> customFormatEntry : new HashMap<>(playerDatabase.CustomFormats).entrySet()) {
            output = output.replace("%" + customFormatEntry.getKey(), customFormatEntry.getValue());
        }

        if(stipcolor)
            return FontFormat.stripColor(output);
        else
            return FontFormat.translateString(output);
    }
}
