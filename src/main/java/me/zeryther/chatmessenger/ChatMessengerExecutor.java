package me.zeryther.chatmessenger;

import me.zeryther.chatmessenger.user.MessengerUser;
import me.zeryther.chatmessenger.util.Util;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.zeryther.chatmessenger.util.PermissionNode;

public class ChatMessengerExecutor implements CommandExecutor {
	
	public ChatMessengerExecutor() {
		ChatMessengerPlugin.getInstance().getCommand("msg").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("reply").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("blockmsg").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("socialspy").setExecutor(this);
		ChatMessengerPlugin.getInstance().getCommand("chatreload").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("chatreload")){
			if(sender.hasPermission(PermissionNode.CMD_CHATRELOAD)){
				ChatMessengerPlugin.getInstance().reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "The config has been reloaded!");
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.noPermission")));
			}
		}

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

								if(Util.containsLink(message) && !p.hasPermission(PermissionNode.CMD_MSG_SEND_LINKS)){
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.mayNotSendLinks")));
									return true;
								}
								
								if(p.hasPermission(PermissionNode.CMD_MSG_COLOR)) message = ChatColor.translateAlternateColorCodes('&', message);
								
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.meTo").replace("%displayname%", p2.getDisplayName()).replace("%message%", message).replace("%name%",p2.getName())));
								p2.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.toMe").replace("%displayname%", p.getDisplayName()).replace("%message%", message).replace("%name%",p.getName())));

								for(MessengerUser spy : ChatMessengerPlugin.USER_STORAGE.values()){
									if(spy.isSocialSpyActive()) if(spy.getPlayer() != p) spy.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.socialspy.msg").replace("%player1%", p.getDisplayName()).replace("%player2%", p2.getDisplayName()).replace("%message%", message).replace("%name1%",p.getName()).replace("%name2%",p2.getName())));
								}
								
								if(ChatMessengerPlugin.REPLY.containsKey(p2)) ChatMessengerPlugin.REPLY.remove(p2);
								ChatMessengerPlugin.REPLY.put(p2, p);
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
						
						console.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.meTo").replace("%displayname%", p2.getDisplayName()).replace("%message%", message).replace("%name%",p2.getName())));
			            p2.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.msg.format.toMe").replace("%displayname%", "&dCONSOLE").replace("%message%", message).replace("%name%","CONSOLE")));
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

		if(cmd.getName().equalsIgnoreCase("socialspy")){
			if(sender.hasPermission(PermissionNode.CMD_SOCIALSPY)){
				if(sender instanceof Player){
					MessengerUser u = MessengerUser.getUser((Player)sender);

					if(u.isSocialSpyActive()){
						// Social Spy is on

						u.setSocialSpyActive(false);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.socialspy.off")));
					} else {
						// Social Spy is off

						u.setSocialSpyActive(true);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.socialspy.on")));
					}
				} else {
					if(ChatMessengerPlugin.SOCIAL_SPY.contains(sender)){
						// Social Spy is on

						ChatMessengerPlugin.SOCIAL_SPY.remove(sender);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.socialspy.off")));
					} else {
						// Social Spy is off

						ChatMessengerPlugin.SOCIAL_SPY.add(sender);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("cmd.socialspy.on")));
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatMessengerPlugin.getInstance().getConfig().getString("player.noPermission")));
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
								for (int i = 0; i < args.length; i++) {
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
			            ChatMessengerPlugin.BLOCK_MSG.add(p);
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
