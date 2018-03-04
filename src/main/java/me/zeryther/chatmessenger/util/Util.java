package me.zeryther.chatmessenger.util;

import java.util.regex.Pattern;

public class Util {
    public static boolean containsLink(String message){
        return Pattern.compile("^((https?|ftp):\\/\\/|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([\\/?].*)?$").matcher(message).find();
    }
}
