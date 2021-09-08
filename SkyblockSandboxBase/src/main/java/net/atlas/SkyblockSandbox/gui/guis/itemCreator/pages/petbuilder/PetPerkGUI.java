package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.*;

public class PetPerkGUI extends NormalGUI {
    public PetPerkGUI(SBPlayer owner) {
        super(owner);
    }

    int addAmt = 11;

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            new PetBuilderGUI(getOwner()).open();
        });
        for (int i = 0; i < 5; i++) {
            int ii = i + 1;
            setAction(i + addAmt, event -> {
                new PerkBuilderGUI(getOwner(), ii).open();
            });
        }
        return true;
    }

    @Override
    public String getTitle() {
        return "Set pet perks";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        int perks = NBTUtil.getPerkAmt(storedPets.get(getOwner().getUniqueId()).build());
        for (int i = 0; i < 5; i++) {
            int ii = i + 1;
            if (ii <= perks) {
                setItem(i + addAmt, makeColorfulItem(Material.BOOK,
                        "&aPerk " + (ii),
                        1,
                        0,
                        "", "&bPerk name: " + NBTUtil.getPerkName(storedPets.get(getOwner().getUniqueId()).build(), ii)));
            } else {
                setItem(i + addAmt, makeColorfulItem(Material.WOOD_BUTTON,
                        "&aEmpty Perk Slot",
                        1,
                        0,
                        "", "&eClick to add a perk!"));
            }
        }
        setItem(31, makeColorfulItem(Material.ARROW, "&cGo Back", 1, 0, ""));
    }
}
