package me.zeryther.chatmessenger.user;

import me.zeryther.chatmessenger.ChatMessengerPlugin;
import org.bukkit.entity.Player;

public class MessengerUser {
    private transient Player p;
    private boolean socialSpyActive = false;

    public static MessengerUser getUser(Player p){
        return ChatMessengerPlugin.USER_STORAGE.getOrDefault(p.getUniqueId().toString(),ChatMessengerPlugin.USER_STORAGE.put(p.getUniqueId().toString(),new MessengerUser(p)));
    }

    public MessengerUser(Player p){
        this.p = p;
    }

    public Player getPlayer(){
        return p;
    }

    public boolean isSocialSpyActive() {
        return socialSpyActive;
    }

    public void setSocialSpyActive(boolean socialSpyActive) {
        this.socialSpyActive = socialSpyActive;
    }
}
