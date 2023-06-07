package me.none030.mortiskitpvp.kitpvp.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatListener implements Listener {

    private final CombatManager combatManager;

    public CombatListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        combatManager.addCombat(player);
        Player damager = (Player) e.getDamager();
        combatManager.addCombat(damager);
        combatManager.getDamagerByPlayer().put(player, damager);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        combatManager.removeCombat(player);
        Player damager = combatManager.getDamagerByPlayer().get(player);
        if (damager == null) {
            return;
        }
        combatManager.getKillStreakManager().addKills(damager);
        combatManager.getDamagerByPlayer().remove(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        combatManager.removeCombat(player);
        Player damager = combatManager.getDamagerByPlayer().get(player);
        if (damager == null) {
            return;
        }
        combatManager.getKillStreakManager().addKills(damager);
        combatManager.getDamagerByPlayer().remove(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        combatManager.removeCombat(player);
        Player damager = combatManager.getDamagerByPlayer().get(player);
        if (damager == null) {
            return;
        }
        combatManager.getKillStreakManager().addKills(damager);
        combatManager.getDamagerByPlayer().remove(player);
    }
}
