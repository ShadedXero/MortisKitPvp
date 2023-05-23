package me.none030.mortiskitpvp.kitpvp.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LeaveCommand implements CommandExecutor {

    private final GameManager gameManager;

    public LeaveCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("mortiskitpvp.leave")) {
            player.sendMessage(gameManager.getMessage("NO_PERMISSION"));
            return false;
        }
        Game game = gameManager.getGameByPlayer().get(player);
        if (game == null) {
            player.sendMessage(gameManager.getMessage("NOT_IN_GAME"));
            return false;
        }
        GamePlayer gamePlayer = game.getGamePlayer(player);
        game.removePlayer(gameManager, gamePlayer);
        gameManager.getBattlefieldManager().getBattlefield().teleport(player);
        return false;
    }
}
