package me.none030.mortiskitpvp.kitpvp.game;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.config.DataConfig;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import me.none030.mortiskitpvp.kitpvp.battlefield.BattlefieldManager;
import me.none030.mortiskitpvp.kitpvp.duels.invite.Invite;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager extends Manager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final DataConfig config = new DataConfig();
    private final BattlefieldManager battlefieldManager;
    private final long length;
    private final long startTime;
    private final long endTime;
    private final boolean forceSelector;
    private final List<Game> games;
    private final HashMap<Player, Game> gameByPlayer;

    public GameManager(BattlefieldManager battlefieldManager, long length, long startTime, long endTime, boolean forceSelector) {
        this.battlefieldManager = battlefieldManager;
        this.length = length;
        this.startTime = startTime;
        this.endTime = endTime;
        this.forceSelector = forceSelector;
        this.games = new ArrayList<>();
        this.gameByPlayer = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new GameListener(this), plugin);
        plugin.getServer().getPluginCommand("leave").setExecutor(new LeaveCommand(this));
        plugin.getServer().getPluginCommand("spectate").setExecutor(new SpectateCommand(this));
    }

    private void check() {
        GameManager gameManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < games.size(); i++) {
                    Game game = games.get(i);
                    if (game == null) {
                        continue;
                    }
                    game.checkGamePlayers();
                    game.setTime(game.getTime() + 1);
                    if (!game.isStarted()) {
                        if (game.getTime() >= startTime) {
                            game.giveKits(battlefieldManager.getKitManager());
                            game.setStarted(true);
                        } else {
                            game.showCountdown(gameManager);
                        }
                    }
                    if (game.isEnded()) {
                        game.setEndTime(game.getEndTime() + 1);
                        game.showResult(gameManager);
                        if (game.getEndTime() >= endTime) {
                            games.remove(game);
                            game.end(gameManager);
                        }
                    }else {
                        game.check(gameManager);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void start(Arena arena, String redName, String blueName, List<Player> players) {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        int half = (int) Math.round((players.size() * 0.5));
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            GamePlayer gamePlayer;
            if (i >= half) {
                gamePlayer = new GamePlayer(player, TeamType.RED, redName);
            }else {
                gamePlayer = new GamePlayer(player, TeamType.BLUE, blueName);
            }
            gamePlayers.add(gamePlayer);
        }
        Game game = new Game(battlefieldManager.getKitManager(), arena, gamePlayers);
        games.add(game);
        for (Player player : players) {
            gameByPlayer.put(player, game);
        }
        config.add(game.getWorld());
    }

    public void start(Invite invite) {
        List<GamePlayer> gamePlayers = new ArrayList<>();
        for (Player redPlayer : invite.getRedPlayers()) {
            GamePlayer gamePlayer = new GamePlayer(redPlayer, TeamType.RED, invite.getRedName());
            gamePlayers.add(gamePlayer);
        }
        for (Player bluePlayer : invite.getBluePlayers()) {
            GamePlayer gamePlayer = new GamePlayer(bluePlayer, TeamType.BLUE, invite.getBlueName());
            gamePlayers.add(gamePlayer);
        }
        Game game = new Game(battlefieldManager.getKitManager(), invite.getArena(), gamePlayers);
        games.add(game);
        for (Player redPlayer : invite.getRedPlayers()) {
            gameByPlayer.put(redPlayer, game);
        }
        for (Player bluePlayer : invite.getBluePlayers()) {
            gameByPlayer.put(bluePlayer, game);
        }
        config.add(game.getWorld());
    }

    public DataConfig getConfig() {
        return config;
    }

    public BattlefieldManager getBattlefieldManager() {
        return battlefieldManager;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLength() {
        return length;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isForceSelector() {
        return forceSelector;
    }

    public List<Game> getGames() {
        return games;
    }

    public HashMap<Player, Game> getGameByPlayer() {
        return gameByPlayer;
    }
}
