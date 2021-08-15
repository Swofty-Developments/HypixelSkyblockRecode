package net.atlas.SkyblockSandbox.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
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
public abstract class SBGUI {

    private final SBPlayer owner;

    private Gui gui;

    public SBGUI(SBPlayer owner) {
        this.owner = owner;
    }

    protected ItemStack FILLER_GLASS = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15);

    public void setItem(int index,ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setItem(Gui gui,int index,ItemStack i) {
        gui.setItem(index, ItemBuilder.from(i).asGuiItem());
    }

    public void setMenuGlass() {
        gui.getFiller().fill(ItemBuilder.from(FILLER_GLASS).asGuiItem());
    }

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract boolean setClickActions();

    public abstract String getTitle();

    public abstract int getRows();

    public abstract void setItems();

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
        gui.open(owner);
    }

    public void setAction(int slot, GuiAction<InventoryClickEvent> e) {
        gui.addSlotAction(slot,e);
    }

    public ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String... lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.setLore(SUtil.colorize(Arrays.asList(lore)));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String lore, boolean glowing) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if(glowing) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String[] lore1 = lore.toArray(new String[0]);
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, String lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(Bukkit.getPlayer(owner)!=null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            item = i.applyTexture(item,owner);
        }
        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, List<String> lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(Bukkit.getPlayer(owner)!=null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            item = i.applyTexture(item,owner);
        }
        meta.setLore(SUtil.colorize(lore));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, String... lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(Bukkit.getPlayer(owner)!=null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            item = i.applyTexture(item,owner);
        }
        meta.setLore(SUtil.colorize(lore));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }
}
