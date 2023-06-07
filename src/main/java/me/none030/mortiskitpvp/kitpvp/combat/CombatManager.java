package me.none030.mortiskitpvp.kitpvp.combat;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.battlefield.BattlefieldManager;
import me.none030.mortiskitpvp.kitpvp.killstreak.KillStreakManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CombatManager extends Manager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final BattlefieldManager battlefieldManager;
    private final KillStreakManager killStreakManager;
    private final long resetTime;
    private final List<Combat> inCombat;
    private final HashMap<Player, Player> damagerByPlayer;

    public CombatManager(BattlefieldManager battlefieldManager, KillStreakManager killStreakManager, long resetTime) {
        this.battlefieldManager = battlefieldManager;
        this.killStreakManager = killStreakManager;
        this.resetTime = resetTime;
        this.inCombat = new ArrayList<>();
        this.damagerByPlayer = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
        plugin.getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Combat> combats = inCombat.iterator();
                while (combats.hasNext()) {
                    Combat combat = combats.next();
                    combat.setTimer(combat.getTimer() + 1);
                    if (combat.isTime(resetTime)) {
                        combats.remove();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void addCombat(Player player) {
        Combat combat = getCombat(player);
        if (combat != null) {
            inCombat.remove(combat);
        }
        inCombat.add(new Combat(player));
    }

    public void removeCombat(Player player) {
        Combat combat = getCombat(player);
        if (combat == null) {
            return;
        }
        inCombat.remove(combat);
    }

    public Combat getCombat(Player player) {
        for (Combat combat : inCombat) {
            if (combat.isPlayer(player)) {
                return combat;
            }
        }
        return null;
    }

    public BattlefieldManager getBattlefieldManager() {
        return battlefieldManager;
    }

    public KillStreakManager getKillStreakManager() {
        return killStreakManager;
    }

    public long getResetTime() {
        return resetTime;
    }

    public List<Combat> getInCombat() {
        return inCombat;
    }

    public HashMap<Player, Player> getDamagerByPlayer() {
        return damagerByPlayer;
    }
}
