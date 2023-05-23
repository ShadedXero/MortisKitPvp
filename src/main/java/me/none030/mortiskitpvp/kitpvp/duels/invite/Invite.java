package me.none030.mortiskitpvp.kitpvp.duels.invite;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Invite {

    private final Arena arena;
    private final Player inviter;
    private final Player invited;
    private final String redName;
    private final String blueName;
    private long timer;
    private boolean accepted;

    public Invite(Arena arena, Player inviter, Player invited, String redName, String blueName) {
        this.arena = arena;
        this.inviter = inviter;
        this.invited = invited;
        this.redName = redName;
        this.blueName = blueName;
        this.accepted = false;
        timer = 0;
    }

    public abstract List<Player> getRedPlayers();

    public abstract List<Player> getBluePlayers();

    public Arena getArena() {
        return arena;
    }

    public Player getInviter() {
        return inviter;
    }

    public Player getInvited() {
        return invited;
    }

    public String getRedName() {
        return redName;
    }

    public String getBlueName() {
        return blueName;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
