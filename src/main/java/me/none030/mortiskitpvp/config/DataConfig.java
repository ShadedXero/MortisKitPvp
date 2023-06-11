package me.none030.mortiskitpvp.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataConfig extends Config {

    public DataConfig() {
        super("data/worlds.yml", null);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = new ArrayList<>(config.getStringList("worlds"));
        for (String worldName : worlds) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                getPlugin().getMultiverseAPI().getMVWorldManager().deleteWorld(worldName, true);
                remove(worldName);
                continue;
            }
            getPlugin().getMultiverseAPI().getMVWorldManager().deleteWorld(worldName, true);
            remove(world);
        }
    }

    public void add(World world) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = new ArrayList<>(config.getStringList("worlds"));
        worlds.add(world.getName());
        config.set("worlds", worlds);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void add(String worldName) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = new ArrayList<>(config.getStringList("worlds"));
        worlds.add(worldName);
        config.set("worlds", worlds);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void remove(World world) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = new ArrayList<>(config.getStringList("worlds"));
        worlds.remove(world.getName());
        config.set("worlds", worlds);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void remove(String worldName) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = new ArrayList<>(config.getStringList("worlds"));
        worlds.remove(worldName);
        config.set("worlds", worlds);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}
