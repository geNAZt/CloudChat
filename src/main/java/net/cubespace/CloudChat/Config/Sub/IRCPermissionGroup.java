package net.cubespace.CloudChat.Config.Sub;


import net.cubespace.Yamler.Config.Config;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class IRCPermissionGroup extends Config {
    public int Rank = 0;
    public ArrayList<String> Permissions = new ArrayList<>();
    public ArrayList<String> Inherits = new ArrayList<>();
    public String prefix = "";
    public String suffix = "";
}
