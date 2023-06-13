package me.none030.mortiskitpvp.kitpvp.killstreak;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class  KillStreakMilestone {

    private final long requirement;
    private final String message;
    private final List<String> commands;

    public KillStreakMilestone(long requirement, String message, List<String> commands) {
        this.requirement = requirement;
        this.message = message;
        this.commands = commands;
    }

    public void executeCommands(Player player) {
        for (String rawCommand : commands) {
            String command = rawCommand.replace("%player_name%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public boolean isRequirement(long kills) {
        return this.requirement == kills;
    }

    public long getRequirement() {
        return requirement;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getCommands() {
        return commands;
    }
}
