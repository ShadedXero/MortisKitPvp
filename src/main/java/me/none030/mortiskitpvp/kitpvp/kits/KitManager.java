package me.none030.mortiskitpvp.kitpvp.kits;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.config.KitConfig;
import me.none030.mortiskitpvp.kitpvp.Manager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class KitManager extends Manager {

    private final KitConfig kitConfig;
    private final KitSettings settings;
    private final HashMap<String, Kit> kitById;
    private final HashMap<Player, String> kitIdByPlayer;

    public KitManager(KitConfig kitConfig, KitSettings settings) {
        this.kitConfig = kitConfig;
        this.settings = settings;
        this.kitById = new HashMap<>();
        this.kitIdByPlayer = new HashMap<>();
        MortisKitPvp plugin = MortisKitPvp.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new KitListener(), plugin);
    }

    public void addKit(Kit kit) {
        kitById.put(kit.getId(), kit);
        kitConfig.addKit(kit);
    }

    public void removeKit(Kit kit) {
        kitById.remove(kit.getId());
        kitConfig.removeKit(kit.getId());
    }

    public void setKit(Player player, String kitId) {
        kitIdByPlayer.put(player, kitId);
    }

    public Kit getKit(Player player) {
        String kitId = kitIdByPlayer.get(player);
        if (kitId == null) {
            return null;
        }
        return kitById.get(kitId);
    }

    public Kit getRandomKit(Player player) {
        List<Kit> kits = getAccessibleKits(player);
        if (kits.size() == 0) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(0, kits.size());
        return kits.get(index);
    }

    public List<Kit> getAccessibleKits(Player player) {
        List<Kit> kitList = new ArrayList<>();
        for (Kit kit : getKits()) {
            if (kit.hasPermission(player)) {
                kitList.add(kit);
            }
        }
        return kitList;
    }

    public List<Kit> getKits() {
        return new ArrayList<>(kitById.values());
    }

    public KitConfig getKitConfig() {
        return kitConfig;
    }

    public KitSettings getSettings() {
        return settings;
    }

    public HashMap<String, Kit> getKitById() {
        return kitById;
    }

    public HashMap<Player, String> getKitIdByPlayer() {
        return kitIdByPlayer;
    }
}
