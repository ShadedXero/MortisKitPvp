package me.none030.mortiskitpvp.kitpvp.game;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamePlayer {

    private final Player player;
    private final TeamType team;
    private final String teamName;
    private boolean spectating;

    public GamePlayer(Player player, TeamType team, String teamName) {
        this.player = player;
        this.team = team;
        this.spectating = team.equals(TeamType.NONE);
        this.teamName = teamName;
    }

    public boolean isTeam(TeamType team) {
        return this.team.equals(team);
    }

    public Player getPlayer() {
        return player;
    }

    public TeamType getTeam() {
        return team;
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
        if (spectating) {
            player.setGameMode(GameMode.SPECTATOR);
        }else {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
