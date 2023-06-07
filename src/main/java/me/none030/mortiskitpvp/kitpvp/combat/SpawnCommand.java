package me.none030.mortiskitpvp.kitpvp.combat;

import me.none030.mortiskitpvp.kitpvp.battlefield.Battlefield;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final CombatManager combatManager;

    public SpawnCommand(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("mortiskitpvp.spawn")) {
            player.sendMessage(combatManager.getMessage("NO_PERMISSION"));
            return false;
        }
        if (combatManager.getCombat(player) != null) {
            player.sendMessage(combatManager.getMessage("IN_COMBAT"));
            return false;
        }
        Battlefield battlefield = combatManager.getBattlefieldManager().getBattlefield();
        if (battlefield == null) {
            return false;
        }
        combatManager.getBattlefieldManager().getUnsafePlayers().remove(player);
        combatManager.getBattlefieldManager().getWithoutElytra().remove(player);
        battlefield.teleport(player);
        battlefield.addElytra(player);
        player.sendMessage(combatManager.getMessage("TELEPORTED"));
        return false;
    }
}
