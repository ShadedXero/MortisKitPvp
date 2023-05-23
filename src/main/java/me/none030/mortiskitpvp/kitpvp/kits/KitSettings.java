package me.none030.mortiskitpvp.kitpvp.kits;

import org.bukkit.inventory.ItemStack;

public class KitSettings {

    private final int menuSize;
    private final ItemStack randomKit;
    private final int randomKitSlot;

    public KitSettings(int menuSize, ItemStack randomKit, int randomKitSlot) {
        this.menuSize = menuSize;
        this.randomKit = randomKit;
        this.randomKitSlot = randomKitSlot;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public ItemStack getRandomKit() {
        return randomKit;
    }

    public int getRandomKitSlot() {
        return randomKitSlot;
    }
}
