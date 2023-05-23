package me.none030.mortiskitpvp.kitpvp.battlefield;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Battlefield {

    private final World world;
    private final Location spawnPoint;
    private final Location protectedOrigin;
    private final Location protectedEnd;
    private final ItemStack elytra;
    private final boolean durability;
    private final boolean hunger;

    public Battlefield(World world, Location spawnPoint, Location protectedOrigin, Location protectedEnd, ItemStack elytra, boolean durability, boolean hunger) {
        this.world = world;
        this.spawnPoint = spawnPoint;
        this.protectedOrigin = protectedOrigin;
        this.protectedEnd = protectedEnd;
        this.elytra = elytra;
        this.durability = durability;
        this.hunger = hunger;
    }

    public void reset(Player player) {
        player.getInventory().clear();
        player.setHealth(20);
    }

    public void teleport(Player player) {
        player.teleport(spawnPoint);
    }

    public void addElytra(Player player) {
        reset(player);
        player.getInventory().setChestplate(elytra);
    }

    public void removeElytra(Player player) {
        player.getInventory().setChestplate(null);
    }

    public boolean hasElytra(Player player) {
        ItemStack elytra = player.getInventory().getChestplate();
        if (elytra == null) {
            return false;
        }
        return elytra.equals(this.elytra);
    }

    public boolean isElytra(ItemStack elytra) {
        return this.elytra.isSimilar(elytra);
    }

    public boolean isProtected(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double minX = Math.min(protectedOrigin.getX(), protectedEnd.getX());
        double minY = Math.min(protectedOrigin.getY(), protectedEnd.getY());
        double minZ = Math.min(protectedOrigin.getZ(), protectedEnd.getZ());
        double maxX = Math.max(protectedOrigin.getX(), protectedEnd.getX());
        double maxY = Math.max(protectedOrigin.getY(), protectedEnd.getY());
        double maxZ = Math.max(protectedOrigin.getZ(), protectedEnd.getZ());
        return (minX < x && maxX > x) && (minY < y && maxY > y) && (minZ < z && maxZ > z);
    }

    public boolean isWorld(World world) {
        return this.world.getName().equals(world.getName());
    }

    public World getWorld() {
        return world;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public Location getProtectedOrigin() {
        return protectedOrigin;
    }

    public Location getProtectedEnd() {
        return protectedEnd;
    }

    public ItemStack getElytra() {
        return elytra;
    }

    public boolean isDurability() {
        return durability;
    }

    public boolean isHunger() {
        return hunger;
    }
}
