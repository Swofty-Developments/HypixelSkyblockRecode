package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import com.google.common.base.Enums;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StatsEditorGUI extends NormalGUI {

    public StatsEditorGUI(SBPlayer owner) {
        super(owner);

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            setAction(i + 10, event -> {

            });
        }
        setAction(49, event -> {
            new ItemCreatorGUIMain(getOwner()).open();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Edit Item Stats";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBottom(ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        getGui().getFiller().fillBetweenPoints(1, 1, 5, 1, ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        getGui().getFiller().fillBetweenPoints(1, 9, 5, 9, ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        getGui().getFiller().fillBetweenPoints(1, 3, 5, 3, ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        getGui().getFiller().fillBetweenPoints(1, 5, 5, 5, ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        getGui().getFiller().fillBetweenPoints(1, 7, 5, 7, ItemBuilder.from(FILLER_GLASS).name(Component.text(SUtil.colorize("&7"))).asGuiItem());
        for (SBPlayer.PlayerStat s : SBPlayer.PlayerStat.values()) {
            getGui().addItem(ItemBuilder.from(s.getStack()).lore(Component.text(""), Component.text(SUtil.colorize("&bClick to set the " + s.getStack().getItemMeta().getDisplayName() + " &bamount!"))).setNbt("Stat", s.name()).asGuiItem());
        }
        for (int in : getGui().getGuiItems().keySet()) {
            for (SBPlayer.PlayerStat s : SBPlayer.PlayerStat.values()) {
                if (getGui().getGuiItem(in) != null) {
                    if (!NBTUtil.getGenericString(getGui().getGuiItem(in).getItemStack(), "Stat").equals("")) {
                        setAction(in,event -> {
                            AnvilGUI gui = setstatGUI(Objects.requireNonNull(Objects.requireNonNull(Enums.getIfPresent(SBPlayer.PlayerStat.class, NBTUtil.getGenericString(Objects.requireNonNull(getGui().getGuiItem(in)).getItemStack(), "Stat"))).orNull()), getOwner().getPlayer());
                        });

                    }
                }

            }
        }
        //setMenuGlass();
        setItem(49, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an Item"));


        /*getGui().setItem(10, ItemBuilder.from(makeColorfulItem(Material.GOLDEN_APPLE, "§aSet Health", 1, 0, "&7Edit the amount of &c❤ Health", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.HEALTH.name()).asGuiItem());
        getGui().setItem(11, ItemBuilder.from(makeColorfulItem(Material.EYE_OF_ENDER, "§aSet Intelligence", 1, 0, "&7Edit the amount of &b✎ Intelligence", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.INTELLIGENCE.name()).asGuiItem());
        getGui().setItem(12, ItemBuilder.from(makeColorfulItem(Material.IRON_CHESTPLATE, "§aSet Defense", 1, 0, "&7Edit the amount of &a❈ Defense", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.DEFENSE.name()).asGuiItem());
        getGui().setItem(13, ItemBuilder.from(makeColorfulItem(Material.IRON_SWORD, "§aSet Strength", 1, 0, "&7Edit the amount of &c❁ Strength", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.STRENGTH.name()).asGuiItem());
        getGui().setItem(14, ItemBuilder.from(makeColorfulItem(Material.BLAZE_POWDER, "§aSet Damage", 1, 0, "&7Edit the amount of &c❁ Damage", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.DAMAGE.name()).asGuiItem());
        getGui().setItem(15, ItemBuilder.from(makeColorfulSkullItem("§aSet Crit Chance", "http://textures.minecraft.net/texture/3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1, "&7Edit the amount of §9☣ Crit Chance", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.CRIT_CHANCE.name()).asGuiItem());
        getGui().setItem(16, ItemBuilder.from(makeColorfulSkullItem("§aSet Crit Damage", "http://textures.minecraft.net/texture/ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1, "&7Edit the amount of §9☠ Crit Damage", "&7your item has!", "", "&eClick to set!")).setNbt("Stat", SBPlayer.PlayerStat.CRIT_DAMAGE.name()).asGuiItem());*/

    }

    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }

    public AnvilGUI setstatGUI(SBPlayer.PlayerStat stat, Player player) {
        String formattedname = stat.name().charAt(0) + stat.name().substring(1).toLowerCase();
        AnvilGUI gui = new AnvilGUI(player, event1 -> {
            if (!NumUtils.isInt(event1.getName())) {
                invalidNumberError(event1, player);
                return;
            }
            if (Integer.parseInt(event1.getName()) > 100000) {
                player.sendMessage("§cThe " + formattedname + " amount can't be more than 100,000!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                player.closeInventory();
                return;
            }
            if (Integer.parseInt(event1.getName()) <= 0) {
                player.sendMessage("§cThe " + formattedname + " amount can't be less than 1!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                player.closeInventory();
                return;
            }
            if (NumUtils.isInt(event1.getName()) && event1.getName() != null) {
                String s = event1.getName();
                int strength = Integer.parseInt(s);

                ItemStack is = player.getItemInHand();
                ItemMeta im = is.getItemMeta();
                ArrayList<String> new_lore;
                if (player.getItemInHand().getItemMeta().getLore() == null) {
                    new_lore = new ArrayList<>();
                    new_lore.add("§7" + ChatColor.translateAlternateColorCodes('&', "&7" + formattedname + ": §a+" + strength));
                    im.setLore(new_lore);
                    is.setItemMeta(im);
                    SBItemStack it = new SBItemStack(is);
                    ItemStack i = it.setInteger(is, strength, stat.name());
                    player.setItemInHand(i);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded " + event1.getName() + " " + stat.name().toLowerCase() + " to the item in your hand!"));
                } else {
                    new_lore = new ArrayList<>();
                    Iterator<String> var12 = player.getItemInHand().getItemMeta().getLore().iterator();

                    String str;
                    while (var12.hasNext()) {
                        str = var12.next();
                        if (!str.contains("§7" + ChatColor.translateAlternateColorCodes('&', formattedname + ": §a+"))) {
                            new_lore.add(str);
                        } else {
                            new_lore.add("§7" + ChatColor.translateAlternateColorCodes('&', formattedname + ": §a+" + strength));
                        }
                    }

                    str = "§7" + ChatColor.translateAlternateColorCodes('&', formattedname + " §a+" + strength);
                    if (!new_lore.contains(str)) {
                        new_lore.add(str);
                    }

                    im.setLore(new_lore);
                    is.setItemMeta(im);
                    SBItemStack it = new SBItemStack(is);
                    ItemStack i = it.setInteger(is, strength, stat.name());
                    player.setItemInHand(i);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded " + event1.getName() + " " + stat.name().toLowerCase() + " to the item in your hand!"));
                }
            } else {
                invalidNumberError(event1, player);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    new StatsEditorGUI(getOwner()).open();
                }
            }.runTaskLater(SBX.getInstance(), 1);
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter value", 1, 0));
        gui.open();
        return gui;
    }
}
