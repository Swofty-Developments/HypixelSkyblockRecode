package net.atlas.SkyblockSandbox.player.pets;

import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescript(List<String> descript) {
        this.descript = descript;
    }

    public void addDescript(String... descript) {
        if(this.descript==null) {
            this.descript = new ArrayList<>();
        }
        List<String> copy = new ArrayList<>(this.descript);
        List<String> supported = new ArrayList<>(SUtil.colorize(descript));
        copy.addAll(supported);
        this.descript = copy;
    }

    public void addDescript(List<String> descript) {
        List<String> copy = new ArrayList<>(this.descript);
        List<String> supported = new ArrayList<>(SUtil.colorize(descript));
        copy.addAll(supported);
        this.descript = copy;
    }

    public int getPerkIndex() {
        return perkIndex;
    }
}
