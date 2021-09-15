package net.atlas.SkyblockSandbox.abilityCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Logger;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class PaginatedFunctionGUI extends SBGUI {
    private SBPlayer owner;
    protected int aindex;
    protected int findex;
    protected AdvancedFunctions backGUI;

    private PaginatedGui gui;

    public PaginatedFunctionGUI(SBPlayer owner) {
        super(owner);
        this.owner = owner;
    }

    public void setItem(int index, ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setItem(Gui gui, int index, ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setMenuGlass() {
        gui.getFiller().fill(ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());
    }

    public abstract int getPageSize();

    public void updateItems() {
        setItems();
        getGui().update();
        getGui().open(getOwner());
    }

    public abstract boolean setClickActions();

    public abstract void handleMenu(InventoryClickEvent event);

    public PaginatedGui gui() {
        return gui;
    }

    public void open() {
        if(owner!=null) {
            if(gui==null) {
                if(getTitle()==null) {
                    Logger.logError(this.getClass(), "SBGUI: the Gui title cannot be null!");
                } else {
                    this.gui = Gui.paginated()
                            .rows(getRows())
                            .title(Component.text("null"))
                            .pageSize(getPageSize())
                            .create();
                    gui.updateTitle(getTitle());
                    setItems();
                    if (!setClickActions()) {
                        gui.setDefaultClickAction(this::handleMenu);
                    } else {
                        gui.setDefaultClickAction(event -> {
                            event.setCancelled(true);
                        });
                    }
                    gui.open(owner);
                }
            }
        } else {
            Logger.logError(this.getClass(),"SBGUI: Cannot open a Gui with the owner or player value equal to null!");
        }

    }

    public void open(SBPlayer owner) {
        this.owner = owner;
        this.open();
    }

    public void setAction(int slot, GuiAction<InventoryClickEvent> e) {
        gui.addSlotAction(slot,e);
    }

}