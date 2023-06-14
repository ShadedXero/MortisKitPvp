package me.none030.mortiskitpvp.kitpvp.arenas;

import org.bukkit.GameRule;
import org.bukkit.World;

public class BooleanGameRule extends ArenaGameRule {

    private final GameRule<Boolean> gameRule;
    private final boolean value;

    public BooleanGameRule(GameRule<Boolean> gameRule, boolean value) {
        this.gameRule = gameRule;
        this.value = value;
    }

    @Override
    public void setGameRule(World world) {
        world.setGameRule(gameRule, value);
    }

    public GameRule<Boolean> getGameRule() {
        return gameRule;
    }

    public boolean isValue() {
        return value;
    }
}
