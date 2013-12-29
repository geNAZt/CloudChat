package net.cubespace.CloudChat.Config.Sub;

import net.cubespace.CloudChat.Module.Spam.SpamAction;
import net.cubespace.lib.Configuration.Config;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:35
 */
public class SpamEntry extends Config {
    public Integer AmountOfMessages = 1000;
    public Integer InHowMuchSeconds = 10;
    public SpamAction ActionIfReached = SpamAction.MUTE;
    public Integer HowLongToMuteInSeconds = 30;
    public String MuteMessage = "You have been muted for %amount seconds due to spamming";
    public String CommandToDispatch = "ban %player Dont spam so much";
    public String KickMessage = "You have been kicked for Spamming";
    public String ExcludePermission = "cloudchat.spam.rule1";
}
