package me.none030.mortiskitpvp.kitpvp.matchmakings;

import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchMaking {

    private final int mode;
    private final int minPlayers;
    private final int maxPlayers;
    private final long waitTime;
    private final ItemStack icon;
    private final List<Arena> arenas;
    private final List<Player> queue;
    private long timer;

    public MatchMaking(int mode, int minPlayers, int maxPlayers, long waitTime, ItemStack icon, List<Arena> arenas) {
        this.mode = mode;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.waitTime = waitTime;
        this.icon = icon;
        this.arenas = arenas;
        this.queue = new ArrayList<>();
        this.timer = 0;
    }

    public boolean isMode(int mode) {
        return this.mode == mode;
    }

    public void clearQueue() {
        queue.clear();
    }

    public void addQueue(Player player) {
        queue.add(player);
    }

    public void removeQueue(Player player) {
        queue.remove(player);
    }

    public boolean hasQueue(Player player) {
        return queue.contains(player);
    }

    public boolean hasMinPlayers() {
        return queue.size() >= minPlayers;
    }

    public boolean hasOverMaxPlayers() {
        return queue.size() > maxPlayers;
    }

    public boolean hasMaxPlayers() {
        return queue.size() == maxPlayers;
    }

    public boolean hasReachedWaitTime() {
        return timer >= waitTime;
    }

    public Arena getRandomArena() {
        Random random = new Random();
        int index = random.nextInt(0, arenas.size());
        return arenas.get(index);
    }

    public int getMode() {
        return mode;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public List<Player> getQueue() {
        return queue;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}
