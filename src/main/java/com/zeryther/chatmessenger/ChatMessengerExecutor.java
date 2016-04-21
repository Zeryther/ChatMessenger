package com.zeryther.chatmessenger;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.zeryther.chatmessenger.util.PermissionNode;

public class ChatMessengerExecutor implements CommandExecutor {
	
	public ChatMessengerExecutor() {
		ChatMessengerPlugin.getInstance().getCommand("msg").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("reply").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("blockmsg").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("msg")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				
				if(p.hasPermission(PermissionNode.CMD_MSG)){
					if(args.length >= 2){
						if(Bukkit.getPlayer(args[0]) != null){
							Player p2 = Bukkit.getPlayer(args[0]);
							
							if(ChatMessengerPlugin.getInstance().maySendMessage(p, p2)){
								StringBuilder sb = new StringBuilder();
								for (int i = 1; i < args.length; i++) {
									sb.append(" ").append(args[i]);
								}
								String message = sb.toString().substring(1);
								
								if(p.hasPermission(PermissionNode.CMD_MSG_COLOR)) message = ChatColor.translateAlternateColorCodes('&', message);
								
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.meTo").replace("%displayname%", p2.getDisplayName()).replace("%message%", message)));
								p2.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.toMe").replace("%displayname%", p.getDisplayName()).replace("%message%", message)));
								
								if(ChatMessengerPlugin.REPLY.containsKey(p)) ChatMessengerPlugin.REPLY.remove(p);
								ChatMessengerPlugin.REPLY.put(p, p2);
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.blockingMessages").replace("%player%", args[0])));
							}
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.notOnline").replace("%player%", args[0])));
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.usage")).replace("%label%", label));
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.noPermission")));
				}
			} else if(sender instanceof CommandBlock){
				CommandBlock block = (CommandBlock)sender;
				
				if((args.length >= 2) && (Bukkit.getPlayer(args[0]) != null)){
					Player p2 = Bukkit.getPlayer(args[0]);
					
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						sb.append(" ").append(args[i]);
					}
					String message = ChatColor.translateAlternateColorCodes('&', sb.toString().substring(1));
					
					p2.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.toMe").replace("%displayname%", "&c[CommandBlock] &r" + block.getName()).replace("%message%", message)));
				}
			} else if(sender instanceof ConsoleCommandSender){
				ConsoleCommandSender console = (ConsoleCommandSender)sender;
				
				if(args.length >= 2){
					if(Bukkit.getPlayer(args[0]) != null){
						Player p2 = Bukkit.getPlayer(args[0]);
						
						StringBuilder sb = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							sb.append(" ").append(args[i]);
						}
						
						String message = ChatColor.translateAlternateColorCodes('&', sb.toString().substring(1));
						
						console.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.meTo").replace("%displayname%", p2.getDisplayName()).replace("%message%", message)));
			            p2.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.toMe").replace("%displayname%", "&dCONSOLE").replace("%message%", message)));
					} else {
						console.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.notOnline").replace("%player%", args[0])));
					}
				} else {
					console.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.usage")).replace("%label%", label));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid type.");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("reply")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission(PermissionNode.CMD_REPLY)){
					if(args.length == 0){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.reply.usage")).replace("%label%", label));
					} else {
						if(ChatMessengerPlugin.REPLY.containsKey(p)){
							String p2 = ChatMessengerPlugin.REPLY.get(p).getName();
							
							if(Bukkit.getPlayer(p2) != null){
								StringBuilder sb = new StringBuilder();
								for (int i = 1; i < args.length; i++) {
									sb.append(" ").append(args[i]);
								}
								String message = sb.toString().substring(1);
								
								p.performCommand("msg " + p2 + " " + message);
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.notOnline").replace("%player%", p2)));
							}
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.reply.noMessageSent")));
						}
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.noPermission")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid type.");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("blockmsg")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				
				if(p.hasPermission(PermissionNode.CMD_BLOCKMSG)){
					if(ChatMessengerPlugin.BLOCK_MSG.contains(p)){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.blockmsg.deactivated")));
						ChatMessengerPlugin.BLOCK_MSG.remove(p);
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.blockmsg.activated")));
			            ChatMessengerPlugin.BLOCK_MSG.remove(p);
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.noPermission")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid type.");
			}
		}
		
		return true;
	}

}
