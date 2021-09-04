package net.atlas.SkyblockSandbox.player.pets;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.function.Consumer;

public class PetPerk {

    private int perkIndex;
    private String name;
    private List<String> descript;

    public PetPerk(int perkIndex, String name, List<String> descript) {

        this.perkIndex = perkIndex;
        this.name = name;
        this.descript = descript;
    }

    public List<String> getDescription() {
        return descript;
    }

    public String getName() {
        return name;
    }

    public int getPerkIndex() {
        return perkIndex;
    }
}
