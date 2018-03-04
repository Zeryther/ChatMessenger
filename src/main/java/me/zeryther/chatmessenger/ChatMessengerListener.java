package me.zeryther.chatmessenger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatMessengerListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        ChatMessengerPlugin.saveData();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

    }
}
