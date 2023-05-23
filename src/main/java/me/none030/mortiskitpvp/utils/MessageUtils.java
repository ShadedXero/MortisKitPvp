package me.none030.mortiskitpvp.utils;

import org.bukkit.ChatColor;

import java.util.Objects;

public class MessageUtils {

    private String message;

    public MessageUtils(String message) {
        this.message = Objects.requireNonNullElse(message, "");
    }

    public String color() {
        String colored = ChatColor.translateAlternateColorCodes('&', message);
        setMessage(colored);
        return colored;
    }

    public void replace(String value, String replacement) {
        setMessage(message.replace(value, replacement));
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
