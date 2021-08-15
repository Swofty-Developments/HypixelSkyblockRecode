package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreator;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreatorPage;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreGUI extends SBGUI {

    public static final String[] PROFANITIES = {
            "amy", "fuck", "shit", "ass", "nigga", "nigger", "niggger", "niggerr", "cum", "sex", "niger",
            "uwu", "owo", "motherfucker", "fucker", "maploop", "mightykloon", "swooftyy", "swofty", "sin_ender",
            "dngreenbean", "duop", "numpties", "amy224", "amy221", "lmfao", "mappy-chan", "mapy-chan", "coom", "dick",
            "pussy", "shlong", "virgin", "virginity", "vagina", "vaagina", "mom", "cock", "cok", "cocc", "dong",
            "bitch", "bitchass"
    };

    public ItemLoreGUI(SBPlayer player) {
        super(player);

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        SBPlayer player = getOwner();
        event.setCancelled(true);
        if (event.getCurrentItem().getType().equals(Material.PAPER)) {
            if (event.getClick().equals(ClickType.RIGHT)) {
                String[] s = event.getCurrentItem().getItemMeta().getDisplayName().split("#");
                int i = Integer.parseInt(s[1]) - 1;
                ItemMeta meta = player.getItemInHand().getItemMeta();
                List<String> lore = player.getItemInHand().getItemMeta().getLore();
                lore.remove(lore.get(i));
                meta.setLore(lore);
                player.getItemInHand().setItemMeta(meta);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new ItemCreator(player, ItemCreatorPage.SET_LORE).open();
                    }
                }.runTaskLater(SBX.getInstance(), 2);
            } else {
                if (event.getClick().equals(ClickType.MIDDLE)) {
                    String[] s = event.getCurrentItem().getItemMeta().getDisplayName().split("#");
                    int i = Integer.parseInt(s[1]) - 1;

                    ItemMeta meta = player.getItemInHand().getItemMeta();
                    List<String> l = new ArrayList<>(meta.getLore());
                    l.remove(l.get(i));
                    meta.setLore(l);
                    player.getItemInHand().setItemMeta(meta);
                    SBItemStack b = new SBItemStack(player.getItemInHand());
                    player.setItemInHand(b.addDescriptionLine((player.getItemInHand()), NBTUtil.getString(event.getCurrentItem(), "Lore_" + (i + 1))));
                    player.playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 0);
                    player.sendMessage(ChatColor.GREEN + "Success! Transferred line #" + (i + 1) + " to item description!");
                    new ItemCreator(player, ItemCreatorPage.SET_LORE).open();
                } else {
                    String[] s = event.getCurrentItem().getItemMeta().getDisplayName().split("#");
                    int i = Integer.parseInt(s[1]) - 1;

                    AnvilGUI gui = new AnvilGUI(player.getPlayer(), new AnvilGUI.AnvilClickEventHandler() {
                        @Override
                        public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                            if (event.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)) {
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                            if (event.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                                String n = SUtil.colorize(event.getName());
                                ItemMeta meta = player.getItemInHand().getItemMeta();
                                List<String> l = new ArrayList<>(meta.getLore());
                                l.set(i, n);
                                meta.setLore(l);
                                player.getItemInHand().setItemMeta(meta);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        new ItemCreator(player, ItemCreatorPage.SET_LORE).open();
                                    }
                                }.runTaskLater(SBX.getInstance(), 2);
                            }
                        }
                    });
                    ItemMeta meta = player.getItemInHand().getItemMeta();
                    List<String> l;
                    if(meta.getLore()!=null) {
                        l = new ArrayList<>(meta.getLore());
                    } else {
                       l = new ArrayList<>();
                    }
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, l.get(i).replace("§", "&"), 1, 0));
                    gui.open();
                }
            }
        }
    }

    @Override
    public boolean setClickActions() {
        SBPlayer player = getOwner();
        setAction(31, event -> {
            new ItemCreator(new SBPlayer((Player) event.getWhoClicked()), ItemCreatorPage.MAIN).open();
        });
        setAction(35, event -> {
            if (player.getItemInHand().hasItemMeta()) {
                if (player.getItemInHand().getItemMeta().hasLore()) {
                    if (player.getItemInHand().getItemMeta().getLore().size() > 48) {
                        player.sendMessage("§e------------------------------------------------");
                        player.sendMessage("§cYou have reached the item lore limit on your item!");
                        player.sendMessage("§cIf you want to increase it, you can buy a rank");
                        player.sendMessage("at §6§nhttps://store.skyblocksandbox.net§c!");
                        player.sendMessage("§e------------------------------------------------");
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        return;
                    }
                }
            }
            AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
                if (event1.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)) {
                    event1.setWillClose(false);
                    event1.setWillDestroy(false);
                }
                if (event1.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                    String n = SUtil.colorize(event1.getName());
                    if (player.getItemInHand().getItemMeta().hasLore()) {
                        ItemMeta meta = player.getItemInHand().getItemMeta();
                        List<String> l = new ArrayList<>(meta.getLore());

                        for (String s : PROFANITIES) {
                            if (ChatColor.stripColor(n).toLowerCase().contains(s)) {
                                player.sendMessage("§cYou cannot use that word in your lore!");
                                player.closeInventory();
                                return;
                            }
                        }

                        l.add(n);

                        meta.setLore(l);
                        player.getItemInHand().setItemMeta(meta);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new ItemCreator(player, ItemCreatorPage.SET_LORE).open();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);

                        return;
                    }
                    ItemMeta meta = player.getItemInHand().getItemMeta();
                    List<String> l = new ArrayList<>();
                    l.add(n);
                    meta.setLore(l);
                    player.getItemInHand().setItemMeta(meta);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new ItemCreator(player, ItemCreatorPage.SET_LORE).open();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                }
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Lore input", 1, 0));
            gui.open();
        });
        return false;
    }

    @Override
    public String getTitle() {
        return "Edit Item Lore";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        SBPlayer player = getOwner();
        getGui().getFiller().fillBorder(ItemBuilder.from(FILLER_GLASS).asGuiItem());
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an item"));

        setItem(35, makeColorfulItem(Material.WOOL, "§aAdd line", 1, 5, SUtil.colorize("&7Add a line of lore to", "&7your item!", "", "&eClick to add!")));

        setItem(34, makeColorfulItem(Material.FEATHER, "§aFormat the lore", 1, 0, SUtil.colorize("&7Lore looking bad?", "&7Click to format the lore!")));

        if (player.getItemInHand().getItemMeta().hasLore()) {
            for (int i = 0; i < player.getItemInHand().getItemMeta().getLore().size(); ++i) {
                int w = i + 1;
                ItemStack b = makeColorfulItem(Material.PAPER, "§aLore #" + w, 1, 0, SUtil.colorize("§7Lore Content:", "", player.getItemInHand().getItemMeta().getLore().get(i) + "", "", "&eClick to edit!", "&eRight-Click to remove", "", "&c&lMIDDLE CLICK to transfer lore line to description!", "&c&lIf this is not done, all not transferred will be deleted when updating lore."));
                b = NBTUtil.setString(b, player.getItemInHand().getItemMeta().getLore().get(i), "Lore_" + w);
                getGui().addItem(ItemBuilder.from(b).asGuiItem());
            }
        }
    }

}
