package me.none030.mortiskitpvp.kitpvp.arenas;

import me.none030.mortiskitpvp.kitpvp.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ArenaManager extends Manager {

    private final List<Arena> arenas;
    private final HashMap<String, Arena> arenaById;

    public ArenaManager() {
        this.arenas = new ArrayList<>();
        this.arenaById = new HashMap<>();
    }

    public Arena getRandom() {
        Random random = new Random();
        int index = random.nextInt(0, arenas.size());
        return arenas.get(index);
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public HashMap<String, Arena> getArenaById() {
        return arenaById;
    }
}
