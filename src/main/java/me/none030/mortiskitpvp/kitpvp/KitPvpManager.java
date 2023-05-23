package me.none030.mortiskitpvp.kitpvp;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.armorequipevent.ArmorEquipEvent;
import me.none030.mortiskitpvp.config.ConfigManager;
import me.none030.mortiskitpvp.items.ItemManager;
import me.none030.mortiskitpvp.kitpvp.arenas.ArenaManager;
import me.none030.mortiskitpvp.kitpvp.battlefield.BattlefieldManager;
import me.none030.mortiskitpvp.kitpvp.combat.CombatManager;
import me.none030.mortiskitpvp.kitpvp.duels.DuelManager;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import me.none030.mortiskitpvp.kitpvp.killstreak.KillStreakManager;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import me.none030.mortiskitpvp.kitpvp.matchmakings.MatchMakingManager;
import me.none030.mortiskitpvp.placeholderapi.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class KitPvpManager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private ItemManager itemManager;
    private ArenaManager arenaManager;
    private KitManager kitManager;
    private BattlefieldManager battlefieldManager;
    private GameManager gameManager;
    private DuelManager duelManager;
    private KillStreakManager killStreakManager;
    private CombatManager combatManager;
    private MatchMakingManager matchMakingManager;
    private PlaceholderManager placeholderManager;
    private ConfigManager configManager;

    public KitPvpManager() {
        ArmorEquipEvent.registerListener(plugin);
        this.itemManager = new ItemManager();
        this.arenaManager = new ArenaManager();
        this.configManager = new ConfigManager(this);
        plugin.getServer().getPluginCommand("kitpvp").setExecutor(new KitPvpCommand(this));
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
        ArmorEquipEvent.registerListener(plugin);
        setItemManager(new ItemManager());
        setArenaManager(new ArenaManager());
        setConfigManager(new ConfigManager(this));
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public void setKitManager(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    public BattlefieldManager getBattlefieldManager() {
        return battlefieldManager;
    }

    public void setBattlefieldManager(BattlefieldManager battlefieldManager) {
        this.battlefieldManager = battlefieldManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public void setDuelManager(DuelManager duelManager) {
        this.duelManager = duelManager;
    }

    public KillStreakManager getKillStreakManager() {
        return killStreakManager;
    }

    public void setKillStreakManager(KillStreakManager killStreakManager) {
        this.killStreakManager = killStreakManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public void setCombatManager(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    public MatchMakingManager getMatchMakingManager() {
        return matchMakingManager;
    }

    public void setMatchMakingManager(MatchMakingManager matchMakingManager) {
        this.matchMakingManager = matchMakingManager;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    public void setPlaceholderManager(PlaceholderManager placeholderManager) {
        this.placeholderManager = placeholderManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
