package net.atlas.SkyblockSandbox.gui.guis.itemCreator;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemCreator  {

    private SBPlayer owner;
    private ItemCreatorPage page;
    private int index;

    public ItemCreator(SBPlayer owner,ItemCreatorPage page,int index,int indexx) {
        this.owner = owner;
        this.page = page;
        this.index = index;
    }
    public ItemCreator(SBPlayer owner,ItemCreatorPage page,int index) {
        this.owner = owner;
        this.page = page;
        this.index = index;
    }
    public ItemCreator(SBPlayer owner,ItemCreatorPage page) {
        this.owner = owner;
        this.page = page;
    }

    public ItemCreator(Player owner,ItemCreatorPage page,int index,int indexx) {
        this.owner = new SBPlayer(owner);
        this.page = page;
        this.index = index;
    }
    public ItemCreator(Player owner,ItemCreatorPage page,int index) {
        this.owner = new SBPlayer(owner);
        this.page = page;
        this.index = index;
    }
    public ItemCreator(Player owner,ItemCreatorPage page) {
        this.owner = new SBPlayer(owner);
        this.page = page;
    }

    public void open() {
        page.getGui(owner).open();
    }

    public void open(int index) {
        page.getGui(owner,index).open();
    }

    public void open(int index,int indexx) {
        page.getGui(owner,index,indexx).open();
    }

}
