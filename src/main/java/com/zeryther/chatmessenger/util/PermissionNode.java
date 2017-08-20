package com.zeryther.chatmessenger.util;

public class PermissionNode {

	public static final String CMD_MSG;
	public static final String CMD_MSG_COLOR;
	public static final String CMD_REPLY;
	public static final String CMD_BLOCKMSG;
	public static final String CMD_BLOCKMSG_EXEMPT;
	public static final String CMD_SOCIALSPY;
	public static final String CMD_CHATRELOAD;
	
	static {
		CMD_MSG = "chatmessenger.cmd.msg";
		CMD_MSG_COLOR = "chatmessenger.cmd.msg.color";
		CMD_REPLY = "chatmessenger.cmd.reply";
		CMD_BLOCKMSG = "chatmessenger.cmd.blockmsg";
		CMD_BLOCKMSG_EXEMPT = "chatmessenger.cmd.blockmsg.exempt";
		CMD_SOCIALSPY = "chatmessenger.cmd.socialspy";
		CMD_CHATRELOAD = "chatmessenger.cmd.chatreload";
	}

}
