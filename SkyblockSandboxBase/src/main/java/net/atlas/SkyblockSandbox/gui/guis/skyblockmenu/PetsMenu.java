package net.atlas.SkyblockSandbox.gui.guis.skyblockmenu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.Serialization;
import org.bson.Document;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.atlas.SkyblockSandbox.SBX.cachedPets;

public class PetsMenu extends PaginatedGUI {
    List<ItemStack> pets = new ArrayList<>();
    public boolean convertToItem = false;
    private MongoCoins db = SBX.getMongoStats();

    public PetsMenu(SBPlayer owner) {
        super(owner);
    }

    public PetsMenu(SBPlayer owner, boolean convertToItem) {
        super(owner);
        this.convertToItem = convertToItem;
    }

    @Override
    public int getPageSize() {
        return 36;
    }

    @Override
    public boolean setClickActions() {
        setAction(49, event -> {
            getGui().close(getOwner());
        });
        setAction(48, event -> {
            if (!getGui().previous()) {
                new SBMenu(getOwner()).open();
            }
        });
        setAction(50, event -> {
            getGui().next();
        });
        setAction(51, event -> {
            convertToItem = !convertToItem;
            new PetsMenu(getOwner(), convertToItem).open();
        });
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Document petsDoc = cachedPets.get(getOwner().getUniqueId());
        if (Boolean.parseBoolean(NBTUtil.getString(event.getCurrentItem(), "is-equipped"))) {
            ItemStack pet = event.getCurrentItem();
            if (convertToItem) {
                getOwner().playSound(getOwner().getLocation(), Sound.ORB_PICKUP, 5, 1);
                convertToItem = false;

                pet = NBTUtil.removeTag(pet, "mf-gui");
                String petStr = Serialization.itemStackToBase64(pet);

                if (getOwner().hasSpace()) {
                    getOwner().getInventory().addItem(NBTUtil.setString(NBTUtil.setString(pet, "false", "is-equipped"), "false", "is-active"));
                    petsDoc.remove("pet_" + petStr);
                }

                cachedPets.put(getOwner().getUniqueId(), petsDoc);

            } else {
                boolean isActive = Boolean.parseBoolean(NBTUtil.getString(pet, "is-active"));
                pet = NBTUtil.removeTag(pet, "mf-gui");
                String petStr1 = Serialization.itemStackToBase64(pet);
                pet = NBTUtil.setString(pet, String.valueOf((!isActive)), "is-active");
                String petStr2 = Serialization.itemStackToBase64(pet);
                petsDoc.remove("pet_" + petStr1);
                String activePet = petsDoc.getString("active-pet");
                if (!isActive) {
                    if(!(activePet==null)) {
                        if(!activePet.isEmpty()) {
                            for (Object s : petsDoc.values()) {
                                String ss = (String) s;
                                try {
                                    ItemStack it = Serialization.itemStackFromBase64(ss);
                                    if (Boolean.parseBoolean(NBTUtil.getString(it, "is-active"))) {
                                        it = NBTUtil.setString(it, "false", "is-active");
                                        String petStr3 = Serialization.itemStackToBase64(it);
                                        petsDoc.remove("pet_" + ss);
                                        petsDoc.put("pet_" + petStr3, petStr3);
                                        return;
                                    }
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                    petsDoc.put("active-pet", petStr2);
                } else {
                    petsDoc.put("active-pet", "");
                    petsDoc.put("pet_" + petStr2, petStr2);
                    cachedPets.put(getOwner().getUniqueId(), petsDoc);
                }
            }

            new PetsMenu(getOwner(), convertToItem).open();
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
        for (int i = 0; i < getGui().getInventory().getSize(); i++) {
            getGui().removeItem(i);
        }
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).asGuiItem());

        List<ItemStack> items = new ArrayList<>();
        Document petsDoc = cachedPets.get(getOwner().getUniqueId());
        for (Object s : petsDoc.values()) {
            String ss = (String) s;
            try {
                ItemStack pet = Serialization.itemStackFromBase64(ss);
                items.add(new SBItemStack(pet).refreshLore());
            } catch (IOException ignored) {
            }

        }


        if (!items.isEmpty()) {
            for (ItemStack item : items) {
                getGui().addItem(ItemBuilder.from(item).asGuiItem());
            }
        }
        if (convertToItem) {
            setItem(51, makeColorfulItem(Material.INK_SACK, "&aTurn Pet Into Item", 1, DyeColor.LIME.getDyeData(), "&7Turn your pet into back into an item."));
        } else {
            setItem(51, makeColorfulItem(Material.INK_SACK, "&aTurn Pet Into Item", 1, DyeColor.GRAY.getDyeData(), "&7Turn your pet into back into an item."));
        }
    }
}
