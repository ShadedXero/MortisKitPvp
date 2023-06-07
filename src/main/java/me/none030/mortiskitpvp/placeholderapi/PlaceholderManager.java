package me.none030.mortiskitpvp.placeholderapi;

import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.killstreak.KillStreakManager;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;

public class PlaceholderManager extends Manager {

    private final KitManager kitManager;
    private final KillStreakManager killStreakManager;

    public PlaceholderManager(KitManager kitManager, KillStreakManager killStreakManager) {
        this.kitManager = kitManager;
        this.killStreakManager = killStreakManager;
        new PlaceholderAddon(this).register();
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public KillStreakManager getKillStreakManager() {
        return killStreakManager;
    }
}
