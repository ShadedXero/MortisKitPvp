package me.none030.mortiskitpvp.kitpvp.matchmakings;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MatchMakingListener implements Listener {

    private final MatchMakingManager matchMakingManager;

    public MatchMakingListener(MatchMakingManager matchMakingManager) {
        this.matchMakingManager = matchMakingManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        matchMakingManager.removePlayer(player);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof MatchMakingMenu)) {
            return;
        }
        e.setCancelled(true);
        MatchMakingMenu menu = (MatchMakingMenu) e.getClickedInventory().getHolder();
        menu.click(player, e.getRawSlot());
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof MatchMakingMenu)) {
            return;
        }
        e.setCancelled(true);
    }
}