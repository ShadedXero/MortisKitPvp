package me.none030.mortiskitpvp.kitpvp.duels.invite;

import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import me.none030.mortiskitpvp.kitpvp.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartyInvite extends Invite {

    private final Party redParty;
    private final Party blueParty;

    public PartyInvite(Arena arena, Player inviter, Player invited, String redName, String blueName, Party redParty, Party blueParty) {
        super(arena, inviter, invited, redName, blueName);
        this.redParty = redParty;
        this.blueParty = blueParty;
    }

    @Override
    public List<Player> getRedPlayers() {
        List<Player> redPlayers = new ArrayList<>();
        for (PartyPlayer partyPlayer : redParty.getOnlineMembers()) {
            Player player = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
            if (player != null) {
                redPlayers.add(player);
            }
        }
        return redPlayers;
    }

    @Override
    public List<Player> getBluePlayers() {
        List<Player> bluePlayers = new ArrayList<>();
        for (PartyPlayer partyPlayer : blueParty.getOnlineMembers()) {
            Player player = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
            if (player != null) {
                bluePlayers.add(player);
            }
        }
        return bluePlayers;
    }

    public Party getRedParty() {
        return redParty;
    }

    public Party getBlueParty() {
        return blueParty;
    }
}
