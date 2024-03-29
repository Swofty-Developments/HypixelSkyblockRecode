package net.atlas.SkyblockSandbox.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import lombok.Setter;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Logger;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class NormalGUI extends SBGUI {

    private SBPlayer owner;

    private Gui gui;

    public NormalGUI(SBPlayer owner) {
        super(owner);
        this.owner = owner;
    }

    public void setItem(int index,ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setContents(ItemStack[] contents) {
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null) {
                setItem(i, new ItemStack(Material.AIR));
                continue;
            }

            setItem(i, contents[i]);
        }
    }

    public void setItem(Gui gui,int index,ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setMenuGlass() {
        gui.getFiller().fill(ItemBuilder.from(FILLER_GLASS).asGuiItem());
    }

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract boolean setClickActions();

    public void updateItems() {
        setItems();
        getGui().update();
        getGui().open(getOwner());
    }


    public Gui gui() {
        return gui;
    }

    public void open() {
        if(owner!=null) {
            if(gui==null) {
                if(getTitle()==null) {
                    Logger.logError(this.getClass(), "SBGUI: the Gui title cannot be null!");
                } else {
                    this.gui = Gui.gui()
                            .title(Component.text(getTitle()))
                            .rows(getRows())
                            .create();
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
                    if (this instanceof Backable) {
                        Backable backable = (Backable) this;

                    }
                    gui.open(owner);
                }
            }
        } else {
            Logger.logError(this.getClass(),"SBGUI: Cannot open a Gui with the owner or player value equal to null!");
        }

    }

    public void setAction(int slot, GuiAction<InventoryClickEvent> e) {
        gui.addSlotAction(slot,e);
    }

}
