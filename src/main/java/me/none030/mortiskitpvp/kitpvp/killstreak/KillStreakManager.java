package me.none030.mortiskitpvp.kitpvp.killstreak;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KillStreakManager extends Manager {

    private final GameManager gameManager;
    private final KillStreakMilestone intervalMilestone;
    private final List<KillStreakMilestone> milestones;
    private final HashMap<Player, Long> killStreakByPlayer;

    public KillStreakManager(GameManager gameManager, KillStreakMilestone intervalMilestone) {
        this.gameManager = gameManager;
        this.intervalMilestone = intervalMilestone;
        this.milestones = new ArrayList<>();
        this.killStreakByPlayer = new HashMap<>();
        MortisKitPvp plugin = MortisKitPvp.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new KillStreakListener(this), plugin);
    }

    public String getKillMessage(Player player) {
        Long kills = killStreakByPlayer.get(player);
        if (kills == null) {
            return null;
        }
        KillStreakMilestone milestone = getExactMilestone(kills);
        if (milestone == null) {
            return null;
        }
        return milestone.getMessage();
    }

    public void addKills(Player player) {
        Long kills = killStreakByPlayer.get(player);
        if (kills != null) {
            kills += 1;
            killStreakByPlayer.put(player, kills);
        }else {
            kills = 1L;
            killStreakByPlayer.put(player, kills);
        }
        KillStreakMilestone milestone = getExactMilestone(kills);
        if (milestone != null) {
            milestone.executeCommands(player);
        }
        String message = getKillMessage(player);
        if (message != null) {
            Bukkit.broadcast(Component.text(message.replace("%player_name%", player.getName())));
        }
        if (intervalMilestone != null) {
            double number = (double) kills / intervalMilestone.getRequirement();
            if (number % 1 == 0) {
                intervalMilestone.executeCommands(player);
                String intervalMessage = intervalMilestone.getMessage();
                if (intervalMessage != null) {
                    Bukkit.broadcast(Component.text(intervalMessage.replace("%player_name%", player.getName())));
                }
            }
        }
    }

    public void resetKills(Player player) {
        killStreakByPlayer.remove(player);
    }

    public KillStreakMilestone getExactMilestone(long kills) {
        for (KillStreakMilestone milestone : milestones) {
            if (milestone.isRequirement(kills)) {
                return milestone;
            }
        }
        return null;
    }
    public GameManager getGameManager() {
        return gameManager;
    }

    public KillStreakMilestone getIntervalMilestone() {
        return intervalMilestone;
    }

    public List<KillStreakMilestone> getMilestones() {
        return milestones;
    }

    public HashMap<Player, Long> getKillStreakByPlayer() {
        return killStreakByPlayer;
    }
}
