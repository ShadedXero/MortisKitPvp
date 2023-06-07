package me.none030.mortiskitpvp.kitpvp.kits;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class KitSettings {

    private final int menuSize;
    private final ItemStack randomKit;
    private final int randomKitSlot;
    private final HashMap<String, Integer> slotByKitId;

    public KitSettings(int menuSize, ItemStack randomKit, int randomKitSlot, HashMap<String, Integer> slotByKitId) {
        this.menuSize = menuSize;
        this.randomKit = randomKit;
        this.randomKitSlot = randomKitSlot;
        this.slotByKitId = slotByKitId;
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

    public HashMap<String, Integer> getSlotByKitId() {
        return slotByKitId;
    }
}
