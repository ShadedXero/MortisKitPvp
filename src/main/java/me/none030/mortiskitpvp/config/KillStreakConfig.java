package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.killstreak.KillStreakManager;
import me.none030.mortiskitpvp.kitpvp.killstreak.KillStreakMilestone;
import me.none030.mortiskitpvp.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class KillStreakConfig extends Config {

    public KillStreakConfig(ConfigManager configManager) {
        super("killstreak.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        KillStreakMilestone intervalMilestone = loadIntervalMilestone(config.getConfigurationSection("interval-milestone"));
        getConfigManager().getManager().setKillStreakManager(new KillStreakManager(getConfigManager().getManager().getGameManager(), intervalMilestone));
        loadMilestones(config.getConfigurationSection("milestones"));
    }

    private KillStreakMilestone loadIntervalMilestone(ConfigurationSection section) {
        if (section == null) {
            return null;
        }
        int requirement = section.getInt("requirement");
        String rawMessage = section.getString("message");
        String message;
        if (rawMessage != null) {
            message = new MessageUtils(rawMessage).color();
        }else {
            message = null;
        }
        List<String> commands = section.getStringList("commands");
        return new KillStreakMilestone(requirement, message, commands);
    }

    private void loadMilestones(ConfigurationSection milestones) {
        if (milestones == null) {
            return;
        }
        for (String id : milestones.getKeys(false)) {
            ConfigurationSection section = milestones.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            int requirement = section.getInt("requirement");
            String rawMessage = section.getString("message");
            String message;
            if (rawMessage != null) {
                message = new MessageUtils(rawMessage).color();
            }else {
                message = null;
            }
            List<String> commands = section.getStringList("commands");
            KillStreakMilestone milestone = new KillStreakMilestone(requirement, message, commands);
            getConfigManager().getManager().getKillStreakManager().getMilestones().add(milestone);
        }
    }
}
