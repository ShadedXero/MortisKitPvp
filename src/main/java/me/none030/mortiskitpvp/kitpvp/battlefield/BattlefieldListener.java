package me.none030.mortiskitpvp.kitpvp.battlefield;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.armorequipevent.ArmorEquipEvent;
import me.none030.mortiskitpvp.armorequipevent.ArmorType;
import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class BattlefieldListener implements Listener {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final BattlefieldManager battlefieldManager;

    public BattlefieldListener(BattlefieldManager battlefieldManager) {
        this.battlefieldManager = battlefieldManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        battlefield.teleport(player);
        battlefield.addElytra(player);
    }

    @EventHandler
    public void onReSpawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        battlefield.teleport(player);
        battlefield.addElytra(player);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld()) || battlefield.isHunger()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        Player player = e.getPlayer();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld()) || battlefield.isDurability()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld())) {
            return;
        }
        if (battlefieldManager.getUnsafePlayers().contains(player)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld())) {
            return;
        }
        if (battlefieldManager.getUnsafePlayers().contains(player)) {
            if (battlefield.isProtected(e.getTo())) {
                e.setCancelled(true);
            }
        }else {
            if (!battlefield.isProtected(e.getTo())) {
                battlefieldManager.getUnsafePlayers().add(player);
            }
        }
        if (!battlefieldManager.getUnsafePlayers().contains(player)) {
            return;
        }
        if (battlefieldManager.getWithoutElytra().contains(player)) {
            return;
        }
        if (!battlefield.hasElytra(player)) {
            return;
        }
        if (!((LivingEntity) player).isOnGround()) {
            return;
        }
        battlefield.removeElytra(player);
        battlefieldManager.getWithoutElytra().add(player);
        Kit kit = battlefieldManager.getKitManager().getKit(player);
        if (kit == null) {
            Kit randomKit = battlefieldManager.getKitManager().getRandomKit(player);
            if (randomKit == null) {
                return;
            }
            kit = randomKit;
        }
        kit.give(player);
    }

    @EventHandler
    public void onUnEquipArmor(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        if (battlefieldManager.getWithoutElytra().contains(player)) {
            return;
        }
        if (!e.getType().equals(ArmorType.CHESTPLATE)) {
            return;
        }
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld())) {
            return;
        }
        ItemStack oldItem = e.getOldArmorPiece();
        if (oldItem == null || oldItem.getType().isAir() || !battlefield.isElytra(oldItem)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.spigot().respawn(), 1L);
        e.getDrops().clear();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        if (!battlefield.isWorld(player.getWorld())) {
            return;
        }
        battlefieldManager.getWithoutElytra().remove(player);
        battlefieldManager.getUnsafePlayers().remove(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Battlefield battlefield = battlefieldManager.getBattlefield();
        battlefield.reset(player);
        if (!battlefield.isWorld(player.getWorld())) {
            return;
        }
        battlefieldManager.getWithoutElytra().remove(player);
        battlefieldManager.getUnsafePlayers().remove(player);
    }
}
