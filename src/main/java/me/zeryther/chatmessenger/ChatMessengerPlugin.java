package me.zeryther.chatmessenger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.zeryther.chatmessenger.user.MessengerUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.zeryther.chatmessenger.util.PermissionNode;

public class ChatMessengerPlugin extends JavaPlugin {
	public static HashMap<String,MessengerUser> USER_STORAGE;

	private static ChatMessengerPlugin instance;
	public static ArrayList<Player> BLOCK_MSG = new ArrayList<Player>();
	public static HashMap<Player, Player> REPLY = new HashMap<Player, Player>();
	public static ArrayList<CommandSender> SOCIAL_SPY = new ArrayList<CommandSender>();

	public static final Gson GSON = new Gson();

	private static final String path = getInstance().getDataFolder().getAbsolutePath() + (getInstance().getDataFolder().getAbsolutePath().endsWith("/") ? "" : "/") + "userData.json";
	
	public void onEnable() {
		instance = this;
		
		registerCommands();
		registerListeners();
		saveDefaultConfig();

		try {
			File file = new File(path);
			if(file.exists()){
				USER_STORAGE = GSON.fromJson(new JsonReader(new FileReader(path)),new TypeToken<HashMap<String,MessengerUser>>(){}.getType());
			} else {
				USER_STORAGE = new HashMap<String,MessengerUser>();
				file.createNewFile();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onDisable(){
		saveData();
	}

	public static void saveData(){
		try(Writer writer = new FileWriter(path)){
			GSON.toJson(USER_STORAGE,writer);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static ChatMessengerPlugin getInstance(){
		return instance;
	}
	
	private void registerCommands(){
		new ChatMessengerExecutor();
	}

	private void registerListeners(){
		Bukkit.getPluginManager().registerEvents(new ChatMessengerListener(),this);
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
