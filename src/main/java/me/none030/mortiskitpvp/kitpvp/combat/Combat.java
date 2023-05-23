package me.none030.mortiskitpvp.kitpvp.combat;

import org.bukkit.entity.Player;

public class Combat {

    private final Player player;
    private long timer;

    public Combat(Player player) {
        this.player = player;
        this.timer = 0;
    }

    public boolean isTime(long time) {
        return timer >= time;
    }

    public boolean isPlayer(Player player) {
        return this.player.equals(player);
    }

    public Player getPlayer() {
        return player;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}
