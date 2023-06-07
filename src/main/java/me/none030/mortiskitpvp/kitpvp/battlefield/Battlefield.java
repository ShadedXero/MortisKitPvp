package me.none030.mortiskitpvp.kitpvp.battlefield;

import me.none030.mortiskitpvp.MortisKitPvp;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Battlefield {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final String name;
    private final String author;
    private final World world;
    private final Location spawnPoint;
    private final Location protectedOrigin;
    private final Location protectedEnd;
    private final ItemStack elytra;
    private final boolean durability;
    private final boolean hunger;

    public Battlefield(String name, String author, World world, Location spawnPoint, Location protectedOrigin, Location protectedEnd, ItemStack elytra, boolean durability, boolean hunger) {
        this.name = name;
        this.author = author;
        this.world = world;
        this.spawnPoint = spawnPoint;
        this.protectedOrigin = protectedOrigin;
        this.protectedEnd = protectedEnd;
        this.elytra = elytra;
        this.durability = durability;
        this.hunger = hunger;
    }

    public void reset(Player player) {
        if (plugin.hasHeads()) {
            plugin.getHeads().getManager().getHeadManager().removeTaskId(player);
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        removePotionEffects(player);
        player.setHealth(20);
        player.setAbsorptionAmount(0);
        player.damage(0.0001);
    }

    private void removePotionEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public void teleport(Player player) {
        reset(player);
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

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
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
