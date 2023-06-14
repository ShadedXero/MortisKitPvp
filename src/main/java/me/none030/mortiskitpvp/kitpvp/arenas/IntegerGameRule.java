package me.none030.mortiskitpvp.kitpvp.arenas;

import org.bukkit.GameRule;
import org.bukkit.World;

public class IntegerGameRule extends ArenaGameRule {

    private final GameRule<Integer> gameRule;
    private final int value;

    public IntegerGameRule(GameRule<Integer> gameRule, int value) {
        this.gameRule = gameRule;
        this.value = value;
    }

    @Override
    public void setGameRule(World world) {
        world.setGameRule(gameRule, value);
    }

    public GameRule<Integer> getGameRule() {
        return gameRule;
    }

    public int getValue() {
        return value;
    }
}
