package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.battlefield.Battlefield;
import me.none030.mortiskitpvp.kitpvp.battlefield.BattlefieldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BattlefieldConfig extends Config {

    public BattlefieldConfig(ConfigManager configManager) {
        super("battlefield.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadBattlefield(config.getConfigurationSection("battlefield"));
    }

    private void loadBattlefield(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        String worldName = section.getString("world");
        if (worldName == null) {
           return;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        String rawSpawnPoints = section.getString("spawn");
        if (rawSpawnPoints == null) {
            return;
        }
        String[] rawSpawn = rawSpawnPoints.split(",");
        if (rawSpawn.length != 5) {
            return;
        }
        Location spawn = new Location(world, Double.parseDouble(rawSpawn[0]), Double.parseDouble(rawSpawn[1]), Double.parseDouble(rawSpawn[2]), Float.parseFloat(rawSpawn[3]), Float.parseFloat(rawSpawn[4]));
        String rawOriginProtected = section.getString("protected-origin");
        if (rawOriginProtected == null) {
            return;
        }
        String[] rawOrigin = rawOriginProtected.split(",");
        if (rawOrigin.length != 3) {
            return;
        }
        Location origin = new Location(world, Double.parseDouble(rawOrigin[0]), Double.parseDouble(rawOrigin[1]), Double.parseDouble(rawOrigin[2]));
        String rawEndProtected = section.getString("protected-end");
        if (rawEndProtected == null) {
            return;
        }
        String[] rawEnd = rawEndProtected.split(",");
        if (rawEnd.length != 3) {
            return;
        }
        Location end = new Location(world, Double.parseDouble(rawEnd[0]), Double.parseDouble(rawEnd[1]), Double.parseDouble(rawEnd[2]));
        String elytraId = section.getString("elytra");
        if (elytraId == null) {
            return;
        }
        ItemStack elytra = getConfigManager().getManager().getItemManager().getItem(elytraId);
        if (elytra == null) {
            return;
        }
        boolean durability = section.getBoolean("durability");
        boolean hunger = section.getBoolean("hunger");
        Battlefield battlefield = new Battlefield(world, spawn, origin, end, elytra, durability, hunger);
        getConfigManager().getManager().setBattlefieldManager(new BattlefieldManager(getConfigManager().getManager().getKitManager(), battlefield));
    }
}
