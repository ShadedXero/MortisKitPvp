package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.placeholderapi.PlaceholderManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlaceholderConfig extends Config {

    public PlaceholderConfig(ConfigManager configManager) {
        super("placeholders.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!getPlugin().hasPlaceholderAPI()) {
            return;
        }
        getConfigManager().getManager().setPlaceholderManager(new PlaceholderManager(getConfigManager().getManager().getKitManager(), getConfigManager().getManager().getKillStreakManager()));
        getConfigManager().getManager().getPlaceholderManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }
}
