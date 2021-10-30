package net.atlas.SkyblockSandbox.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ItemType {
    SWORD("SWORD"),
    BOW("BOW"),
    BOOTS("BOOTS"),
    LEGGINGS("LEGGINGS"),
    CHESTPLATE("CHESTPLATE"),
    HELMET("HELMET"),
    ARMOR("BOOTS", "CHESTPLATE", "LEGGINGS", "HELMET"),
    ITEM(""),
    WAND("WAND"),
    ACCESSORY("ACCESSORY"),
    DUNGEON_ITEM("DUNGEON ITEM"),
    DUNGEON_BOW("DUNGEON BOW"),
    DUNGEON_SWORD("DUNGEON SWORD"),
    AXE("AXE"),
    SHEARS("SHEARS"),
    PICKAXE("PICKAXE"),
    DRILL("DRILL"),
    HOE("HOE"),
    SUMMONING("SUMMONING ITEM"),
    PET_ITEM("PET ITEM");

    private String s;
    private final ArrayList<String> alias = new ArrayList<>();

    ItemType(String s) {
        this.s = s;
    }
    ItemType(String... s) {
        Collections.addAll(alias, s);
    }

    public String getValue() {
        return s;
    }

    public List<String> getList() {
        return alias;
    }

    public static ItemType typeFromString(String s) {
        ItemType type = ITEM;
        if (s == null) return type;
        for (ItemType types : values()) {
            if (s.equals(type.s)) {
                type = types;
            }
        }
        return type;
    }
}
