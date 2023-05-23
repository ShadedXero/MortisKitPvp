package me.none030.mortiskitpvp.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.none030.mortiskitpvp.kitpvp.game.Game;
import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAddon extends PlaceholderExpansion {

    private final PlaceholderManager placeholderManager;

    public PlaceholderAddon(PlaceholderManager placeholderManager) {
        this.placeholderManager = placeholderManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mortiskitpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "none030";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("kit")) {
            Kit kit = placeholderManager.getKitManager().getKit(player);
            if (kit != null) {
                return kit.getName();
            }
            return placeholderManager.getMessage("NO_KIT");
        }
        if (params.equalsIgnoreCase("map")) {
            Game game = placeholderManager.getKillStreakManager().getGameManager().getGameByPlayer().get(player);
            if (game != null) {
                return game.getArena().getName();
            }
            return null;
        }
        if (params.equalsIgnoreCase("author")) {
            Game game = placeholderManager.getKillStreakManager().getGameManager().getGameByPlayer().get(player);
            if (game != null) {
                return game.getArena().getAuthor();
            }
            return null;
        }
        if (params.equalsIgnoreCase("killstreak")) {
            Long kills = placeholderManager.getKillStreakManager().getKillStreakByPlayer().get(player);
            if (kills != null) {
                return Long.toString(kills);
            }
            return "0";
        }
        return null;
    }
}
