package me.none030.mortiskitpvp.kitpvp.duels;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.arenas.ArenaManager;
import me.none030.mortiskitpvp.kitpvp.duels.invite.Invite;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuelManager extends Manager {

    private final ArenaManager arenaManager;
    private final GameManager gameManager;
    private final long expireTime;
    private final List<Invite> invites;
    private final HashMap<Player, Invite> inviteByInvited;
    private final HashMap<Player, Invite> inviteByInviter;

    public DuelManager(ArenaManager arenaManager, GameManager gameManager, long expireTime) {
        this.arenaManager = arenaManager;
        this.gameManager = gameManager;
        this.expireTime = expireTime;
        this.invites = new ArrayList<>();
        this.inviteByInvited = new HashMap<>();
        this.inviteByInviter = new HashMap<>();
        check();
        MortisKitPvp plugin = MortisKitPvp.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new DuelListener(this), plugin);
        plugin.getServer().getPluginCommand("duel").setExecutor(new DuelCommand(this));
    }

    private void check() {
        for (Invite invite : invites) {
            invite.setTimer(invite.getTimer() + 1);
            if (invite.getTimer() > expireTime) {
                invites.remove(invite);
                continue;
            }
            if (invite.isAccepted()) {
                gameManager.start(invite);
                removePlayer(invite.getInviter());
                removePlayer(invite.getInvited());
            }
        }
    }

    public void removePlayer(Player player) {
        if (inviteByInvited.containsKey(player)) {
            Invite invite = inviteByInvited.get(player);
            inviteByInvited.remove(player);
            inviteByInviter.remove(invite.getInviter());
            invites.remove(invite);
        }
        if (inviteByInviter.containsKey(player)) {
            Invite invite = inviteByInviter.get(player);
            inviteByInviter.remove(player);
            inviteByInvited.remove(invite.getInvited());
            invites.remove(invite);
        }
    }

    public void addInvite(Invite invite) {
        removePlayer(invite.getInviter());
        inviteByInvited.put(invite.getInvited(), invite);
        inviteByInviter.put(invite.getInviter(), invite);
        invites.add(invite);
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public HashMap<Player, Invite> getInviteByInvited() {
        return inviteByInvited;
    }

    public HashMap<Player, Invite> getInviteByInviter() {
        return inviteByInviter;
    }
}
