package me.none030.mortiskitpvp.kitpvp.game;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpectateCommand implements CommandExecutor {

    private final GameManager gameManager;

    public SpectateCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("mortiskitpvp.spectate")) {
            player.sendMessage(gameManager.getMessage("NO_PERMISSION"));
            return false;
        }
        Game game = gameManager.getGameByPlayer().get(player);
        if (game != null) {
            player.sendMessage(gameManager.getMessage("IN_GAME"));
            return false;
        }
        if (args.length != 1) {
            player.sendMessage(gameManager.getMessage("SPECTATE_USAGE"));
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(gameManager.getMessage("INVALID_TARGET"));
            return false;
        }
        Game targetGame = gameManager.getGameByPlayer().get(target);
        if (targetGame == null) {
            player.sendMessage(gameManager.getMessage("TARGET_NOT_IN_GAME"));
            return false;
        }
        GamePlayer gamePlayer = new GamePlayer(player, TeamType.NONE, "None");
        gamePlayer.setSpectating(targetGame.getArena(), targetGame.getWorld(), true);
        targetGame.getGamePlayers().add(gamePlayer);
        gameManager.getGameByPlayer().put(target, targetGame);
        return false;
    }
}
