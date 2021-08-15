package net.atlas.SkyblockSandbox.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Logger;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Getter
public abstract class PaginatedGUI extends SBGUI{
    private SBPlayer owner;

    private PaginatedGui gui;

    public PaginatedGUI(SBPlayer owner) {
        super(owner);
        this.owner = owner;
    }

    public void setItem(int index, ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setItem(Gui gui,int index,ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setMenuGlass() {
        gui.getFiller().fill(ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());
    }

    public abstract int getPageSize();

    public abstract void setItems();

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
                            .title(Component.text(getTitle()))
                            .rows(getRows())
                            .pageSize(getPageSize())
                            .create();
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