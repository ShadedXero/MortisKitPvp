package me.none030.mortiskitpvp.config;

import me.none030.mortiskitpvp.kitpvp.KitPvpManager;

public class ConfigManager {

    private final KitPvpManager manager;
    private final ItemConfig itemConfig;
    private final ArenaConfig arenaConfig;
    private final KitConfig kitConfig;
    private final BattlefieldConfig battlefieldConfig;
    private final GameConfig gameConfig;
    private final DuelConfig duelConfig;
    private final KillStreakConfig killStreakConfig;
    private final CombatConfig combatConfig;
    private final MatchMakingConfig matchMakingConfig;
    private final PlaceholderConfig placeholderConfig;

    public ConfigManager(KitPvpManager manager) {
        this.manager = manager;
        this.itemConfig = new ItemConfig(this);
        this.arenaConfig = new ArenaConfig(this);
        this.kitConfig = new KitConfig(this);
        this.battlefieldConfig = new BattlefieldConfig(this);
        this.gameConfig = new GameConfig(this);
        this.duelConfig = new DuelConfig(this);
        this.killStreakConfig = new KillStreakConfig(this);
        this.combatConfig = new CombatConfig(this);
        this.matchMakingConfig = new MatchMakingConfig(this);
        this.placeholderConfig = new PlaceholderConfig(this);
    }

    public KitPvpManager getManager() {
        return manager;
    }

    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    public ArenaConfig getArenaConfig() {
        return arenaConfig;
    }

    public KitConfig getKitConfig() {
        return kitConfig;
    }

    public BattlefieldConfig getBattlefieldConfig() {
        return battlefieldConfig;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public DuelConfig getDuelConfig() {
        return duelConfig;
    }

    public KillStreakConfig getKillStreakConfig() {
        return killStreakConfig;
    }

    public CombatConfig getCombatConfig() {
        return combatConfig;
    }

    public MatchMakingConfig getMatchMakingConfig() {
        return matchMakingConfig;
    }

    public PlaceholderConfig getPlaceholderConfig() {
        return placeholderConfig;
    }
}
