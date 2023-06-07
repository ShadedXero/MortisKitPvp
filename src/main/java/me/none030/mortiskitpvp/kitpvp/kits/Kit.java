package me.none030.mortiskitpvp.kitpvp.kits;

import me.none030.mortisheads.heads.Head;
import me.none030.mortiskitpvp.MortisKitPvp;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Kit {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final String id;
    private final String name;
    private final String permission;
    private final String iconId;
    private final ItemStack icon;
    private final Inventory inventory;
    private final List<PotionEffect> effects;

    public Kit(String id, String name, String permission, String iconId, ItemStack icon, Inventory inventory, List<PotionEffect> effects) {
        this.id = id;
        this.name = name;
        this.permission = permission;
        this.iconId = iconId;
        this.icon = icon;
        this.inventory = inventory;
        this.effects = effects;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

    public void give(Player player) {
        clearInventory(player);
        clearPotionEffects(player);
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            player.getInventory().setItem(i, item);
        }
        if (plugin.hasHeads()) {
            Head head = plugin.getHeads().getManager().getHeadManager().getHead(player.getInventory().getHelmet());
            if (head != null) {
                head.addEffects(plugin.getHeads().getManager().getHeadManager(), player);
            }
        }
        applyEffects(player);
    }

    private void applyEffects(Player player) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
    }

    private void clearInventory(Player player) {
        player.getInventory().clear();
    }

    private void clearPotionEffects(Player player) {
        player.getActivePotionEffects().clear();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getIconId() {
        return iconId;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }
}
