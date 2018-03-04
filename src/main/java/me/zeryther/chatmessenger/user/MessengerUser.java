package me.zeryther.chatmessenger.user;

import me.zeryther.chatmessenger.ChatMessengerPlugin;
import org.bukkit.entity.Player;

public class MessengerUser {
    private transient Player p;
    private boolean socialSpyActive;

    public static MessengerUser getUser(Player p){
        if(ChatMessengerPlugin.USER_STORAGE.containsKey(p.getUniqueId().toString())){
            return ChatMessengerPlugin.USER_STORAGE.get(p.getUniqueId().toString());
        } else {
            MessengerUser u = new MessengerUser(p);

            ChatMessengerPlugin.USER_STORAGE.put(p.getUniqueId().toString(),u);

            return u;
        }
    }

    public MessengerUser(Player p){
        this.p = p;
        System.out.println("NEW");
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
