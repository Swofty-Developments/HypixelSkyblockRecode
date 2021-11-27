package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto;

import com.google.common.base.Enums;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.signGUI.SignCompleteEvent;
import net.atlas.SkyblockSandbox.util.signGUI.SignGUI;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StatsEditorGUI extends NormalGUI implements Backable {

    public StatsEditorGUI(SBPlayer owner) {
        super(owner);

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {

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
            if(s == SBPlayer.PlayerStat.BREAKING_POWER) {
                getGui().setItem(4, ItemBuilder.from(s.getStack()).lore(Component.text(""), Component.text(SUtil.colorize("&bClick to set the " + s.getStack().getItemMeta().getDisplayName() + " &bamount!"))).setNbt("Stat", s.name()).asGuiItem());
            } else {
                getGui().addItem(ItemBuilder.from(s.getStack()).lore(Component.text(""), Component.text(SUtil.colorize("&bClick to set the " + s.getStack().getItemMeta().getDisplayName() + " &bamount!"))).setNbt("Stat", s.name()).asGuiItem());
            }
        }
        for (int in = 0; in < getGui().getInventory().getSize(); in++) {
            if (getGui().getGuiItem(in) != null) {
                if (!NBTUtil.getGenericString(getGui().getGuiItem(in).getItemStack(), "Stat").equals("")) {
                    String stat = NBTUtil.getGenericString(getGui().getGuiItem(in).getItemStack(), "Stat");
                    int finalIn = in;
                    setAction(in, event -> {
                        SignGUI gui = setstatGUI(Objects.requireNonNull(Enums.getIfPresent(SBPlayer.PlayerStat.class, stat).orNull()), getOwner().getPlayer());
                    });

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

    private void invalidNumberError(SignCompleteEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getLines()[0] + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
        new StatsEditorGUI(getOwner()).open();
    }

    public SignGUI setstatGUI(SBPlayer.PlayerStat stat, Player player) {
        String formattedname = SUtil.firstLetterUpper(stat.toString());
        SignGUI gui = new SignGUI(SBX.getInstance().signManager, event1 -> {
            if (!NumUtils.isInt(event1.getLines()[0])) {
                invalidNumberError(event1, player);
                return;
            }
            if (Integer.parseInt(event1.getLines()[0]) > stat.getMax()) {
                player.sendMessage("§cThe " + formattedname + " amount can't be more than " + NumUtils.format(stat.getMax(), "###,#") + "!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                player.closeInventory();
                return;
            }
            if (Integer.parseInt(event1.getLines()[0]) <= 0) {
                player.sendMessage("§cThe " + formattedname + " amount can't be less than 1!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                player.closeInventory();
                return;
            }
            if (NumUtils.isInt(event1.getLines()[0]) && event1.getLines()[0] != null) {
                String s = event1.getLines()[0];
                int strength = Integer.parseInt(s);

                ItemStack is = player.getItemInHand();
                player.setItemInHand(new SBItemBuilder(is).stat(stat, strength).build());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded " + event1.getLines()[0] + " " + stat.getDisplayName().replace("_", " ") + " to the item in your hand!"));
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
        gui.withLines("", "^^^^^^^^^^^^^^^", stat.getDisplayName(), "Stat");
        player.closeInventory();
        gui.open(player);
        return gui;
    }

    @Override
    public void openBack() {
        new ItemCreatorGUIMain(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Auto Item Creator";
    }

    @Override
    public int backItemSlot() {
        return 48;
    }
}
