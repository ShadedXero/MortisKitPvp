package me.none030.mortiskitpvp.kitpvp.arenas;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import me.none030.mortiskitpvp.MortisKitPvp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

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

    public Arena(String id, String name, String author, String worldName, List<Location> redSpawns, List<Location> blueSpawns, Location spectate, boolean lavaInstantKill, boolean waterInstantKill, boolean durability, boolean hunger) {
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
        unloadWorld();
    }

    private void unloadWorld() {
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.unloadWorld(worldName, true);
    }

    public World create() {
        String newName = UUID.randomUUID().toString();
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.cloneWorld(worldName, newName);
        return Bukkit.getWorld(newName);
    }

    public void delete(World world) {
        MVWorldManager worldManager = plugin.getMultiverseAPI().getMVWorldManager();
        worldManager.deleteWorld(world.getName());
    }

    public void teleportRed(List<Player> players, World world) {
        List<Location> locations = getRedLocations(world);
        for (Player player : players) {
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
            if (locations.size() != 0) {
                Location location = locations.get(0);
                player.teleport(location);
                locations.remove(location);
            } else {
                player.teleport(getBlueLocation(world));
            }
        }
    }

    public void teleportSpectator(Player player, World world) {
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
}
