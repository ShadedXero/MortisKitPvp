package me.none030.mortiskitpvp.kitpvp.arenas;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import me.none030.mortiskitpvp.MortisKitPvp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Arena {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final String id;
    private final String name;
    private final String author;
    private final String worldName;
    private final Location redSpawn;
    private final Location blueSpawn;
    private final boolean lavaInstantKill;
    private final boolean waterInstantKill;
    private final boolean durability;
    private final boolean hunger;

    public Arena(String id, String name, String author, String worldName, Location redSpawn, Location blueSpawn, boolean lavaInstantKill, boolean waterInstantKill, boolean durability, boolean hunger) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.worldName = worldName;
        this.redSpawn = redSpawn;
        this.blueSpawn = blueSpawn;
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

    public void teleportRed(Player player, World world) {
        player.teleport(getRedLocation(world));
    }

    public void teleportBlue(Player player, World world) {
        player.teleport(getBlueLocation(world));
    }

    public Location getRedLocation(World world) {
        Location location = redSpawn.clone();
        location.setWorld(world);
        return location;
    }

    public Location getBlueLocation(World world) {
        Location location = blueSpawn.clone();
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

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
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
