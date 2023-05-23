package me.none030.mortiskitpvp;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.onarandombox.MultiverseCore.MultiverseCore;
import me.none030.mortiskitpvp.armorequipevent.ArmorListener;
import me.none030.mortiskitpvp.armorequipevent.DispenserArmorListener;
import me.none030.mortiskitpvp.kitpvp.KitPvpManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MortisKitPvp extends JavaPlugin {

    private static MortisKitPvp Instance;
    private MultiverseCore multiverseAPI;
    private PartiesAPI partiesAPI;
    private boolean placeholderAPI;
    private KitPvpManager kitPvpManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        multiverseAPI = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (multiverseAPI == null) {
            getServer().getPluginManager().disablePlugin(this);
        }
        if (getServer().getPluginManager().getPlugin("Parties") != null) {
            partiesAPI = Parties.getApi();
        }
        placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        kitPvpManager = new KitPvpManager();
    }

    public static MortisKitPvp getInstance() {
        return Instance;
    }

    public MultiverseCore getMultiverseAPI() {
        return multiverseAPI;
    }

    public boolean hasParties() {
        return partiesAPI != null;
    }

    public PartiesAPI getPartiesAPI() {
        return partiesAPI;
    }

    public boolean hasPlaceholderAPI() {
        return placeholderAPI;
    }

    public KitPvpManager getKitPvpManager() {
        return kitPvpManager;
    }
}
