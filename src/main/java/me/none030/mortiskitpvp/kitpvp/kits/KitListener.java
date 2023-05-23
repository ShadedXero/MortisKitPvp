package me.none030.mortiskitpvp.kitpvp.kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class KitListener implements Listener {

    public KitListener() {
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
}
