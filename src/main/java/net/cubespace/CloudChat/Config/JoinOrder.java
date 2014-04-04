package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class JoinOrder extends Config {
    public JoinOrder(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "joinOrder.yml");
    }

    @Comments({
        "Key are the Server names out of the BungeeCord config",
        "The value is a List of Channel names which get checked from top to bottom",
        "The first hit gets joined into. When the player can't join any it joins the Global Channel"
    })
    public HashMap<String, ArrayList<String>> JoinOrder = new HashMap<>();
}
