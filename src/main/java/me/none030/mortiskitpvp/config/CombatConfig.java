package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.combat.CombatManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CombatConfig extends Config {

    public CombatConfig(ConfigManager configManager) {
        super("combat.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadCombat(config.getConfigurationSection("combat"));
        getConfigManager().getManager().getCombatManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private void loadCombat(ConfigurationSection combat) {
        if (combat == null) {
            return;
        }
        long resetTime = combat.getLong("reset-time");
        getConfigManager().getManager().setCombatManager(new CombatManager(getConfigManager().getManager().getBattlefieldManager(), getConfigManager().getManager().getKillStreakManager(), resetTime));
    }
}
