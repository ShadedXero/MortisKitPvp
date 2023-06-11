package me.none030.mortiskitpvp.kitpvp.duels.invite;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class Invite {

    private final String id;
    private final Arena arena;
    private final Player inviter;
    private final Player invited;
    private final String redName;
    private final String blueName;
    private long timer;

    public Invite(Arena arena, Player inviter, Player invited, String redName, String blueName) {
        this.id = getRandomId();
        this.arena = arena;
        this.inviter = inviter;
        this.invited = invited;
        this.redName = redName;
        this.blueName = blueName;
        timer = 0;
    }

    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    public abstract List<Player> getRedPlayers();

    public abstract List<Player> getBluePlayers();

    public String getId() {
        return id;
    }

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
}
