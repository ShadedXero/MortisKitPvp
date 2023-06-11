package me.none030.mortiskitpvp.kitpvp.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null || game.getArena().isHunger()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        if (player.isInLava()) {
            if (game.getArena().isLavaInstantKill()) {
                player.setHealth(0);
            }
        }
        if (player.isInWater()) {
            if (game.getArena().isWaterInstantKill()) {
                player.setHealth(0);
            }
        }
        if (game.isStarted()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null || game.getArena().isDurability()) {
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
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        if (!game.isStarted() || game.isEnded()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        Game game = gameManager.getGameByPlayer().get(attacker);
        if (game == null) {
            return;
        }
        GamePlayer victimPlayer = game.getGamePlayer(victim);
        GamePlayer attackerPlayer = game.getGamePlayer(attacker);
        if (victimPlayer == null || attackerPlayer == null) {
            return;
        }
        if (!victimPlayer.isTeam(attackerPlayer.getTeam())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        GamePlayer gamePlayer = game.getGamePlayer(player);
        if (gamePlayer == null) {
           return;
        }
        gamePlayer.setSpectating(game.getArena(), game.getWorld(), true);
        game.check(gameManager);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            return;
        }
        GamePlayer gamePlayer = game.getGamePlayer(player);
        if (gamePlayer == null) {
            return;
        }
        game.removePlayer(gameManager, gamePlayer);
        game.check(gameManager);
    }
}
