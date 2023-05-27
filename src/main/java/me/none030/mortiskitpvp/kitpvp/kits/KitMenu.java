package me.none030.mortiskitpvp.kitpvp.kits;

import me.none030.mortiskitpvp.utils.ItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitMenu implements InventoryHolder {

    private final KitManager kitManager;
    private final Player player;
    private final Inventory menu;
    private final List<String> kitIds;

    public KitMenu(KitManager kitManager, Player player) {
        this.kitManager = kitManager;
        this.player = player;
        this.menu = getMenu();
        this.kitIds = new ArrayList<>();
        create();
        setKits();
    }

    private Inventory getMenu() {
        String title = kitManager.getMessage("MENU_TITLE");
        if (title == null) {
            title = " ";
        }
        return Bukkit.createInventory(this, kitManager.getSettings().getMenuSize(), Component.text(title));
    }

    private void create() {
        ItemEditor filter = new ItemEditor(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        filter.setName(" ");
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, filter.getItem());
        }
        ItemStack randomKit = kitManager.getSettings().getRandomKit();
        if (randomKit == null) {
            return;
        }
        menu.setItem(kitManager.getSettings().getRandomKitSlot(), randomKit);
    }

    private void setKits() {
        List<Kit> kits = kitManager.getAccessibleKits(player);
        for (int i = 0; i < menu.getSize(); i++) {
            if (i >= kits.size()) {
                break;
            }
            Kit kit = kits.get(i);
            kitIds.add(kit.getId());
            menu.setItem(i, kit.getIcon());
        }
    }

    public void click(int slot) {
        if (slot == kitManager.getSettings().getRandomKitSlot()) {
            Kit kit = kitManager.getRandomKit(player);
            if (kit == null) {
                return;
            }
            kitManager.setKit(player, kit.getId());
            player.sendMessage(kitManager.getMessage("KIT_CHANGED"));
            close(player);
            return;
        }
        if (slot >= kitIds.size()) {
            return;
        }
        String kitId = kitIds.get(slot);
        kitManager.setKit(player, kitId);
        player.sendMessage(kitManager.getMessage("KIT_CHANGED"));
        close(player);
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }
}
