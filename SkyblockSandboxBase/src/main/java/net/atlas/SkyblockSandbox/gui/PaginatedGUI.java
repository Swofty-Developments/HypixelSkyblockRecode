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
import org.bukkit.Material;
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
                    if (this instanceof Backable) {
                        Backable backable = (Backable) this;
                        setItem(backable.backItemSlot(), makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To " + backable.backTitle()));
                        gui.addSlotAction(backable.backItemSlot(), e -> {
                            backable.openBack();
                        });

                        setItem(backable.backItemSlot()+1,makeColorfulItem(Material.BARRIER,"&cClose",1,0));
                        gui.addSlotAction(backable.backItemSlot()+1,event -> {
                            gui.close(getOwner());
                        });
                    }
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