package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.storedPets;

public class PerkBuilderGUI extends NormalGUI {
    private int index;

    public PerkBuilderGUI(SBPlayer owner,int index) {
        super(owner);
        this.index = index;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(40,event -> {
            new PetPerkGUI(getOwner()).open();
        });
        setAction(13,event -> {
            AnvilGUI gui = new AnvilGUI(getOwner().getPlayer(), event1 -> {
                String name = SUtil.colorize(event1.getName());
                storedPets.get(getOwner().getUniqueId()).perkName(index,name);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new PerkBuilderGUI(getOwner(),index).open();
                    }
                }.runTaskLater(SBX.getInstance(), 1);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter value", 1, 0));
            gui.open();
        });
        setAction(22,event -> {
            AnvilGUI gui = new AnvilGUI(getOwner().getPlayer(), event1 -> {
                String name = SUtil.colorize(event1.getName());
                storedPets.get(getOwner().getUniqueId()).addPerkDescriptionLine(index,SUtil.colorize(name));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new PerkBuilderGUI(getOwner(),index).open();
                    }
                }.runTaskLater(SBX.getInstance(), 1);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter value", 1, 0));
            gui.open();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Pet perk - " + index;
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(40, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "&cTo pet builder"));

        setItem(13, makeColorfulItem(Material.NAME_TAG, "§aSet perk name", 1, 0,
                SUtil.colorize("&7Set the name of your perk!","","&cNOTICE: Inappropriate perk names","&cwill result in a warn/ban.","","&eClick to set name!")));

        setItem(22,makeColorfulItem(Material.BOOK_AND_QUILL, ChatColor.GREEN + "Add ability description",1,0,ChatColor.GRAY + "Set the ability description."));

    }
}
