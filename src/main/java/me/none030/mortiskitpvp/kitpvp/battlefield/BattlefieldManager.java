package me.none030.mortiskitpvp.kitpvp.battlefield;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BattlefieldManager extends Manager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final KitManager kitManager;
    private final Battlefield battlefield;
    private final List<Player> unsafePlayers;
    private final List<Player> withoutElytra;

    public BattlefieldManager(KitManager kitManager, Battlefield battlefield) {
        this.kitManager = kitManager;
        this.battlefield = battlefield;
        this.unsafePlayers = new ArrayList<>();
        this.withoutElytra = new ArrayList<>();
        MortisKitPvp plugin = MortisKitPvp.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new BattlefieldListener(this), plugin);
        check();
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Player> unsafePlayersIterator = unsafePlayers.iterator();
                while (unsafePlayersIterator.hasNext()) {
                    Player player = unsafePlayersIterator.next();
                    if (battlefield.isWorld(player.getWorld())) {
                        continue;
                    }
                    unsafePlayersIterator.remove();
                }
                Iterator<Player> withoutElytraIterator = withoutElytra.iterator();
                while (withoutElytraIterator.hasNext()) {
                    Player player = withoutElytraIterator.next();
                    if (battlefield.isWorld(player.getWorld())) {
                        continue;
                    }
                    withoutElytraIterator.remove();
                }
                for (Player player : battlefield.getWorld().getPlayers()) {
                    if (battlefield.isProtected(player.getLocation())) {
                        if (!battlefield.hasElytra(player)) {
                            battlefield.addElytra(player);
                        }
                        unsafePlayers.remove(player);
                        withoutElytra.remove(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public List<Player> getUnsafePlayers() {
        return unsafePlayers;
    }

    public List<Player> getWithoutElytra() {
        return withoutElytra;
    }
}
