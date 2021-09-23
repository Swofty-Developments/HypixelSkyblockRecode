package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionsMainGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityCreatorGUI extends NormalGUI {
    private final int index;

    public AbilityCreatorGUI(SBPlayer owner, int index) {
        super(owner);
        this.index = index;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        Player player = getOwner().getPlayer();
        setAction(13, event -> {
            AnvilGUI gui = new AnvilGUI(player, event12 -> {
                if (event12.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)) {
                    event12.setWillDestroy(false);
                    event12.setWillClose(false);
                    return;
                }
                if (event12.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                    SBItemStack i = new SBItemStack(player.getItemInHand());
                    if (i.getAbilityData(index, AbilityValue.MANA_COST).isEmpty()) {
                        player.setItemInHand(i.setAbilityData(index, AbilityValue.MANA_COST, 0));
                        i = new SBItemStack(player.getItemInHand());
                        player.setItemInHand(i.setAbilityData(index, AbilityValue.NAME, event12.getName()));
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new AbilityCreatorGUI(getOwner(), index).open();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                }
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.NAME_TAG, "Name input", 1, 0));
            player.setItemOnCursor(null);
            gui.open();
        });
        setAction(21, event -> {
            AnvilGUI gui = new AnvilGUI(player, event1 -> {
                try {
                    Integer p = Integer.parseInt(event1.getName());
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cThat's not a valid number!");
                    new AbilityCreatorGUI(getOwner(), index).open();
                    return;
                }
                if (Integer.parseInt(event1.getName()) > 600) {
                    player.sendMessage("§cThe cooldown cannot be more than 600!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                    player.closeInventory();
                    return;
                }
                if (Integer.parseInt(event1.getName()) < 0) {
                    player.sendMessage("§cThe cooldown cannot be less than 0!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                    player.closeInventory();
                    return;
                }
                if (event1.getName() != null) {
                    SBItemStack i = new SBItemStack(player.getItemInHand());
                    player.setItemInHand(i.setAbilityData(index, AbilityValue.COOLDOWN, event1.getName()));
                } else {
                    invalidNumberError(event1, player);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new AbilityCreatorGUI(getOwner(), index).open();
                    }
                }.runTaskLater(SBX.getInstance(), 2);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your cooldown", 1, 0));
            gui.open();
        });
        setAction(14, event -> {
            SBItemStack i = new SBItemStack(player.getItemInHand());
            player.setItemInHand(AbilityUtil.setAbilityData(player.getItemInHand(), index, AbilityValue.CLICK_TYPE, event.getClick().toString().toUpperCase() + "_CLICK"));
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            updateItems();
        });
        setAction(22, event -> {
            AnvilGUI gui = new AnvilGUI(player, event13 -> {
                if (!NumUtils.isInt(event13.getName())) {
                    invalidNumberError(event13, getOwner().getPlayer());
                    return;
                }
                if (Integer.parseInt(event13.getName()) > 10000) {
                    player.sendMessage("§cThe mana cost cannot be more than 10,000!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                    return;
                }
                if (Integer.parseInt(event13.getName()) < 0) {
                    player.sendMessage("§cThe mana cost cannot be less than 0!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                    return;
                }
                if (NumUtils.isInt(event13.getName()) && event13.getName() != null) {
                    SBItemStack bb = new SBItemStack(player.getItemInHand());
                    bb.setAbilityData(index, AbilityValue.MANA_COST, event13.getName());
                    player.setItemInHand(bb.asBukkitItem());
                } else {
                    invalidNumberError(event13, player);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new AbilityCreatorGUI(getOwner(), index).open();
                    }
                }.runTaskLater(SBX.getInstance(), 2);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your mana cost", 1, 0));
            gui.open();
        });
        setAction(30, event -> {
            new SetAbilityDescriptionMenu(getOwner(), index).open();
        });
        setAction(31, event -> {
            new FunctionsMainGUI(getOwner(), index).open();
        });
        setAction(49, event -> {
            new AbilitySelectorGUI(getOwner()).open();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Create Ability #" + index;
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(49, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Edit Item Ability"));

        setItem(13, makeColorfulItem(Material.NAME_TAG, "§aSet ability name", 1, 0,
                SUtil.colorize("&7Set the name of your ability!", "", "&cNOTICE: Inappropriate ability names", "&cwill result in a warn/ban.", "", "&eClick to set name!")));

        setItem(21, makeColorfulItem(Material.WATCH, "§aSet ability cooldown", 1, 0,
                SUtil.colorize("&7Set the cooldown of your ability!", "", "§7Maximum: &a600s", "", "&eClick to set!")));

        setItem(22, makeColorfulItem(Material.EYE_OF_ENDER, "§aSet item Mana cost", 1, 0, SUtil.colorize(
                "&7Set the amount of&b intelligence", "&7your ability costs to use!", "", "&7Maximum: &a10000", "", "&eClick to set!"
        )));
        setItem(30, makeColorfulItem(Material.BOOK_AND_QUILL, ChatColor.GREEN + "Add ability description", 1, 0, ChatColor.GRAY + "Set the ability description."));
        setItem(31, makeColorfulItem(Material.COMMAND, "§aAdvanced Functions", 1, 0, "§7You can add functions to", "§7your item to make it weirder!", "§7You can add up to §a5", "§7functions to your items.", "", "§eClick to view!"));
        SBItemStack item = new SBItemStack(getOwner().getItemInHand());
        getOwner().setItemInHand(AbilityUtil.setGenericAbilityString(item.asBukkitItem(), "has-ability", "true"));
        setItem(14, makeColorfulItem(Material.FEATHER, "&aSet Click Type", 1, 0, "&7Set the click type of your", "&7ability", "&7Currently set: " + AbilityUtil.getAbilityData(item.asBukkitItem(), index, AbilityValue.CLICK_TYPE) + "", "", "&eMake any click to set!"));
    }


    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }

}
