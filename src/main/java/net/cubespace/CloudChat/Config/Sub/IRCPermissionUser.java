package net.cubespace.CloudChat.Config.Sub;


import net.cubespace.Yamler.Config.Config;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class IRCPermissionUser extends Config {
    @SuppressWarnings("CanBeFinal")
    public String IngameName = "";
    public ArrayList<String> Groups = new ArrayList<>();
    @SuppressWarnings("CanBeFinal")
    public ArrayList<String> Permissions = new ArrayList<>();
}
