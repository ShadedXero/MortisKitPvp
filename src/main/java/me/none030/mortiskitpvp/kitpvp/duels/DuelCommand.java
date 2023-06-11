package me.none030.mortiskitpvp.kitpvp.duels;

import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.duels.invite.DuelInvite;
import me.none030.mortiskitpvp.kitpvp.duels.invite.Invite;
import me.none030.mortiskitpvp.kitpvp.duels.invite.PartyInvite;
import me.none030.mortiskitpvp.kitpvp.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DuelCommand implements TabExecutor {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final DuelManager duelManager;

    public DuelCommand(DuelManager duelManager) {
        this.duelManager = duelManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            return false;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("accept")) {
                if (!player.hasPermission("mortiskitpvp.duel.accept")) {
                    player.sendMessage(duelManager.getMessage("NO_PERMISSION"));
                    return false;
                }
                Game game = duelManager.getGameManager().getGameByPlayer().get(player);
                if (game != null) {
                    player.sendMessage(duelManager.getMessage("ALREADY_IN_GAME"));
                    return false;
                }
                Invite invite = duelManager.getInvite(duelManager.getInviteByInvited().get(player));
                if (invite == null) {
                    player.sendMessage(duelManager.getMessage("NO_INVITE"));
                    return false;
                }
                duelManager.removePlayer(invite.getInviter());
                duelManager.removePlayer(invite.getInvited());
                duelManager.getInvites().remove(invite);
                duelManager.getGameManager().start(invite);
                player.sendMessage(duelManager.getMessage("INVITE_ACCEPTED"));
                return true;
            }
            if (!player.hasPermission("mortiskitpvp.duel")) {
                player.sendMessage(duelManager.getMessage("NO_PERMISSION"));
                return false;
            }
            Game game = duelManager.getGameManager().getGameByPlayer().get(player);
            if (game != null) {
                player.sendMessage(duelManager.getMessage("ALREADY_IN_GAME"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (player.equals(target)) {
                    player.sendMessage(duelManager.getMessage("CANNOT_SELF_DUEL"));
                    return false;
                }
                DuelInvite invite = new DuelInvite(duelManager.getArenaManager().getRandom(), player, target, player.getName(), target.getName(), Collections.singletonList(player), Collections.singletonList(target));
                duelManager.addInvite(invite);
            }else {
                if (!plugin.hasParties()) {
                    return false;
                }
                PartiesAPI api = plugin.getPartiesAPI();
                Party playerParty = api.getPartyOfPlayer(player.getUniqueId());
                if (playerParty == null) {
                    player.sendMessage(duelManager.getMessage("NOT_IN_PARTY"));
                    return false;
                }
                if (playerParty.getLeader() == null || !playerParty.getLeader().equals(player.getUniqueId())) {
                    player.sendMessage(duelManager.getMessage("NOT_PARTY_LEADER"));
                    return false;
                }
                Party targetParty = api.getParty(args[0]);
                if (targetParty == null || targetParty.getLeader() == null) {
                    player.sendMessage(duelManager.getMessage("PARTY_NOT_FOUND"));
                    return false;
                }
                target = Bukkit.getPlayer(targetParty.getLeader());
                if (target == null) {
                    player.sendMessage(duelManager.getMessage("PARTY_LEADER_NOT_FOUND"));
                    return false;
                }
                if (playerParty.equals(targetParty)) {
                    player.sendMessage(duelManager.getMessage("CANNOT_SELF_DUEL"));
                    return false;
                }
                PartyInvite invite = new PartyInvite(duelManager.getArenaManager().getRandom(), player, target, playerParty.getName(), targetParty.getName(), playerParty, targetParty);
                duelManager.addInvite(invite);
            }
            player.sendMessage(duelManager.getMessage("INVITE_SENT"));
            target.sendMessage(duelManager.getMessage("INVITED").replace("%player_name%", player.getName()));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("accept");
            for (Player player : Bukkit.getOnlinePlayers()) {
                arguments.add(player.getName());
            }
            if (plugin.hasParties()) {
                PartiesAPI api = plugin.getPartiesAPI();
                for (Party party : api.getOnlineParties()) {
                    arguments.add(party.getName());
                }
            }
            return arguments;
        }
        return null;
    }
}
