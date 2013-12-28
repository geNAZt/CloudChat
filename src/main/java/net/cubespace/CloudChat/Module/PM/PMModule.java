package net.cubespace.CloudChat.Module.PM;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PM.Command.PM;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 19:23
 */
public class PMModule {
    public PMModule(CloudChatPlugin plugin) {
        //Register Commands
        new PM(plugin);
    }
}
