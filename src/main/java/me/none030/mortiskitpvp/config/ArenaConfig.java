package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import me.none030.mortiskitpvp.kitpvp.arenas.ArenaGameRule;
import me.none030.mortiskitpvp.kitpvp.arenas.BooleanGameRule;
import me.none030.mortiskitpvp.kitpvp.arenas.IntegerGameRule;
import me.none030.mortiskitpvp.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArenaConfig extends Config {

    public ArenaConfig(ConfigManager configManager) {
        super("arenas.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadArenas(config.getConfigurationSection("arenas"));
    }

    private void loadArenas(ConfigurationSection arenas) {
        if (arenas == null) {
            return;
        }
        for (String id : arenas.getKeys(false)) {
            ConfigurationSection section = arenas.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            MessageUtils name = new MessageUtils(section.getString("name"));
            MessageUtils author = new MessageUtils(section.getString("author"));
            String worldName = section.getString("world");
            if (worldName == null) {
                continue;
            }
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }
            List<Location> redSpawns = new ArrayList<>();
            for (String rawRed : section.getStringList("red-spawns")) {
                String[] rawRedSpawn = rawRed.split(",");
                if (rawRedSpawn.length != 5) {
                    continue;
                }
                Location redSpawn = new Location(world, Double.parseDouble(rawRedSpawn[0]), Double.parseDouble(rawRedSpawn[1]), Double.parseDouble(rawRedSpawn[2]), Float.parseFloat(rawRedSpawn[3]), Float.parseFloat(rawRedSpawn[4]));
                redSpawns.add(redSpawn);
            }
            List<Location> blueSpawns = new ArrayList<>();
            for (String rawBlue : section.getStringList("blue-spawns")) {
                String[] rawBlueSpawn = rawBlue.split(",");
                if (rawBlueSpawn.length != 5) {
                    continue;
                }
                Location blueSpawn = new Location(world, Double.parseDouble(rawBlueSpawn[0]), Double.parseDouble(rawBlueSpawn[1]), Double.parseDouble(rawBlueSpawn[2]), Float.parseFloat(rawBlueSpawn[3]), Float.parseFloat(rawBlueSpawn[4]));
                blueSpawns.add(blueSpawn);
            }
            String rawSpectate = section.getString("spectate");
            if (rawSpectate == null) {
                continue;
            }
            String[] rawSpectateSpawn = rawSpectate.split(",");
            if (rawSpectateSpawn.length != 5) {
                continue;
            }
            Location spectate = new Location(world, Double.parseDouble(rawSpectateSpawn[0]), Double.parseDouble(rawSpectateSpawn[1]), Double.parseDouble(rawSpectateSpawn[2]), Float.parseFloat(rawSpectateSpawn[3]), Float.parseFloat(rawSpectateSpawn[4]));
            boolean lavaInstantKill = section.getBoolean("lava-instant-kill");
            boolean waterInstantKill = section.getBoolean("water-instant-kill");
            boolean durability = section.getBoolean("durability");
            boolean hunger = section.getBoolean("hunger");
            boolean dropping = section.getBoolean("dropping");
            boolean picking = section.getBoolean("picking");
            boolean breaking = section.getBoolean("breaking");
            boolean placing = section.getBoolean("placing");
            boolean redstone = section.getBoolean("redstone");
            boolean doors = section.getBoolean("doors");
            boolean chest = section.getBoolean("chest");
            List<ArenaGameRule> gameRules = new ArrayList<>();
            for (String rawGameRule : section.getStringList("game-rules")) {
                String[] raw = rawGameRule.split(":");
                if (raw.length != 2) {
                    continue;
                }
                GameRule<?> gameRule = GameRule.getByName(raw[0]);
                if (gameRule == null) {
                    continue;
                }
                if (gameRule.getType().equals(Boolean.class)) {
                    boolean value = Boolean.parseBoolean(raw[1]);
                    BooleanGameRule booleanGameRule = new BooleanGameRule((GameRule<Boolean>) gameRule, value);
                    gameRules.add(booleanGameRule);
                }else {
                    int value;
                    try {
                        value = Integer.parseInt(raw[1]);
                    }catch (NumberFormatException exp) {
                        continue;
                    }
                    IntegerGameRule integerGameRule = new IntegerGameRule((GameRule<Integer>) gameRule, value);
                    gameRules.add(integerGameRule);
                }
            }
            Arena arena = new Arena(id, name.getMessage(), author.getMessage(), worldName, redSpawns, blueSpawns, spectate, lavaInstantKill, waterInstantKill, durability, hunger, dropping, picking, breaking, placing, redstone, doors, chest, gameRules);
            getConfigManager().getManager().getArenaManager().getArenas().add(arena);
            getConfigManager().getManager().getArenaManager().getArenaById().put(id, arena);
        }
    }
}
