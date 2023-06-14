package me.none030.mortiskitpvp.kitpvp.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KitListener implements Listener {

    private final KitManager kitManager;

    public KitListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof KitMenu)) {
            return;
        }
        e.setCancelled(true);
        KitMenu menu = (KitMenu) e.getClickedInventory().getHolder();
        menu.click(e.getRawSlot());
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof KitMenu)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (e.getFrom().getWorld().equals(e.getTo().getWorld())) {
            return;
        }
        kitManager.getKitIdByPlayer().remove(player);
    }
}
