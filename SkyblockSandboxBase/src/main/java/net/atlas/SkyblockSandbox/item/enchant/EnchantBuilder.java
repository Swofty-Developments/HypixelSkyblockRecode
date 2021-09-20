package net.atlas.SkyblockSandbox.item.enchant;

import org.bukkit.event.Event;

public class EnchantBuilder {


    public static EnchantBuilder init() {
        return new EnchantBuilder();
    }

    public EnchantBuilder event(Event event) {
        return this;
    }
}
