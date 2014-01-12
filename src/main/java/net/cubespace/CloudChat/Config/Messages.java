package net.cubespace.CloudChat.Config;

import net.cubespace.lib.Configuration.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Messages extends Config {
    public Messages(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "messages.yml");
    }

    public String NoPermission = "&cYou don't have the Permission to use this Command";


    public String PlayerJoin = "&8[&2%channel_short&8]&r %prefix%nick{click:playerMenu}%suffix&r joined the Server";
    public String PlayerQuit = "&8[&2%channel_short&8]&r %prefix%nick%suffix&r left the Server";

    public String PlayerGotAfk = "&8[&2%channel_short&8]&r %prefix%nick{click:playerMenu}%suffix&r is afk";
    public String PlayerGotOutOfAfk = "&8[&2%channel_short&8]&r %prefix%nick{click:playerMenu}%suffix&r is not afk anymore";

    public String Complete_Player = "%prefix%nick%suffix&r";

    public String Command_CC_Report_On = "&6Reporting has been enabled and the Report will be saved to %file. Type &c/cc:report&6 off to stop or it will automaticly stop after &a10&6 Minutes";
    public String Command_CC_Report_Off = "&6Reporting is now &aoff";

    public String Command_Channel_Join_OnlyPlayers = "&cOnly Players can join Channels";
    public String Command_Channel_Join_ChannelDoesNotExist = "&cThis Channel does not exist";
    public String Command_Channel_Join_PasswordProtected = "&cThis Channel is password protected";
    public String Command_Channel_Join_WrongPassword = "&cThe password for this Channel is wrong";
    public String Command_Channel_Join_JoinedChannel = "&6You have joined Channel &a%channel";

    public String Command_Channel_Leave_OnlyPlayers = "&cOnly Players can leave Channels";
    public String Command_Channel_Leave_ChannelDoesNotExist = "&cThis Channel does not exist";
    public String Command_Channel_Leave_ForcedChannel = "&cYou can't leave a forced Channel";
    public String Command_Channel_Leave_LeftChannel = "&6You have left Channel &a%channel";
    public String Command_Channel_Leave_FocusChannel = "&6You now focus Channel &a%channel";

    public String Command_Channel_Focus_OnlyPlayers = "&cOnly Players can focus Channels";
    public String Command_Channel_Focus_ChannelDoesNotExist = "&cThis Channel does not exist";
    public String Command_Channel_Focus_NotIn = "&cYou can't focus a Channel where you aren't in";
    public String Command_Channel_Focus_FocusChannel = "&6You now focus Channel &a%channel";

    public String Command_Channel_ListChannel_ArgumentMissing = "&6You now focus Channel &a%channel";
    public String Command_Channel_ListChannel_ChannelDoesNotExist = "&cThis Channel does not exist";
    public String Command_Channel_ListChannel_Header = "&cCurrently in the Channel &a%channel";
    public String Command_Channel_ListChannel_Player = "%prefix%nick%suffix&r";

    public String Command_Channel_List_Header = "&cPlayers currently online";
    public String Command_Channel_List_Player = "%prefix%nick%suffix&r";

    public String Command_Channel_Create_AlreadyExists = "&cThis channel already exists";
    public String Command_Channel_Create_ErrorInSave = "&cThere was an error in the creation of your Channel. Please report this to a Admin";
    public String Command_Channel_Create_CreatedChannel = "&6Channel &a%channel was created. Password is &a%password";

    public String Command_Channel_Invite_OfflinePlayer = "&cYou can't invite offline Players";
    public String Command_Channel_Invite_SecondArgumentError = "&cIf you are not ingame you must give a channel as second Argument";
    public String Command_Channel_Invite_ChannelDoesNotExist = "&cThis Channel does not exist";
    public String Command_Channel_Invite_CantInvite = "&cYou can't send invitations for this Channel";
    public String Command_Channel_Invite_SendInvitation = "&6You got an Invitation to the &a%channel&6 Channel. Join with &a/join{click:joinChannel} %channel %password";
    public String Command_Channel_Invite_Notification = "&6You have sent an Invitation to &a%name";

    public String Command_CC_Reload_Success = "&6Reloaded all Channels and IRC Config";

    public String Command_IRC_Reconnect_Success = "&6The IRC Bot was reconnected";

    public String IRC_Command_Message_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    public String IRC_Command_Message_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    public String IRC_Command_Message_OfflinePlayer = "&c%nick: You can't message offline Players";

    public String IRC_Command_Mute_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    public String IRC_Command_Mute_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    public String IRC_Command_Mute_OfflinePlayer = "&c%nick: You can't mute offline Players";
    public String IRC_Command_Mute_Success = "&6%nick: You have muted %muted";

    public String IRC_Command_Players_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    public String IRC_Command_Players_Header = "&6Current Players online (%count): ";
    public String IRC_Command_Players_Player = "%prefix%nick%suffix&r";

    public String IRC_Command_Scmd_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    public String IRC_Command_Scmd_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    public String IRC_Command_Scmd_NotEnoughPermissionForServer = "&c%nick: You dont't have Permission to execute this on that Server";
    public String IRC_Command_Scmd_NotEnoughPermissionForCommand = "&c%nick: You dont't have Permission to execute that Scmd Command";
    public String IRC_Command_Scmd_InvalidServer = "&c%nick: You dont't have Permission to execute that Scmd Command";
    public String IRC_Command_Scmd_ServerIsEmpty = "&c%nick: Can't send the Command. It needs to be at least one Player online on the Server";
    public String IRC_Command_Scmd_CommandIssued = "&6Command has been issued";

    public String IRC_Command_Unmute_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    public String IRC_Command_Unmute_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    public String IRC_Command_Unmute_OfflinePlayer = "&c%nick: You can't unmute offline Players";
    public String IRC_Command_Unmute_Success = "&6%nick: You have unmuted %muted";

    public String IRC_PmSessionChangedTo = "!!! %from you are now talking with %to !!!";

    public String Command_Mail_Send_NotPlayer = "&cOnly Players can send mails";
    public String Command_Mail_Send_UnknownPlayer = "&cThis Player is not known. Can not send a Mail to it";
    public String Command_Mail_Send_Success = "&6Mail was sent";

    public String Command_Mail_Read_NotPlayer = "&cOnly Players can read mails";
    public String Command_Mail_Read_Header = "&6=== &aMails&6 ===";

    public String Command_Mail_Clear_NotPlayer = "&cOnly Players can clear mails";
    public String Command_Mail_Clear_Success = "&6Mails where cleared";

    public String Command_Mute_NotPlayer = "&cOnly Players can mute";
    public String Command_Mute_OfflinePlayer = "&cYou can't mute offline Players";
    public String Command_Mute_Success = "&6You muted %player";

    public String Command_Unmute_NotPlayer = "&cOnly Players can unmute";
    public String Command_Unmute_OfflinePlayer = "&cYou can't unmute offline Players";
    public String Command_Unmute_Success = "&6You unmuted %player";

    public String Command_CC_Mute_OfflinePlayer = "&cYou can't mute offline Players";
    public String Command_CC_Mute_Success = "&6You muted %player";

    public String Command_CC_Unmute_OfflinePlayer = "&cYou can't unmute offline Players";
    public String Command_CC_Unmute_Success = "&6You unmuted %player";

    public String Command_Nick_NotPlayer = "&cOnly Players can change nicknames";
    public String Command_Nick_NoPermissionToChangeOther = "&cYou are not allowed to change Nicknames of other Players";
    public String Command_Nick_OfflinePlayer = "&cYou can not change Nicknames from offline Players";
    public String Command_Nick_ChangedNick = "&6Your nickname was changed to &a%nick";

    public String Command_Msg_NotPlayer = "&cOnly Players can send Messages";

    public String Command_Reply_NotPlayer = "&cOnly Players can reply on Messages";
    public String Command_Reply_NoConversation = "&cYou don't have a Conversation to reply to";

    public String Message_Sender = "&6You&8 -> &6%receiver&8:&7 %message";
    public String Message_Receiver = "&6%sender&8 -> &6You&8:&7 %message";
    public String Message_OfflinePlayer = "&cYou can't message to offline Players";
    public String Message_Self = "&cYou can't message with yourself";
    public String Message_NoIrcNick = "&cYou can not send PMs to IRC";
    public String Message_IrcNickNotOnline = "&cThis IRC Nick is not online";

    public String Playermenu_Prefix = "%prefix%nick%suffix&8:&r ";
    public String Playermenu_Unmute = "&a&nUnmute{click:unMute}&r | ";
    public String Playermenu_Mute = "&c&nMute{click:mute}&r | ";
    public String Playermenu_Message = "&6&nMessage{click:message}";

    public String Command_Realname_OfflinePlayer = "&cYou can not get the real Name of offline Players";
    public String Command_Realname_Success = "&6The Realname of %nick is &a%realname";

    public String Command_BroadCast_Message = "&c[&aBroadcast&c]&r %message";
}
