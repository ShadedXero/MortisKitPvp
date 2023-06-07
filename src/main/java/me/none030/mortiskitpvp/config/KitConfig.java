package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import me.none030.mortiskitpvp.kitpvp.kits.KitSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitConfig extends Config {

    public KitConfig(ConfigManager configManager) {
        super("kits.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        KitSettings settings = loadSettings(config.getConfigurationSection("menu"));
        if (settings == null) {
            return;
        }
        getConfigManager().getManager().setKitManager(new KitManager(this, settings));
        loadKits(config.getConfigurationSection("kits"));
        getConfigManager().getManager().getKitManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private KitSettings loadSettings(ConfigurationSection menu) {
        if (menu == null) {
            return null;
        }
        int size = menu.getInt("size");
        String randomKitIconId = menu.getString("random-kit-icon");
        if (randomKitIconId == null) {
            return null;
        }
        ItemStack randomKitIcon = getConfigManager().getManager().getItemManager().getItem(randomKitIconId);
        if (randomKitIcon == null) {
            return null;
        }
        int randomKitSlot = menu.getInt("random-kit-slot");
        ConfigurationSection section = menu.getConfigurationSection("kit-slots");
        if (section == null) {
            return null;
        }
        HashMap<String, Integer> slotByKitId = new HashMap<>();
        for (String kitId : section.getKeys(false)) {
            if (kitId == null) {
                continue;
            }
            int slot = section.getInt(kitId);
            slotByKitId.put(kitId, slot);
        }
        return new KitSettings(size, randomKitIcon, randomKitSlot, slotByKitId);
    }

    private void loadKits(ConfigurationSection kits) {
        if (kits == null) {
            return;
        }
        for (String id : kits.getKeys(false)) {
            ConfigurationSection section = kits.getConfigurationSection(id);
            if (section == null) {
               continue;
            }
            String name = section.getString("name");
            String permission = section.getString("permission");
            String iconId = section.getString("icon");
            if (iconId == null) {
                continue;
            }
            ItemStack icon = getConfigManager().getManager().getItemManager().getItem(iconId);
            if (icon == null) {
                continue;
            }
            String inventoryData = section.getString("inventory");
            if (inventoryData == null) {
                continue;
            }
            Inventory inventory = deserializeInventory(inventoryData);
            if (inventory == null) {
                continue;
            }
            List<PotionEffect> effectList = new ArrayList<>();
            ConfigurationSection effects = section.getConfigurationSection("effects");
            if (effects != null) {
                for (String effectId : section.getKeys(false)) {
                    ConfigurationSection effectSection = section.getConfigurationSection(effectId);
                    if (effectSection == null) {
                        continue;
                    }
                    PotionEffectType type = PotionEffectType.getByName(effectId);
                    if (type == null) {
                        continue;
                    }
                    int duration = effectSection.getInt("duration");
                    int amplifier = effectSection.getInt("amplifier");
                    PotionEffect effect = new PotionEffect(type, duration, amplifier);
                    effectList.add(effect);
                }
            }
            Kit kit = new Kit(id, name, permission, iconId, icon, inventory, effectList);
            getConfigManager().getManager().getKitManager().getKits().add(kit);
            getConfigManager().getManager().getKitManager().getKitById().put(id, kit);
        }
    }

    public void addKit(Kit kit) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection kits = config.getConfigurationSection("kits");
        if (kits == null) {
            kits = config.createSection("kits");
        }
        ConfigurationSection kitSection = kits.createSection(kit.getId());
        kitSection.set("name", kit.getName());
        kitSection.set("permission", kit.getPermission());
        kitSection.set("icon", kit.getIconId());
        kitSection.set("inventory", serializeInventory(kit.getInventory()));
        if (kit.getEffects().size() != 0) {
            ConfigurationSection effects = kitSection.createSection("effects");
            for (PotionEffect effect : kit.getEffects()) {
                ConfigurationSection effectSection = effects.createSection(effect.getType().getName());
                effectSection.set("duration", effect.getDuration());
                effectSection.set("amplifier", effect.getAmplifier());
            }
        }
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void removeKit(String kitId) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection kits = config.getConfigurationSection("kits");
        if (kits == null) {
            return;
        }
        kits.set(kitId, null);
    }
}
