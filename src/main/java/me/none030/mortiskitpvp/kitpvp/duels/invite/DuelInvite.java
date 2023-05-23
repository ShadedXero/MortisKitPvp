package me.none030.mortiskitpvp.kitpvp.duels.invite;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import org.bukkit.entity.Player;

import java.util.List;

public class DuelInvite extends Invite {

    private final List<Player> redPlayers;
    private final List<Player> bluePlayers;

    public DuelInvite(Arena arena, Player inviter, Player invited, String redName, String blueName, List<Player> redPlayers, List<Player> bluePlayers) {
        super(arena, inviter, invited, redName, blueName);
        this.redPlayers = redPlayers;
        this.bluePlayers = bluePlayers;
    }

    @Override
    public List<Player> getRedPlayers() {
        return redPlayers;
    }

    @Override
    public List<Player> getBluePlayers() {
        return bluePlayers;
    }
}
