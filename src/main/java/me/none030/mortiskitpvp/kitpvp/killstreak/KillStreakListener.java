package me.none030.mortiskitpvp.kitpvp.killstreak;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KillStreakListener implements Listener {

    private final KillStreakManager killStreakManager;

    public KillStreakListener(KillStreakManager killStreakManager) {
        this.killStreakManager = killStreakManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
    }
}
