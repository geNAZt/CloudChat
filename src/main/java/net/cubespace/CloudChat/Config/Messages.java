package net.cubespace.CloudChat.Config;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Messages extends Config {
    public Messages(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "messages.yml");
    }

    @SuppressWarnings("CanBeFinal")
    public String NoPermission = "&cYou don't have the Permission to use this Command";

    @SuppressWarnings("CanBeFinal")
    public String PlayerJoin = "&8[&2%channel_short{click:focusChannel}&8]&r %prefix%nick{click:playerMenu}%suffix&r joined the Server";
    @SuppressWarnings("CanBeFinal")
    public String PlayerQuit = "&8[&2%channel_short{click:focusChannel}&8]&r %prefix%nick%suffix&r left the Server";

    @SuppressWarnings("CanBeFinal")
    public String PlayerGotAfk = "&8[&2%channel_short{click:focusChannel}&8]&r %prefix%nick{click:playerMenu}%suffix&r is afk";
    @SuppressWarnings("CanBeFinal")
    public String PlayerGotOutOfAfk = "&8[&2%channel_short{click:focusChannel}&8]&r %prefix%nick{click:playerMenu}%suffix&r is not afk anymore";

    @SuppressWarnings("CanBeFinal")
    public String Complete_Player = "%prefix%nick%suffix&r";

    @SuppressWarnings("CanBeFinal")
    public String PM_Blocked = "&cYou can't send PMs to this Player";

    @SuppressWarnings("CanBeFinal")
    public String Channels_MaximumAmount = "&cYou cant join this Channel. You are already in the maximum Amount of Channels";
    @SuppressWarnings("CanBeFinal")
    public String Channels_NotEnoughPermission = "&cYou cant join this Channel. You don't have enough Permissions";
    @SuppressWarnings("CanBeFinal")
    public String Channels_PlayerCantWrite = "&cYou can't write into this Channel.";

    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Report_On = "&6Reporting has been enabled and the Report will be saved to %file. Type &c/cc:report&6 off to stop or it will automaticly stop after &a10&6 Minutes";
    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Report_Off = "&6Reporting is now &aoff";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Join_OnlyPlayers = "&cOnly Players can join Channels";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Join_ChannelDoesNotExist = "&cThis Channel does not exist";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Join_PasswordProtected = "&cThis Channel is password protected";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Join_WrongPassword = "&cThe password for this Channel is wrong";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Join_JoinedChannel = "&6You have joined Channel &a%channel";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Leave_OnlyPlayers = "&cOnly Players can leave Channels";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Leave_ChannelDoesNotExist = "&cThis Channel does not exist";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Leave_ForcedChannel = "&cYou can't leave a forced Channel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Leave_LeftChannel = "&6You have left Channel &a%channel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Leave_FocusChannel = "&6You now focus Channel &a%channel";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Focus_OnlyPlayers = "&cOnly Players can focus Channels";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Focus_ChannelDoesNotExist = "&cThis Channel does not exist";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Focus_NotIn = "&cYou can't focus a Channel where you aren't in";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Focus_FocusChannel = "&6You now focus Channel &a%channel";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_ListChannel_ArgumentMissing = "&6You now focus Channel &a%channel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_ListChannel_ChannelDoesNotExist = "&cThis Channel does not exist";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_ListChannel_Header = "&cCurrently in the Channel &a%channel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_ListChannel_Player = "%prefix%nick%suffix&r";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_List_Header = "&cPlayers currently online";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_List_Player = "%afk%prefix%nick%suffix&r";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_List_AFK = "&8[&cAFK&8]&r ";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Create_AlreadyExists = "&cThis channel already exists";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Create_ErrorInSave = "&cThere was an error in the creation of your Channel. Please report this to a Admin";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Create_CreatedChannel = "&6Channel &a%channel was created. Password is &a%password";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_OfflinePlayer = "&cYou can't invite offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_SecondArgumentError = "&cIf you are not ingame you must give a channel as second Argument";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_ChannelDoesNotExist = "&cThis Channel does not exist";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_CantInvite = "&cYou can't send invitations for this Channel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_SendInvitation = "&6You got an Invitation to the &a%channel&6 Channel. Join with &a/join{click:joinChannel} %channel %password";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channel_Invite_Notification = "&6You have sent an Invitation to &a%name";

    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Reload_Success = "&6Reloaded all Channels and IRC Config";

    @SuppressWarnings("CanBeFinal")
    public String Command_IRC_Reconnect_Success = "&6The IRC Bot was reconnected";
    @SuppressWarnings("CanBeFinal")
    public String Command_IRC_Mute_Success = "&6The IRC Bot has been muted";
    @SuppressWarnings("CanBeFinal")
    public String Command_IRC_UnMute_Success = "&6The IRC Bot has been unmuted";

    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Message_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Message_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Message_OfflinePlayer = "&c%nick: You can't message offline Players";

    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Mute_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Mute_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Mute_OfflinePlayer = "&c%nick: You can't mute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Mute_Success = "&6%nick: You have muted %muted";

    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Players_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Players_Header = "&6Current Players online (%count): ";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Players_Player = "%prefix%nick%suffix&r";

    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_NotEnoughPermissionForServer = "&c%nick: You don't have Permission to execute this on that Server";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_NotEnoughPermissionForCommand = "&c%nick: You don't have Permission to execute that Scmd Command";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_InvalidServer = "&c%nick: You don't have Permission to execute that Scmd Command";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_ServerIsEmpty = "&c%nick: Can't send the Command. It needs to be at least one Player online on the Server";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Scmd_CommandIssued = "&6Command has been issued";

    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Unmute_NotEnoughPermission = "&c%nick: You have not enough Permissions to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Unmute_NotEnoughArguments = "&c%nick: Not enough Arguments to execute this";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Unmute_OfflinePlayer = "&c%nick: You can't unmute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String IRC_Command_Unmute_Success = "&6%nick: You have unmuted %muted";

    @SuppressWarnings("CanBeFinal")
    public String IRC_PmSessionChangedTo = "!!! %from you are now talking with %to !!!";

    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Send_UnknownPlayer = "&cThis Player is not known. Can not send a Mail to it";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Send_Success = "&6Mail was sent";

    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Read_NotPlayer = "&cOnly Players can read mails";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Read_Header = "&6=== &aMails&6 ===";

    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Clear_NotPlayer = "&cOnly Players can clear mails";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_Clear_Success = "&6Mails where cleared";

    @SuppressWarnings("CanBeFinal")
    public String Command_Mail_HelpText = "&6Welcome to the Mail System\n&6Use &a/mail send <player> <message>&6 to send mails\n" +
            "&6Use &a/mail read &6 to read mails\n" +
            "&6Use &a/mail clear&6 to clear your mails";

    @SuppressWarnings("CanBeFinal")
    public String Mail_JoinMessage = "&6You have &a%size&6 unread Mail(s)";

    @SuppressWarnings("CanBeFinal")
    public String Command_Mute_NotPlayer = "&cOnly Players can mute";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mute_OfflinePlayer = "&cYou can't mute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mute_Success = "&6You muted %player";
    @SuppressWarnings("CanBeFinal")
    public String Command_Mute_CannotBeMuted = "&cYou can't mute this Player";

    @SuppressWarnings("CanBeFinal")
    public String Command_Unmute_NotPlayer = "&cOnly Players can unmute";
    @SuppressWarnings("CanBeFinal")
    public String Command_Unmute_OfflinePlayer = "&cYou can't unmute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Unmute_Success = "&6You unmuted %player";

    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Mute_OfflinePlayer = "&cYou can't mute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Mute_Success = "&6You muted %player";

    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Unmute_OfflinePlayer = "&cYou can't unmute offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_CC_Unmute_Success = "&6You unmuted %player";

    @SuppressWarnings("CanBeFinal")
    public String Command_Nick_NotPlayer = "&cOnly Players can change nicknames";
    @SuppressWarnings("CanBeFinal")
    public String Command_Nick_NoPermissionToChangeOther = "&cYou are not allowed to change Nicknames of other Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Nick_OfflinePlayer = "&cYou can not change Nicknames from offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Nick_ChangedNick = "&6Your nickname was changed to &a%nick";
    @SuppressWarnings("CanBeFinal")
    public String Command_Nick_ResetNick = "&6The Nickname has been removed";

    @SuppressWarnings("CanBeFinal")
    public String Command_Msg_NotPlayer = "&cOnly Players can send Messages";

    @SuppressWarnings("CanBeFinal")
    public String Command_Reply_NotPlayer = "&cOnly Players can reply on Messages";
    @SuppressWarnings("CanBeFinal")
    public String Command_Reply_NoConversation = "&cYou don't have a Conversation to reply to";

    @SuppressWarnings("CanBeFinal")
    public String Message_Sender = "&6You&8 -> &6%receiver&8:&7 %message";
    @SuppressWarnings("CanBeFinal")
    public String Message_Receiver = "&6%sender&8 -> &6You&8:&7 %message";
    @SuppressWarnings("CanBeFinal")
    public String Message_Spy = "&6%sender&8 -> &6%receiver&8:&7 %message";
    @SuppressWarnings("CanBeFinal")
    public String Message_Nick = "%prefix%nick%suffix";
    @SuppressWarnings("CanBeFinal")
    public String Message_OfflinePlayer = "&cYou can't message to offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Message_Self = "&cYou can't message with yourself";
    @SuppressWarnings("CanBeFinal")
    public String Message_NoIrcNick = "&cYou can not send PMs to IRC";
    @SuppressWarnings("CanBeFinal")
    public String Message_IrcNickNotOnline = "&cThis IRC Nick is not online";

    @SuppressWarnings("CanBeFinal")
    public String Playermenu_Complete = "%prefix%nick%suffix&8:&r {permission:cloudchat.command.mute; status:unmuted; message:&c&nMute[click]&r | ; command:/mute %player}{permission:cloudchat.command.unmute; status:muted; message:&a&nUnmute[click]&r | ; command:/unmute %player}{permission:cloudchat.command.msg; message:&6&nMessage[click]; mode: suggest; command:/msg %player}";

    @SuppressWarnings("CanBeFinal")
    public String Command_Realname_OfflinePlayer = "&cYou can not get the real Name of offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Realname_Success = "&6The Realname of %nick is &a%realname";

    @SuppressWarnings("CanBeFinal")
    public String Command_BroadCast_Message = "&c[&aBroadcast&c]&r %message";

    @SuppressWarnings("CanBeFinal")
    public String Command_SocialSpy_NotPlayer = "&cOnly Players can spy on PMs";
    @SuppressWarnings("CanBeFinal")
    public String Command_SocialSpy_Disabled = "&6You no longer spy on PMs";
    @SuppressWarnings("CanBeFinal")
    public String Command_SocialSpy_Enabled = "&6You now spy on PMs";

    @SuppressWarnings("CanBeFinal")
    public String Command_ChatSpy_NotPlayer = "&cOnly Players can spy on Chat";
    @SuppressWarnings("CanBeFinal")
    public String Command_ChatSpy_Disabled = "&6You no longer spy on Chat";
    @SuppressWarnings("CanBeFinal")
    public String Command_ChatSpy_Enabled = "&6You now spy on Chat";

    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NotPlayer = "&cOnly Players can clear Chat";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_InvalidMode = "&cInvalid clear mode. Only can be 'player' or 'server'";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NoPermissionForOther = "&cYou can not clear Chat of other Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NoPermissionForServer = "&cYou can not clear Chat of Servers";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NoPermissionForGlobal = "&cYou can not clear Chat of all Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NotEnoughArguments = "&cNot enough Arguments";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_ChatCleared = "&6Chat was cleared";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_PlayerOffline = "&cYou can not clear Chat from offline Players";
    @SuppressWarnings("CanBeFinal")
    public String Command_Clearchat_NoServer = "&cThis Server does not exist";

    @SuppressWarnings("CanBeFinal")
    public String Command_Channels_Header = "&4=== All Channels you can enter ===";
    @SuppressWarnings("CanBeFinal")
    public String Command_Channels_Channel = "&4%channel";

    @SuppressWarnings("CanBeFinal")
    public String Command_TogglePM_YouNowIgnore = "&6You now ignore PMs";
    @SuppressWarnings("CanBeFinal")
    public String Command_TogglePM_YouDontIgnore = "&6You don't ignore PMs";

    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_NotPlayer = "&cOnly Players can use conversations";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_SamePlayer = "&cYou can't start a conversation with yourself";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_TargetIsOffline = "&cThe conversation target is offline";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_NoTarget = "&cThe conversation target could not be found";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_GotRequest = "&6You got a conversation request from &a%sender&6. Type &a/conversation accept&6 to accept, &a/conversation cancel&6 to cancel";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_SendRequest = "&6The conversation request was sent.";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_TargetLeft = "&cThe conversation target left the Server. Please try again when its online again";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_RequestTimedOut = "&cThe conversation request timed out. Please try again later.";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_SenderLeft = "&cThe conversation request sender is offline. Please try again when its online again";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_NoRequest = "&cYou have no conversation request pending.";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_AcceptedTarget = "&6You now started a conversation with &a%target";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_AcceptedSender = "&6Your conversation was accepted";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_PlayerCanceled = "&cThe conversation was left or the opposite has canceled the conversation";
    @SuppressWarnings("CanBeFinal")
    public String Command_Conversation_NothingToCancel = "&cYou have no conversation to cancel";

    @SuppressWarnings("CanBeFinal")
    public String Conversation_Format = "&7[&aConversation&7] %nick: %message";

    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_NotPlayer = "&cOnly Players can use highlight";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Enabled = "&6Highlight is enabled";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Disabled = "&6Highlight is disabled";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Sound_Enabled = "&6Highlight sound has been enabled";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Sound_Disabled = "&6Highlight sound has been disabled";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Sound_Changed = "&6Highlight sound was changed to &a%sound";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Header = "&6=== All Highlights you have configured ===";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Removed = "&6Highlight pattern was removed";
    @SuppressWarnings("CanBeFinal")
    public String Command_Highlight_Added = "&6Highlight pattern was added";
}
