package me.none030.mortiskitpvp.kitpvp.matchmakings;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MatchMakingManager extends Manager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final GameManager gameManager;
    private final MatchMakingMenu menu;
    private final List<MatchMaking> modes;

    public MatchMakingManager(GameManager gameManager, MatchMakingSettings settings, List<MatchMaking> modes) {
        this.gameManager = gameManager;
        this.modes = modes;
        this.menu = new MatchMakingMenu(this, settings);
        check();
        plugin.getServer().getPluginManager().registerEvents(new MatchMakingListener(this), plugin);
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (MatchMaking matchMaking : modes) {
                    if (matchMaking.hasMinPlayers()) {
                        if (matchMaking.hasOverMaxPlayers()) {
                            List<Player> queue = new ArrayList<>();
                            for (int i = 0; i < matchMaking.getMaxPlayers(); i++) {
                                Player player = matchMaking.getQueue().get(i);
                                queue.add(player);
                            }
                            gameManager.start(matchMaking.getRandomArena(), getMessage("RED_TEAM"), getMessage("BLUE_TEAM"), queue);
                        }else {
                            if (matchMaking.hasMaxPlayers()) {
                                gameManager.start(matchMaking.getRandomArena(), getMessage("RED_TEAM"), getMessage("BLUE_TEAM"), matchMaking.getQueue());
                                matchMaking.clearQueue();
                            }else {
                                matchMaking.setTimer(matchMaking.getTimer() + 1);
                                if (matchMaking.hasReachedWaitTime()) {
                                    matchMaking.setTimer(0);
                                    gameManager.start(matchMaking.getRandomArena(), getMessage("RED_TEAM"), getMessage("BLUE_TEAM"), matchMaking.getQueue());
                                    matchMaking.clearQueue();
                                }
                            }
                        }
                    }else {
                        matchMaking.setTimer(0);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public MatchMaking getMode(int mode) {
        for (MatchMaking matchMaking : modes) {
            if (matchMaking.isMode(mode)) {
                return matchMaking;
            }
        }
        return null;
    }

    public void removePlayer(Player player) {
        for (MatchMaking matchMaking : modes) {
            matchMaking.removeQueue(player);
        }
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MatchMakingMenu getMenu() {
        return menu;
    }

    public List<MatchMaking> getModes() {
        return modes;
    }
}