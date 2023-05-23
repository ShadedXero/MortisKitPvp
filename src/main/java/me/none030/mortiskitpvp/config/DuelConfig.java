package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.duels.DuelManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DuelConfig extends Config {

    public DuelConfig(ConfigManager configManager) {
        super("duels.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadDuels(config.getConfigurationSection("duels"));
        getConfigManager().getManager().getDuelManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private void loadDuels(ConfigurationSection duels) {
        if (duels == null) {
            return;
        }
        long expireTime = duels.getLong("expire-time");
        getConfigManager().getManager().setDuelManager(new DuelManager(getConfigManager().getManager().getArenaManager(), getConfigManager().getManager().getGameManager(), expireTime));
    }
}
