package me.none030.mortiskitpvp.kitpvp.battlefield;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BattlefieldManager extends Manager {

    private final KitManager kitManager;
    private final Battlefield battlefield;
    private final List<Player> unsafePlayers;

    public BattlefieldManager(KitManager kitManager, Battlefield battlefield) {
        this.kitManager = kitManager;
        this.battlefield = battlefield;
        this.unsafePlayers = new ArrayList<>();
        MortisKitPvp plugin = MortisKitPvp.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new BattlefieldListener(this), plugin);
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
}
