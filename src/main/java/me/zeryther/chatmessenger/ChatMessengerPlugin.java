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
	public static ArrayList<MessengerUser> USER_STORAGE;

	private static ChatMessengerPlugin instance;
	public static ArrayList<Player> BLOCK_MSG = new ArrayList<Player>();
	public static HashMap<Player, Player> REPLY = new HashMap<Player, Player>();
	public static ArrayList<CommandSender> SOCIAL_SPY = new ArrayList<CommandSender>();

	public static final Gson GSON = new Gson();

	private static String path;
	
	public void onEnable() {
		instance = this;

		path = getInstance().getDataFolder().getAbsolutePath() + (getInstance().getDataFolder().getAbsolutePath().endsWith("/") ? "" : "/") + "userData.json";
		
		registerCommands();
		registerListeners();
		saveDefaultConfig();

		getConfig().addDefault("cmd.msg.format.meTo","&6[&9I&6->&c%displayname%&6] &f%message%");
		getConfig().addDefault("cmd.msg.format.toMe","&6[&9%displayname%&6->&cme&6] &f%message%");
		getConfig().addDefault("cmd.msg.mayNotSendLinks","&cYou are not allowed to send links via private message.");
		getConfig().addDefault("cmd.msg.usage","&c/%label% <User> <Message>");

		getConfig().addDefault("cmd.reply.usage","&c/%label% <Message>");
		getConfig().addDefault("cmd.reply.noMessageSent","&cYou have to send a message before you can use /reply");

		getConfig().addDefault("cmd.blockmsg.activated","&cYou can no longer receive private messages!");
		getConfig().addDefault("cmd.blockmsg.deactivated","&aYou can now receive private messages again!");

		getConfig().addDefault("cmd.socialspy.on","&aSocial-Spy is now active!");
		getConfig().addDefault("cmd.socialspy.off","&cSocial-Spy is no longer active!");
		getConfig().addDefault("cmd.socialspy.msg","&6[&c%player1%&6->%c%player2%&6] &f%message%");

		getConfig().addDefault("player.notOnline","&cPlayer '%player%' is offline!");
		getConfig().addDefault("player.noPermission","&cYou do not have permission!");
		getConfig().addDefault("player.blockingMessages","&c%displayname% is blocking private messages!");

		try {
			File file = new File(path);
			if(file.exists()){
				USER_STORAGE = GSON.fromJson(new JsonReader(new FileReader(path)),new TypeToken<ArrayList<MessengerUser>>(){}.getType());
			} else {
				file.createNewFile();
			}
		} catch(Exception e){
			System.out.println("An error occurred trying to read userData.json, defaulting to empty storage.");
		}

		if(USER_STORAGE == null) USER_STORAGE = new ArrayList<MessengerUser>();
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
