package me.none030.mortiskitpvp.kitpvp.arenas;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.none030.mortiskitpvp.MortisKitPvp;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Arena {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final String id;
    private final String name;
    private final String author;
    private final String worldName;
    private final List<Location> redSpawns;
    private final List<Location> blueSpawns;
    private final Location spectate;
    private final boolean lavaInstantKill;
    private final boolean waterInstantKill;
    private final boolean durability;
    private final boolean hunger;
    private final boolean dropping;
    private final boolean picking;
    private final boolean breaking;
    private final boolean placing;
    private final boolean redstone;
    private final boolean doors;
    private final boolean chest;
    private final List<ArenaGameRule> gameRules;

    public Arena(String id, String name, String author, String worldName, List<Location> redSpawns, List<Location> blueSpawns, Location spectate, boolean lavaInstantKill, boolean waterInstantKill, boolean durability, boolean hunger, boolean dropping, boolean picking, boolean breaking, boolean placing, boolean redstone, boolean doors, boolean chest, List<ArenaGameRule> gameRules) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.worldName = worldName;
        this.redSpawns = redSpawns;
        this.blueSpawns = blueSpawns;
        this.spectate = spectate;
        this.lavaInstantKill = lavaInstantKill;
        this.waterInstantKill = waterInstantKill;
        this.durability = durability;
        this.hunger = hunger;
        this.dropping = dropping;
        this.picking = picking;
        this.breaking = breaking;
        this.placing = placing;
        this.redstone = redstone;
        this.doors = doors;
        this.chest = chest;
        this.gameRules = gameRules;
        unloadWorld();
    }

    private void reset(Player player) {
        if (plugin.hasHeads()) {
            plugin.getHeads().getManager().getHeadManager().removeTaskId(player);
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAbsorptionAmount(0);
        player.damage(0.0001);
        player.setFallDistance(0);
    }

    public void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        reset(player);
    }

    public void resetSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        reset(player);
    }

    private void unloadWorld() {
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.unloadWorld(worldName, true);
    }

    public World create() {
        String newName = UUID.randomUUID().toString();
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.cloneWorld(worldName, newName);
        World world = Bukkit.getWorld(newName);
        for (ArenaGameRule gameRule : gameRules) {
            gameRule.setGameRule(world);
        }
        return world;
    }

    public void delete(World world) {
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.deleteWorld(world.getName(), true, true);
        if (plugin.hasWorldGuard()) {
            try {
                FileUtils.forceDelete(new File(WorldGuardPlugin.inst().getDataFolder() + "/worlds/", world.getName()));
            }catch (IOException ignored) {
            }
        }
    }

    public void teleportRed(List<Player> players, World world) {
        List<Location> locations = getRedLocations(world);
        for (Player player : players) {
            resetPlayer(player);
            if (locations.size() != 0) {
                Location location = locations.get(0);
                player.teleport(location);
                locations.remove(location);
            } else {
                player.teleport(getRedLocation(world));
            }
        }
    }

    public void teleportBlue(List<Player> players, World world) {
        List<Location> locations = getBlueLocations(world);
        for (Player player : players) {
            resetPlayer(player);
            if (locations.size() != 0) {
                Location location = locations.get(0);
                player.teleport(location);
                locations.remove(location);
            } else {
                player.teleport(getBlueLocation(world));
            }
        }
    }

    public void teleportRed(Player player, World world) {
        resetPlayer(player);
        player.teleport(getRedLocation(world));
    }

    public void teleportBlue(Player player, World world) {
        resetPlayer(player);
        player.teleport(getBlueLocation(world));
    }

    public void teleportSpectator(Player player, World world) {
        resetSpectator(player);
        player.teleport(getSpectateLocation(world));
    }

    public Location getRedLocation(World world) {
        Random random = new Random();
        List<Location> locations = getRedLocations(world);
        int number = random.nextInt(0, locations.size());
        return locations.get(number);
    }

    public List<Location> getRedLocations(World world) {
        List<Location> locations = new ArrayList<>();
        for (Location location : redSpawns) {
            Location loc = location.clone();
            loc.setWorld(world);
            locations.add(loc);
        }
        return locations;
    }

    public Location getBlueLocation(World world) {
        Random random = new Random();
        List<Location> locations = getBlueLocations(world);
        int number = random.nextInt(0, locations.size());
        return locations.get(number);
    }

    public List<Location> getBlueLocations(World world) {
        List<Location> locations = new ArrayList<>();
        for (Location location : blueSpawns) {
            Location loc = location.clone();
            loc.setWorld(world);
            locations.add(loc);
        }
        return locations;
    }

    public Location getSpectateLocation(World world) {
        Location location = spectate.clone();
        location.setWorld(world);
        return location;
    }

    public boolean isRedstone(Material material) {
        if (material.name().contains("_BUTTON")) {
            return true;
        }
        if (material.name().contains("_PRESSURE_PLATE")) {
            return true;
        }
        List<Material> materials = new ArrayList<>();
        materials.add(Material.SCULK_SENSOR);
        materials.add(Material.DAYLIGHT_DETECTOR);
        materials.add(Material.JUKEBOX);
        materials.add(Material.LEVER);
        materials.add(Material.REPEATER);
        materials.add(Material.COMPARATOR);
        materials.add(Material.BELL);
        materials.add(Material.NOTE_BLOCK);
        materials.add(Material.MINECART);
        return materials.contains(material);
    }

    public boolean isDoors(Material material) {
        if (material.name().contains("_DOOR")) {
            return true;
        }
        if (material.name().contains("_TRAPDOOR")) {
            return true;
        }
        return material.name().contains("_GATE");
    }

    public boolean isChest(Material material) {
        if (material.name().contains("SHULKER_BOX")) {
            return true;
        }
        List<Material> materials = new ArrayList<>();
        materials.add(Material.ARMOR_STAND);
        materials.add(Material.BARREL);
        materials.add(Material.BEEHIVE);
        materials.add(Material.BLAST_FURNACE);
        materials.add(Material.BREWING_STAND);
        materials.add(Material.CAMPFIRE);
        materials.add(Material.CAULDRON);
        materials.add(Material.CHEST);
        materials.add(Material.DISPENSER);
        materials.add(Material.DROPPER);
        materials.add(Material.ENDER_CHEST);
        materials.add(Material.FLOWER_POT);
        materials.add(Material.FURNACE);
        materials.add(Material.HOPPER);
        materials.add(Material.ITEM_FRAME);
        materials.add(Material.JUKEBOX);
        materials.add(Material.LECTERN);
        materials.add(Material.CHEST_MINECART);
        materials.add(Material.HOPPER_MINECART);
        materials.add(Material.SMOKER);
        materials.add(Material.TRAPPED_CHEST);
        return materials.contains(material);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getWorldName() {
        return worldName;
    }

    public List<Location> getRedSpawns() {
        return redSpawns;
    }

    public List<Location> getBlueSpawns() {
        return blueSpawns;
    }

    public Location getSpectate() {
        return spectate;
    }

    public boolean isLavaInstantKill() {
        return lavaInstantKill;
    }

    public boolean isWaterInstantKill() {
        return waterInstantKill;
    }

    public boolean isDurability() {
        return durability;
    }

    public boolean isHunger() {
        return hunger;
    }

    public boolean isDropping() {
        return dropping;
    }

    public boolean isPicking() {
        return picking;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public boolean isPlacing() {
        return placing;
    }

    public boolean isRedstone() {
        return redstone;
    }

    public boolean isDoors() {
        return doors;
    }

    public boolean isChest() {
        return chest;
    }

    public List<ArenaGameRule> getGameRules() {
        return gameRules;
    }
}
