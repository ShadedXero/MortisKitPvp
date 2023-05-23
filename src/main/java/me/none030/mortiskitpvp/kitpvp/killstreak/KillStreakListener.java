package me.none030.mortiskitpvp.kitpvp.killstreak;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class KillStreakListener implements Listener {

    private final KillStreakManager killStreakManager;

    public KillStreakListener(KillStreakManager killStreakManager) {
        this.killStreakManager = killStreakManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
        Player killer = player.getKiller();
        if (killer == null) {
            return;
        }
        killStreakManager.addKills(killer);
        String message = killStreakManager.getKillMessage(killer);
        if (message == null) {
            return;
        }
        Bukkit.broadcast(Component.text(message.replace("%player_name%", killer.getName())));
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        killStreakManager.resetKills(player);
    }
}
