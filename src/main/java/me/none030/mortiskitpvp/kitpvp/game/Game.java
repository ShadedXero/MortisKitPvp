package me.none030.mortiskitpvp.kitpvp.game;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import me.none030.mortiskitpvp.kitpvp.kits.KitMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Arena arena;
    private final World world;
    private final List<GamePlayer> gamePlayers;
    private long time;
    private long endTime;
    private TeamType winner;
    private boolean started;
    private boolean ended;

    public Game(KitManager kitManager, Arena arena, List<GamePlayer> gamePlayers) {
        this.arena = arena;
        this.world = arena.create();
        this.gamePlayers = gamePlayers;
        this.time = 0;
        this.endTime = 0;
        this.winner = TeamType.NONE;
        this.started = false;
        this.ended = false;
        teleport();
        openKitSelector(kitManager);
    }

    private void teleport() {
        for (GamePlayer gamePlayer : getRedGamePlayers()) {
            arena.teleportRed(gamePlayer.getPlayer(), world);
        }
        for (GamePlayer gamePlayer : getBlueGamePlayers()) {
            arena.teleportBlue(gamePlayer.getPlayer(), world);
        }
    }

    private void openKitSelector(KitManager kitManager) {
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            KitMenu menu = new KitMenu(kitManager, player);
            menu.open(player);
        }
    }

    private void closeKitSelector() {
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof KitMenu)) {
                continue;
            }
            player.closeInventory();
        }
    }

    public void giveKits(KitManager kitManager) {
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            Kit kit = kitManager.getKit(player);
            if (kit == null) {
                kit = kitManager.getRandomKit(player);
            }
            kit.give(player);
        }
    }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (player.equals(gamePlayer.getPlayer())) {
                return gamePlayer;
            }
        }
        return null;
    }

    public List<GamePlayer> getRedGamePlayers() {
        List<GamePlayer> redGamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.isTeam(TeamType.RED)) {
                redGamePlayers.add(gamePlayer);
            }
        }
        return redGamePlayers;
    }

    public List<GamePlayer> getBlueGamePlayers() {
        List<GamePlayer> blueGamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.isTeam(TeamType.BLUE)) {
                blueGamePlayers.add(gamePlayer);
            }
        }
        return blueGamePlayers;
    }

    public List<GamePlayer> getAliveRedGamePlayers() {
        List<GamePlayer> redGamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (!gamePlayer.isTeam(TeamType.RED)) {
                continue;
            }
            if (gamePlayer.isSpectating()) {
                continue;
            }
            redGamePlayers.add(gamePlayer);
        }
        return redGamePlayers;
    }

    public List<GamePlayer> getAliveBlueGamePlayers() {
        List<GamePlayer> blueGamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (!gamePlayer.isTeam(TeamType.BLUE)) {
                continue;
            }
            if (gamePlayer.isSpectating()) {
                continue;
            }
            blueGamePlayers.add(gamePlayer);
        }
        return blueGamePlayers;
    }

    public void sendMessage(Component message) {
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            player.sendMessage(message);
        }
    }

    public void showCountdown(GameManager gameManager) {
        long timeLeft = gameManager.getStartTime() - time;
        Component title = Component.text(gameManager.getMessage("COUNTDOWN_TITLE").replace("%time%", String.valueOf(timeLeft)));
        Component subTitle = Component.text(gameManager.getMessage("COUNTDOWN_SUBTITLE").replace("%time%", String.valueOf(timeLeft)));
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            gamePlayer.getPlayer().sendMessage(gameManager.getMessage("COUNTDOWN").replace("%time%", String.valueOf(timeLeft)));
        }
    }

    public void showResult(GameManager gameManager) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.isTeam(winner)) {
                Component title = Component.text(gameManager.getMessage("WIN_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("WIN_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("WIN").replace("%team_name%", gamePlayer.getTeamName()));
            }else {
                Component title = Component.text(gameManager.getMessage("LOSE_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("LOSE_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("LOSE").replace("%team_name%", gamePlayer.getTeamName()));
            }
        }
    }

    public void sendResultMessage(GameManager gameManager) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.isTeam(winner)) {
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("WIN").replace("%team_name%", gamePlayer.getTeamName()));
            }else {
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("LOSE").replace("%team_name%", gamePlayer.getTeamName()));
            }
        }
    }

    public void end(GameManager gameManager) {
        for (GamePlayer gamePlayer : gamePlayers) {
            removePlayer(gameManager, gamePlayer);
        }
        arena.delete(world);
    }

    public void removePlayer(GameManager gameManager, GamePlayer gamePlayer) {
        gamePlayer.setSpectating(false);
        gameManager.getBattlefieldManager().getBattlefield().teleport(gamePlayer.getPlayer());
        gameManager.getGameByPlayer().remove(gamePlayer.getPlayer());
    }

    public void check(GameManager gameManager) {
        List<GamePlayer> redGamePlayers = getAliveRedGamePlayers();
        List<GamePlayer> blueGamePlayers = getAliveBlueGamePlayers();
        if (redGamePlayers.size() == 0 && blueGamePlayers.size() == 0) {
            setEnded(true);
            return;
        }
        if (redGamePlayers.size() == 0) {
            setEnded(true);
            setWinner(TeamType.BLUE);
            sendResultMessage(gameManager);
            showResult(gameManager);
            return;
        }
        if (blueGamePlayers.size() == 0) {
            setEnded(true);
            setWinner(TeamType.RED);
            sendResultMessage(gameManager);
            showResult(gameManager);
        }
    }

    public Arena getArena() {
        return arena;
    }

    public World getWorld() {
        return world;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public TeamType getWinner() {
        return winner;
    }

    public void setWinner(TeamType winner) {
        this.winner = winner;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
        closeKitSelector();
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
