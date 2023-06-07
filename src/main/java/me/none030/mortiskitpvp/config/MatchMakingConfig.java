package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import me.none030.mortiskitpvp.kitpvp.matchmakings.MatchMaking;
import me.none030.mortiskitpvp.kitpvp.matchmakings.MatchMakingManager;
import me.none030.mortiskitpvp.kitpvp.matchmakings.MatchMakingSettings;
import me.none030.mortiskitpvp.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchMakingConfig extends Config {

    public MatchMakingConfig(ConfigManager configManager) {
        super("matchmaking.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        MatchMakingSettings settings = loadMenu(config.getConfigurationSection("menu"));
        if (settings == null) {
            return;
        }
        List<MatchMaking> modes = loadMatchMaking(config.getConfigurationSection("matchmaking"));
        getConfigManager().getManager().setMatchMakingManager(new MatchMakingManager(getConfigManager().getManager().getGameManager(), settings, modes));
        getConfigManager().getManager().getMatchMakingManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private MatchMakingSettings loadMenu(ConfigurationSection menu) {
        if (menu == null) {
            return null;
        }
        int size = menu.getInt("size");
        MessageUtils menuTitle = new MessageUtils(menu.getString("menu-title"));
        ConfigurationSection section = menu.getConfigurationSection("modes");
        if (section == null) {
            return null;
        }
        HashMap<Integer, Integer> modeBySlot = new HashMap<>();
        for (String key : section.getKeys(false)) {
            int slot = section.getInt(key);
            int mode;
            try {
                mode = Integer.parseInt(key);
            }catch (IllegalArgumentException exp) {
                continue;
            }
            modeBySlot.put(slot, mode);
        }
        return new MatchMakingSettings(size, menuTitle.getMessage(), modeBySlot);
    }

    private List<MatchMaking> loadMatchMaking(ConfigurationSection matchMaking) {
        if (matchMaking == null) {
            return new ArrayList<>();
        }
        List<MatchMaking> modes = new ArrayList<>();
        for (String key : matchMaking.getKeys(false)) {
            ConfigurationSection section = matchMaking.getConfigurationSection(key);
            if (section == null) {
                continue;
            }
            int mode = section.getInt("mode");
            int min = section.getInt("min");
            int max = section.getInt("max");
            long waitTime = section.getLong("wait-time");
            String iconId = section.getString("icon");
            if (iconId == null) {
                continue;
            }
            ItemStack icon = getConfigManager().getManager().getItemManager().getItem(iconId);
            if (icon == null) {
                continue;
            }
            List<Arena> arenas = new ArrayList<>();
            for (String arenaId : section.getStringList("arenas")) {
                Arena arena = getConfigManager().getManager().getArenaManager().getArenaById().get(arenaId);
                if (arena == null) {
                    continue;
                }
                arenas.add(arena);
            }
            MatchMaking match = new MatchMaking(mode, min, max, waitTime, icon, arenas);
            modes.add(match);
        }
        return modes;
    }
}
