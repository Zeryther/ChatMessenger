package com.zeryther.chatmessenger;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zeryther.chatmessenger.util.PermissionNode;

public class ChatMessengerPlugin extends JavaPlugin {

	private static ChatMessengerPlugin instance;
	public static ArrayList<Player> BLOCK_MSG = new ArrayList<Player>();
	public static HashMap<Player, Player> REPLY = new HashMap<Player, Player>();
	
	public void onEnable() {
		instance = this;
		
		registerCommands();
		saveDefaultConfig();
	}
	
	public static ChatMessengerPlugin getInstance(){
		return instance;
	}
	
	private void registerCommands(){
		new ChatMessengerExecutor();
	}
	
	public boolean maySendMessage(Player p, Player p2){
		boolean b = true;
		
		if(!p.hasPermission(PermissionNode.CMD_BLOCKMSG_EXEMPT)){
			if(BLOCK_MSG.contains(p2)){
				b = false;
			}
		}
		
		return b;
	}

}
