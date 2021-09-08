package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.Serialization;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PetsMenu extends PaginatedGUI {
    List<ItemStack> pets = new ArrayList<>();
    public boolean convertToItem = false;
    private MongoCoins db = SBX.getMongoStats();
    public PetsMenu(SBPlayer owner) {
        super(owner);
    }

    public PetsMenu(SBPlayer owner,boolean convertToItem) {
        super(owner);
        this.convertToItem = convertToItem;
    }

    @Override
    public int getPageSize() {
        return 36;
    }

    @Override
    public boolean setClickActions() {
        setAction(49,event -> {
            getGui().close(getOwner());
        });
        setAction(48,event -> {
            if(!getGui().previous()) {
                new SBMenu(getOwner()).open();
            }
        });
        setAction(50,event -> {
            getGui().next();
        });
        setAction(51,event -> {
            convertToItem = !convertToItem;
            new PetsMenu(getOwner(),convertToItem).open();
        });
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if(Boolean.parseBoolean(NBTUtil.getString(event.getCurrentItem(),"is-equipped"))) {
            if (convertToItem) {
                getOwner().playSound(getOwner().getLocation(), Sound.ORB_PICKUP, 5, 1);
                convertToItem = false;
                ItemStack pet = event.getCurrentItem();
                pet = NBTUtil.removeTag(pet,"mf-gui");
                String petStr = Serialization.itemStackToBase64(pet);

                db.removeData(getOwner().getUniqueId(), petStr);
                if (getOwner().hasSpace()) {
                    getOwner().getInventory().addItem(NBTUtil.setString(pet,"false","is-equipped"));
                }

                new PetsMenu(getOwner(),convertToItem).open();
            }
        }
    }

    @Override
    public String getTitle() {
        return "Pets";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        for(int i = 0;i<getGui().getInventory().getSize();i++) {
            getGui().removeItem(i);
        }
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).asGuiItem());

        List<ItemStack> items = db.getPets(getOwner().getUniqueId());

        if(!(items ==null)) {
            if (!items.isEmpty()) {
                for (ItemStack item : items) {
                    getGui().addItem(ItemBuilder.from(item).asGuiItem());
                }
            }
        }
        if(convertToItem) {
            setItem(51,makeColorfulItem(Material.INK_SACK, "&aTurn Pet Into Item",1,DyeColor.LIME.getDyeData(),"&7Turn your pet into back into an item."));
        } else {
            setItem(51,makeColorfulItem(Material.INK_SACK, "&aTurn Pet Into Item",1, DyeColor.GRAY.getDyeData(),"&7Turn your pet into back into an item."));
        }
    }
}
