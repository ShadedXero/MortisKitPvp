package me.none030.mortiskitpvp.kitpvp.game;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import me.none030.mortiskitpvp.kitpvp.kits.KitManager;
import me.none030.mortiskitpvp.kitpvp.kits.KitMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

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
        arena.teleportRed(new ArrayList<>(getRedPlayers()), world);
        arena.teleportBlue(new ArrayList<>(getBluePlayers()), world);
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
       List<GamePlayer> gamePlayers = new ArrayList<>(getAliveRedGamePlayers());
        gamePlayers.addAll(getAliveBlueGamePlayers());
        for (GamePlayer gamePlayer : gamePlayers) {
            Player player = gamePlayer.getPlayer();
            Kit kit = kitManager.getKit(player);
            if (kit == null) {
                kit = kitManager.getRandomKit(player);
                if (kit == null) {
                    return;
                }
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

    public List<Player> getRedPlayers() {
        List<Player> redPlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : getRedGamePlayers()) {
            redPlayers.add(gamePlayer.getPlayer());
        }
        return redPlayers;
    }

    public List<Player> getBluePlayers() {
        List<Player> bluePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : getBlueGamePlayers()) {
            bluePlayers.add(gamePlayer.getPlayer());
        }
        return bluePlayers;
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
        for (GamePlayer gamePlayer : getRedGamePlayers()) {
            if (gamePlayer.isSpectating()) {
                continue;
            }
            redGamePlayers.add(gamePlayer);
        }
        return redGamePlayers;
    }

    public List<GamePlayer> getAliveBlueGamePlayers() {
        List<GamePlayer> blueGamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : getBlueGamePlayers()) {
            if (gamePlayer.isSpectating()) {
                continue;
            }
            blueGamePlayers.add(gamePlayer);
        }
        return blueGamePlayers;
    }

    public void checkGamePlayers() {
        for (GamePlayer gamePlayer : gamePlayers) {
            TeamType team = gamePlayer.getTeam();
            if (team.equals(TeamType.NONE)) {
                if (!world.equals(gamePlayer.getPlayer().getWorld())) {
                    arena.teleportSpectator(gamePlayer.getPlayer(), world);
                }
                if (!gamePlayer.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                    gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                }
            }
            if (team.equals(TeamType.BLUE)) {
                if (gamePlayer.isSpectating()) {
                    if (!world.equals(gamePlayer.getPlayer().getWorld())) {
                        arena.teleportSpectator(gamePlayer.getPlayer(), world);
                    }
                    if (!gamePlayer.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                        gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                }else {
                    if (!world.equals(gamePlayer.getPlayer().getWorld())) {
                        arena.teleportBlue(gamePlayer.getPlayer(), world);
                    }
                    if (!gamePlayer.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                        gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
            if (team.equals(TeamType.RED)) {
                if (gamePlayer.isSpectating()) {
                    if (!world.equals(gamePlayer.getPlayer().getWorld())) {
                        arena.teleportSpectator(gamePlayer.getPlayer(), world);
                    }
                    if (!gamePlayer.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                        gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                }else {
                    if (!world.equals(gamePlayer.getPlayer().getWorld())) {
                        arena.teleportRed(gamePlayer.getPlayer(), world);
                    }
                    if (!gamePlayer.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                        gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
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
        if (winner.equals(TeamType.NONE)) {
            List<GamePlayer> gamePlayers = new ArrayList<>(getRedGamePlayers());
            gamePlayers.addAll(getBlueGamePlayers());
            Iterator<GamePlayer> gamePlayerList = gamePlayers.iterator();
            while (gamePlayerList.hasNext()) {
                GamePlayer gamePlayer = gamePlayerList.next();
                if (gamePlayer == null) {
                    continue;
                }
                gamePlayerList.remove();
                Component title = Component.text(gameManager.getMessage("TIE_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("TIE_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            }
        }
        if (winner.equals(TeamType.RED)) {
            Iterator<GamePlayer> redGamePlayers = getRedGamePlayers().iterator();
            while (redGamePlayers.hasNext()) {
                GamePlayer gamePlayer = redGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                redGamePlayers.remove();
                Component title = Component.text(gameManager.getMessage("WIN_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("WIN_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            }
            Iterator<GamePlayer> blueGamePlayers = getBlueGamePlayers().iterator();
            while (blueGamePlayers.hasNext()) {
                GamePlayer gamePlayer = blueGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                blueGamePlayers.remove();
                Component title = Component.text(gameManager.getMessage("LOSE_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("LOSE_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            }
        }
        if (winner.equals(TeamType.BLUE)) {
            Iterator<GamePlayer> blueGamePlayers = getBlueGamePlayers().iterator();
            while (blueGamePlayers.hasNext()) {
                GamePlayer gamePlayer = blueGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                blueGamePlayers.remove();
                Component title = Component.text(gameManager.getMessage("WIN_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("WIN_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            }
            Iterator<GamePlayer> redGamePlayers = getRedGamePlayers().iterator();
            while (redGamePlayers.hasNext()) {
                GamePlayer gamePlayer = redGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                redGamePlayers.remove();
                Component title = Component.text(gameManager.getMessage("LOSE_TITLE").replace("%team_name%", gamePlayer.getTeamName()));
                Component subTitle = Component.text(gameManager.getMessage("LOSE_SUBTITLE").replace("%team_name%", gamePlayer.getTeamName()));
                gamePlayer.getPlayer().showTitle(Title.title(title, subTitle));
            }
        }
    }

    public void sendResultMessage(GameManager gameManager) {
        if (winner.equals(TeamType.NONE)) {
            List<GamePlayer> gamePlayers = new ArrayList<>(getRedGamePlayers());
            gamePlayers.addAll(getBlueGamePlayers());
            Iterator<GamePlayer> gamePlayerList = gamePlayers.iterator();
            while (gamePlayerList.hasNext()) {
                GamePlayer gamePlayer = gamePlayerList.next();
                if (gamePlayer == null) {
                    continue;
                }
                gamePlayerList.remove();
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("TIE").replace("%team_name%", gamePlayer.getTeamName()));
            }
        }
        if (winner.equals(TeamType.RED)) {
            Iterator<GamePlayer> redGamePlayers = getRedGamePlayers().iterator();
            while (redGamePlayers.hasNext()) {
                GamePlayer gamePlayer = redGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                redGamePlayers.remove();
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("WIN").replace("%team_name%", gamePlayer.getTeamName()));
            }
            Iterator<GamePlayer> blueGamePlayers = getBlueGamePlayers().iterator();
            while (blueGamePlayers.hasNext()) {
                GamePlayer gamePlayer = blueGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                blueGamePlayers.remove();
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("LOSE").replace("%team_name%", gamePlayer.getTeamName()));
            }
        }
        if (winner.equals(TeamType.BLUE)) {
            Iterator<GamePlayer> blueGamePlayers = getBlueGamePlayers().iterator();
            while (blueGamePlayers.hasNext()) {
                GamePlayer gamePlayer = blueGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                blueGamePlayers.remove();
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("WIN").replace("%team_name%", gamePlayer.getTeamName()));
            }
            Iterator<GamePlayer> redGamePlayers = getRedGamePlayers().iterator();
            while (redGamePlayers.hasNext()) {
                GamePlayer gamePlayer = redGamePlayers.next();
                if (gamePlayer == null) {
                    continue;
                }
                redGamePlayers.remove();
                gamePlayer.getPlayer().sendMessage(gameManager.getMessage("LOSE").replace("%team_name%", gamePlayer.getTeamName()));
            }
        }
    }

    public void end(GameManager gameManager) {
        Iterator<GamePlayer> gamePlayerList = gamePlayers.iterator();
        while (gamePlayerList.hasNext()) {
            GamePlayer gamePlayer = gamePlayerList.next();
            if (gamePlayer == null) {
                continue;
            }
            gamePlayerList.remove();
            gameManager.getGameByPlayer().remove(gamePlayer.getPlayer());
            gameManager.getBattlefieldManager().getBattlefield().teleport(gamePlayer.getPlayer());
        }
        arena.delete(world);
    }

    public void removePlayer(GameManager gameManager, GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gameManager.getGameByPlayer().remove(gamePlayer.getPlayer());
        gameManager.getBattlefieldManager().getBattlefield().teleport(gamePlayer.getPlayer());
    }

    public void check(GameManager gameManager) {
        List<GamePlayer> redGamePlayers = getAliveRedGamePlayers();
        List<GamePlayer> blueGamePlayers = getAliveBlueGamePlayers();
        if (redGamePlayers.size() == 0 && blueGamePlayers.size() == 0) {
            setEnded(true);
            setWinner(TeamType.NONE);
            sendResultMessage(gameManager);
            showResult(gameManager);
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
            return;
        }
        if (time >= gameManager.getLength()) {
            if (redGamePlayers.size() == blueGamePlayers.size()) {
                setEnded(true);
                setWinner(TeamType.NONE);
                sendResultMessage(gameManager);
                showResult(gameManager);
                return;
            }
            if (redGamePlayers.size() < blueGamePlayers.size()) {
                setEnded(true);
                setWinner(TeamType.BLUE);
                sendResultMessage(gameManager);
                showResult(gameManager);
            }else {
                setEnded(true);
                setWinner(TeamType.RED);
                sendResultMessage(gameManager);
                showResult(gameManager);
            }
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
