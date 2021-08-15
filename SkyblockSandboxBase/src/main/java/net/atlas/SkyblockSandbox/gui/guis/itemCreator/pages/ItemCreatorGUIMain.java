package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilityEditorGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Stream;

public class ItemCreatorGUIMain extends NormalGUI {

    public ItemCreatorGUIMain(SBPlayer owner) {
        super(owner);
    }

    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            event.getWhoClicked().closeInventory();
        });
        setAction(4, event -> {
            AnvilGUI gui = new AnvilGUI((Player) event.getWhoClicked(), event1 -> {
                SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
                if (event1.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)) {
                    event1.setWillDestroy(false);
                    event1.setWillClose(false);
                    return;
                }
                if (event1.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                    ItemMeta meta = player.getItemInHand().getItemMeta();

                    for (String s : ItemLoreGUI.PROFANITIES) {
                        if (ChatColor.stripColor(event1.getName()).toLowerCase().contains(s)) {
                            player.sendMessage(SUtil.colorize("&cYou cannot use that word in your item name!"));
                            player.closeInventory();
                            return;
                        }
                    }


                    meta.setDisplayName(SUtil.colorize(event1.getName()));
                    player.getItemInHand().setItemMeta(meta);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            open(player);
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                }
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.NAME_TAG, "Enter item name", 1, 0));
            gui.open();
        });
        setAction(13, event -> {
            new ItemLoreGUI(getOwner()).open();
        });
        setAction(15, event -> {
            new AbilityEditorGUI(getOwner()).open();
        });
        setAction(22, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            if (player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                new TagsEditorGUI(player).open();
            } else {
                player.sendMessage("§cYour item must have a displayname and lore first in order to set it's tags!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
            }
        });
        setAction(14, event -> {
            new StatsEditorGUI(getOwner()).open();
        });
        setAction(12, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            if (player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                new RaritiesGUI(getOwner()).open();
            } else {
                player.sendMessage("§cYour item must have a displayname and lore first in order to set it's rarity!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
            }
        });
        setAction(11, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            if (player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                if (Stream.of(Material.STAINED_CLAY, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.INK_SACK, Material.WOOL, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE).anyMatch(material -> player.getItemInHand().getType().equals(material))) {
                    player.closeInventory();
                    player.performCommand("dye");
                } else {
                    player.sendMessage("§cThat isn't a dyeable item!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                }
            } else {
                player.sendMessage("§cYour item must have a displayname and lore first in order to set it's color!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
            }
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Create an item";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setMenuGlass();

        setItem(40, makeColorfulItem(Material.BARRIER, "§cClose", 1, 0));
        setItem(4, makeColorfulItem(Material.NAME_TAG, "§aRename Item", 1, 0,
                SUtil.colorize("&7Rename the item you have", "&7in your hand!", "", "&eClick to rename!")));

        setItem(13, makeColorfulItem(Material.PAPER, "§aEdit Item Lore", 1, 0, SUtil.colorize("&7Edit the lore of the item you", "&7have in your hand!", "", "&eClick to edit!")));
        setItem(14, makeColorfulItem(Material.GOLDEN_APPLE, "§aEdit Item Stats", 1, 0, SUtil.colorize("&7Edit the stats the item has!", "&7Including Defense, Health, and Intelligence!", "", "&eClick to edit!")));
        setItem(12, makeColorfulItem(Material.PAINTING, "§aSet item Rarity", 1, 0, SUtil.colorize("&7Set the rarity of your item", "&7you can choose anything", "&7between: &fCommon&7, &aUncommon", "&9Rare&7, &5Epic&7, &6Legendary&7,", "&dMythic&7, &cSpecial&7.", "", "§cNote: The last line of", "§clore in your item will", "§cturn into the rarity name.", "", "&eClick to set!")));
        setItem(15, makeColorfulItem(Material.GLOWSTONE_DUST, "§aSet item ability", 1, 0, SUtil.colorize("&7Create your own custom ability!", "&7Using the Base Abilites or", "&7the Advanced functions!", "", "&eClick to create!")));
        setItem(22, makeColorfulItem(Material.ENCHANTED_BOOK, "§aEdit Item Tags", 1, 0, SUtil.colorize("&7Edit the tags the item has!", "&7Including Unbreakable, Enchant tag,", "&7Glowing tag, and the Damage Tag!", "", "&eClick to edit!")));
        setItem(11, makeColorfulItem(Material.INK_SACK, "§aSet item color", 1, 5, SUtil.colorize("&7Edit the color of the item!", "&7you can change any item", "&7from: &aWool &7, &aStained_Clay &7, ", "&aStained_Glass_Panes &7, &aStained_Glass  ", "&7or &aINK_SACK(DYES)")));

    }
}
