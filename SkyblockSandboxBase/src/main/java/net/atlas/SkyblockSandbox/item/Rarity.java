package net.atlas.SkyblockSandbox.item;

import org.bukkit.ChatColor;

public enum Rarity {
    COMMON(ChatColor.WHITE,"COMMON"),
    UNCOMMON(ChatColor.GREEN,"UNCOMMON"),
    RARE(ChatColor.BLUE,"RARE"),
    EPIC(ChatColor.DARK_PURPLE,"EPIC"),
    LEGENDARY(ChatColor.GOLD,"LEGENDARY"),
    MYTHIC(ChatColor.LIGHT_PURPLE,"MYTHIC"),
    SPECIAL(ChatColor.RED,"SPECIAL"),
    SUPER_SPECIAL(ChatColor.RED,"SUPER SPECIAL"),
    SUPREME(ChatColor.DARK_RED,"SUPREME"),
    DIVINE(ChatColor.AQUA, "DIVINE"),
    UNFINISHED(ChatColor.DARK_RED,"UNFINISHED"),
    SKYBLOCK_MENU(ChatColor.YELLOW,"Click to open!"),
    UNOBTAINABLE(ChatColor.DARK_AQUA,"UNOBTAINABLE");

    private final ChatColor color;
    private final String displayName;

    Rarity(ChatColor color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getName() {
        return displayName;
    }

    public boolean isRarerThan(Rarity rarity) {
        int current = getIndex();
        int param = rarity.getIndex();
        return (current > param);
    }

    public int getIndex() {
        int index = 0;
        for (Rarity rarity : values()) {
            if (equals(rarity))
                return index;
            index++;
        }
        return -1;
    }
}
