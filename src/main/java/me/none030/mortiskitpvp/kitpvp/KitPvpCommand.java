package me.none030.mortiskitpvp.kitpvp;

import me.none030.mortiskitpvp.kitpvp.game.Game;
import me.none030.mortiskitpvp.kitpvp.kits.Kit;
import me.none030.mortiskitpvp.kitpvp.kits.KitMenu;
import me.none030.mortiskitpvp.kitpvp.matchmakings.MatchMakingMenu;
import me.none030.mortiskitpvp.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KitPvpCommand implements TabExecutor {

    private final KitPvpManager kitPvpManager;

    public KitPvpCommand(KitPvpManager kitPvpManager) {
        this.kitPvpManager = kitPvpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("kit")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new MessageUtils("&cThis command can only be executed by a player").color());
                return false;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("mortiskitpvp.kit")) {
                player.sendMessage(new MessageUtils("&cYou don't have permission to use this").color());
                return false;
            }
            if (args.length < 2) {
                KitMenu menu = new KitMenu(kitPvpManager.getKitManager(), player);
                menu.open(player);
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    if (!sender.hasPermission("mortiskitpvp.kit.add")) {
                        player.sendMessage(new MessageUtils("&cYou don't have permission to use this").color());
                        return false;
                    }
                    if (args.length != 6) {
                        player.sendMessage(new MessageUtils("&cUsage: /kitpvp kit add <id> <name> <permission> <icon-id>").color());
                        return false;
                    }
                    String id = args[2];
                    String name = args[3];
                    String permission = args[4];
                    String iconId = args[5];
                    ItemStack icon = kitPvpManager.getItemManager().getItem(iconId);
                    if (icon == null) {
                        player.sendMessage(new MessageUtils("&cPlease enter a valid item id").color());
                        return false;
                    }
                    Kit kit = new Kit(id, name, permission, iconId, icon, player.getInventory(), new ArrayList<>());
                    kitPvpManager.getKitManager().addKit(kit);
                    player.sendMessage(new MessageUtils("&cAdded the kit").color());
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("mortiskitpvp.kit.remove")) {
                        player.sendMessage(new MessageUtils("&cYou don't have permission to use this").color());
                        return false;
                    }
                    if (args.length != 3) {
                        player.sendMessage(new MessageUtils("&cUsage: /kitpvp kit remove <kit-id>").color());
                        return false;
                    }
                    Kit kit = kitPvpManager.getKitManager().getKitById().get(args[2]);
                    if (kit == null) {
                        player.sendMessage(new MessageUtils("&cKit could not be found").color());
                        return false;
                    }
                    kitPvpManager.getKitManager().removeKit(kit);
                    player.sendMessage(new MessageUtils("&cRemoved the kit").color());
                }
            }
        }
        if (args[0].equalsIgnoreCase("matchmaking")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new MessageUtils("&cThis command can only be executed by a player").color());
                return false;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("mortiskitpvp.matchmaking")) {
                player.sendMessage(new MessageUtils("&cYou don't have permission to use this").color());
                return false;
            }
            MatchMakingMenu menu = kitPvpManager.getMatchMakingManager().getMenu();
            menu.open(player);
        }
        if (args[0].equalsIgnoreCase("purge")) {
            if (!sender.hasPermission("mortiskitpvp.purge")) {
                sender.sendMessage(new MessageUtils("&cYou don't have permission to use this").color());
                return false;
            }
            for (Game game : kitPvpManager.getGameManager().getGames()) {
                game.end(kitPvpManager.getGameManager());
                kitPvpManager.getGameManager().getGames().remove(game);
            }
            sender.sendMessage(new MessageUtils("&cGames purged").color());
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("mortiskitpvp.reload")) {
                sender.sendMessage(new MessageUtils("&cThis command can only be executed by a player").color());
                return false;
            }
            kitPvpManager.reload();
            sender.sendMessage(new MessageUtils("&cMortisKitPvP Reloaded").color());
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("kit");
            arguments.add("matchmaking");
            arguments.add("purge");
            arguments.add("reload");
            return arguments;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("kit")) {
                List<String> arguments = new ArrayList<>();
                arguments.add("add");
                arguments.add("remove");
                return arguments;
            }
        }
        return null;
    }
}
