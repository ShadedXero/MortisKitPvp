package me.none030.mortiskitpvp.kitpvp.killstreak;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KillStreakManager extends Manager {

    private final GameManager gameManager;
    private final List<KillStreakMilestone> milestones;
    private final HashMap<Player, Long> killStreakByPlayer;

    public KillStreakManager(GameManager gameManager) {
        this.gameManager = gameManager;
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
        KillStreakMilestone milestone = getMilestone(kills);
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
        if (milestone == null) {
            return;
        }
        milestone.executeCommands(player);
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

    public KillStreakMilestone getMilestone(long kills) {
        List<KillStreakMilestone> milestones = getMilestonesBelow(kills);
        KillStreakMilestone killStreakMilestone = null;
        for (KillStreakMilestone milestone : milestones) {
            long distance = (kills - milestone.getRequirement());
            if (killStreakMilestone == null || distance < (kills - killStreakMilestone.getRequirement())) {
                killStreakMilestone = milestone;
            }
        }
        return killStreakMilestone;
    }

    public List<KillStreakMilestone> getMilestonesBelow(long kills) {
        List<KillStreakMilestone> milestoneList = new ArrayList<>();
        for (KillStreakMilestone milestone : milestones) {
            if (milestone.hasRequirement(kills)) {
                milestoneList.add(milestone);
            }
        }
        return milestoneList;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public List<KillStreakMilestone> getMilestones() {
        return milestones;
    }

    public HashMap<Player, Long> getKillStreakByPlayer() {
        return killStreakByPlayer;
    }
}
