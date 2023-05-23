package me.none030.mortiskitpvp.kitpvp.matchmakings;

import me.none030.mortiskitpvp.utils.ItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MatchMakingMenu implements InventoryHolder {

    private final MatchMakingManager matchMakingManager;
    private final Inventory menu;
    private final MatchMakingSettings settings;

    public MatchMakingMenu(MatchMakingManager matchMakingManager, MatchMakingSettings settings) {
        this.matchMakingManager = matchMakingManager;
        this.settings = settings;
        this.menu = getMenu(settings.getMenuSize());
        create();
    }
    
    private Inventory getMenu(int size) {
        return Bukkit.createInventory(this, size, Component.text(settings.getMenuTitle()));
    }

    private void create() {
        ItemEditor filter = new ItemEditor(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        filter.setName(" ");
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, filter.getItem());
        }
        for (int slot : settings.getModeBySlot().keySet()) {
            int mode = settings.getModeBySlot().get(slot);
            MatchMaking matchMaking = matchMakingManager.getMode(mode);
            if (matchMaking == null) {
                continue;
            }
            menu.setItem(slot, matchMaking.getIcon());
        }
    }

    public void click(Player player, int slot) {
        if (!settings.getModeBySlot().containsKey(slot)) {
            return;
        }
        matchMakingManager.removePlayer(player);
        int mode = settings.getModeBySlot().get(slot);
        MatchMaking matchMaking = matchMakingManager.getMode(mode);
        if (matchMaking == null) {
            return;
        }
        matchMaking.addQueue(player);
        player.sendMessage(matchMakingManager.getMessage("ADDED_QUEUE"));
        close(player);
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public MatchMakingManager getMatchMakingManager() {
        return matchMakingManager;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }

    public MatchMakingSettings getSettings() {
        return settings;
    }
}
