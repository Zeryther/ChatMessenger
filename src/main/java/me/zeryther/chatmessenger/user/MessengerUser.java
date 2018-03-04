package me.zeryther.chatmessenger.user;

import me.zeryther.chatmessenger.ChatMessengerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessengerUser {
    private transient Player p;
    private boolean socialSpyActive;

    public static MessengerUser getUser(Player p){
        if(ChatMessengerPlugin.USER_STORAGE.containsKey(p.getUniqueId().toString())){
            MessengerUser u = ChatMessengerPlugin.USER_STORAGE.get(p.getUniqueId().toString());

            u.getPlayer(p.getUniqueId().toString());

            return u;
        } else {
            MessengerUser u = new MessengerUser(p);

            ChatMessengerPlugin.USER_STORAGE.put(p.getUniqueId().toString(),u);

            return u;
        }
    }

    public MessengerUser(Player p){
        this.p = p;
    }

    public Player getPlayer(){
        return p;
    }

    public Player getPlayer(String uuid){
        if(p == null) p = Bukkit.getPlayer(UUID.fromString(uuid));

        return p;
    }

    public boolean isSocialSpyActive() {
        return socialSpyActive;
    }

    public void setSocialSpyActive(boolean socialSpyActive) {
        this.socialSpyActive = socialSpyActive;
    }
}
