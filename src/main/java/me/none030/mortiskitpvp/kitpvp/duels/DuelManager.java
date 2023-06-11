package me.none030.mortiskitpvp.kitpvp.duels;

import me.none030.mortiskitpvp.MortisKitPvp;
import me.none030.mortiskitpvp.kitpvp.Manager;
import me.none030.mortiskitpvp.kitpvp.arenas.ArenaManager;
import me.none030.mortiskitpvp.kitpvp.duels.invite.Invite;
import me.none030.mortiskitpvp.kitpvp.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DuelManager extends Manager {

    private final MortisKitPvp plugin = MortisKitPvp.getInstance();
    private final ArenaManager arenaManager;
    private final GameManager gameManager;
    private final long expireTime;
    private final List<Invite> invites;
    private final HashMap<Player, String> inviteByInvited;
    private final HashMap<Player, String> inviteByInviter;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Invite> inviteList = invites.iterator();
                while (inviteList.hasNext()) {
                    Invite invite = inviteList.next();
                    invite.setTimer(invite.getTimer() + 1);
                    if (invite.getTimer() > expireTime) {
                        removePlayer(invite.getInviter());
                        removePlayer(invite.getInvited());
                        inviteList.remove();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void removePlayer(Player player) {
        if (inviteByInvited.containsKey(player)) {
            String inviteId = inviteByInvited.get(player);
            if (inviteId != null) {
                Invite invite = getInvite(inviteId);
                if (invite != null) {
                    inviteByInvited.remove(player);
                    inviteByInviter.remove(invite.getInviter());
                }
            }
        }
        if (inviteByInviter.containsKey(player)) {
            String inviteId = inviteByInviter.get(player);
            if (inviteId != null) {
                Invite invite = getInvite(inviteId);
                if (invite != null) {
                    inviteByInviter.remove(player);
                    inviteByInvited.remove(invite.getInvited());
                }
            }
        }
    }

    public Invite getInvite(String id) {
        for (Invite invite : invites) {
            if (invite.getId().equals(id)) {
                return invite;
            }
        }
        return null;
    }

    public void addInvite(Invite invite) {
        removePlayer(invite.getInviter());
        inviteByInvited.put(invite.getInvited(), invite.getId());
        inviteByInviter.put(invite.getInviter(), invite.getId());
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

    public HashMap<Player, String> getInviteByInvited() {
        return inviteByInvited;
    }

    public HashMap<Player, String> getInviteByInviter() {
        return inviteByInviter;
    }
}
