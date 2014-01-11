package net.cubespace.CloudChat.Module.PlayerManager.Database;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.Mail.Database.Mail;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 20:26
 */
public class PlayerDatabase extends Config {
    public PlayerDatabase(CloudChatPlugin plugin, String userName) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + userName + ".yml");
        CONFIG_HEADER = "User Database for " + userName;

        Nick = userName;
        Focus = ((Main) plugin.getConfigManager().getConfig("main")).Global;
    }

    public ArrayList<String> JoinedChannels = new ArrayList<String>();
    public String Focus;
    public String Nick = "";
    public String Prefix = "";
    public String Suffix = "";
    public String Reply = "";
    public String World = "";
    public String WorldAlias = "";
    public Boolean AFK = false;
    public String Server = "";
    public Boolean Ignore = false;
    public Boolean Output = true;
    public ArrayList<Mail> Mails = new ArrayList<>();
}
