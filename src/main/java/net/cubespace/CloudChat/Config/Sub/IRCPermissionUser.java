package net.cubespace.CloudChat.Config.Sub;

import net.cubespace.lib.Configuration.Config;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 04.01.14 00:40
 */
public class IRCPermissionUser extends Config {
    public String IngameName = "";
    public ArrayList<String> Groups = new ArrayList<>();
    public ArrayList<String> Permissions = new ArrayList<>();
}
