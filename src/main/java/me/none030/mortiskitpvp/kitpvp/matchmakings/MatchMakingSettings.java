package me.none030.mortiskitpvp.kitpvp.matchmakings;

import java.util.HashMap;

public class MatchMakingSettings {

    private final int menuSize;
    private final String menuTitle;
    private final HashMap<Integer, Integer> modeBySlot;

    public MatchMakingSettings(int menuSize, String menuTitle, HashMap<Integer, Integer> modeBySlot) {
        this.menuSize = menuSize;
        this.menuTitle = menuTitle;
        this.modeBySlot = modeBySlot;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public HashMap<Integer, Integer> getModeBySlot() {
        return modeBySlot;
    }
}
