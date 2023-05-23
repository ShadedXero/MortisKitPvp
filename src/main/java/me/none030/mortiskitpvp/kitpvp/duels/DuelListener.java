package me.none030.mortiskitpvp.kitpvp.duels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelListener implements Listener {

    private final DuelManager duelManager;

    public DuelListener(DuelManager duelManager) {
        this.duelManager = duelManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        duelManager.removePlayer(player);
    }
}
