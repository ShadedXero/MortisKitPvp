package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class GameConfig extends Config {

    public GameConfig(ConfigManager configManager) {
        super("game.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadGame(config.getConfigurationSection("game"));
        getConfigManager().getManager().getGameManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private void loadGame(ConfigurationSection game) {
        if (game == null) {
            return;
        }
        long length = game.getLong("length");
        long startTime = game.getLong("start-time");
        long endTime = game.getLong("end-time");
        getConfigManager().getManager().setGameManager(new GameManager(getConfigManager().getManager().getBattlefieldManager(), length, startTime, endTime));
    }
}
