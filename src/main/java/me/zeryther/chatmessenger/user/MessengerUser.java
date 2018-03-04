package me.zeryther.chatmessenger.user;

import me.zeryther.chatmessenger.ChatMessengerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessengerUser {
    private String uuid;
    private boolean socialSpyActive;

    public static MessengerUser getUser(Player p){
        for(MessengerUser u : ChatMessengerPlugin.USER_STORAGE){
            if(u.uuid.equals(p.getUniqueId().toString())) return u;
        }

        MessengerUser u = new MessengerUser(p);

        ChatMessengerPlugin.USER_STORAGE.add(u);

        return u;
    }

    public MessengerUser(Player p){
        this.uuid = p.getUniqueId().toString();
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public boolean isSocialSpyActive() {
        return socialSpyActive;
    }

    public void setSocialSpyActive(boolean socialSpyActive) {
        this.socialSpyActive = socialSpyActive;
    }
}
