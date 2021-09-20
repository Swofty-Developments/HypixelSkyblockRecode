package net.atlas.SkyblockSandbox.gui.guis;

import com.sk89q.util.StringUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.TwoPlayerGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradeGUI extends TwoPlayerGUI {
    List<ItemStack> p1Items = new ArrayList<>();
    List<ItemStack> p2Items = new ArrayList<>();
    List<Integer> baseLeftSide = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30));
    List<Integer> baseRightSide = new ArrayList<>(Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44));
    List<Integer> p2rightside = new ArrayList<>(baseRightSide);
    List<Integer> p1rightside = new ArrayList<>(baseRightSide);
    List<Integer> p2leftside = new ArrayList<>(baseLeftSide);
    List<Integer> p1leftside = new ArrayList<>(baseLeftSide);
    BukkitTask prevTask = null;
    boolean player1confirm = false;
    boolean player2confirm = false;
    boolean canp1confirm = false;
    boolean canp2Confirm = false;
    private SBPlayer player1;
    private SBPlayer player2;

    public TradeGUI(SBPlayer owner, SBPlayer player1, SBPlayer player2) {
        super(owner, player1, player2);
        this.player1 = player1;
        this.player2 = player2;
    }

    void startTradeCountdown(Player player1, List<ItemStack> p1Itemss, Player player2, List<ItemStack> p2Itemss) {

        canp1confirm = false;
        canp2Confirm = false;
        if (prevTask != null) {
            prevTask.cancel();
        }
        prevTask = new BukkitRunnable() {

            int i = 3;

            @Override
            public void run() {


                getP1Gui().updateItem(39, ItemBuilder.from(makeColorfulItem(Material.STAINED_CLAY, "&eDeal timer! &7(&e" + i + "&7)", i, DyeColor.YELLOW.getData(), "&7The trade recently changed.\n&7Please review it before\n&7accepting.")).asGuiItem());
                getP2Gui().updateItem(39, ItemBuilder.from(makeColorfulItem(Material.STAINED_CLAY, "&eDeal timer! &7(&e" + i + "&7)", i, DyeColor.YELLOW.getData(), "&7The trade recently changed.\n&7Please review it before\n&7accepting.")).asGuiItem());

                if (i <= 0) {
                    this.cancel();
                    getP1Gui().updateItem(41, ItemBuilder.from(makeColorfulItem(Material.INK_SACK, "&ePending their confirm", 1, (byte) 8, "&7Trading with " + PlaceholderAPI.setPlaceholders(player2, "%luckperms_prefix%") + " " + player2.getName() + "&7.\n&7Waiting for them to confirm...")).asGuiItem());
                    getP2Gui().updateItem(41, ItemBuilder.from(makeColorfulItem(Material.INK_SACK, "&ePending their confirm", 1, (byte) 8, "&7Trading with " + PlaceholderAPI.setPlaceholders(player1, "%luckperms_prefix%") + " " + player1.getName() + "&7.\n&7Waiting for them to confirm...")).asGuiItem());
                    getP1Gui().updateItem(39, ItemBuilder.from(makeColorfulItem(Material.STAINED_CLAY, "&ePending their confirm", 1, DyeColor.GREEN.getData(), "&7Trading with " + PlaceholderAPI.setPlaceholders(player2, "%luckperms_prefix%") + " " + player2.getName() + "&7.\n&7Waiting for them to confirm...")).asGuiItem());
                    getP2Gui().updateItem(39, ItemBuilder.from(makeColorfulItem(Material.STAINED_CLAY, "&ePending their confirm", 1, DyeColor.GREEN.getData(), "&7Trading with " + PlaceholderAPI.setPlaceholders(player1, "%luckperms_prefix%") + " " + player1.getName() + "&7.\n&7Waiting for them to confirm...")).asGuiItem());
                    canp1confirm = true;
                    canp2Confirm = true;
                }
                i--;

                if (p1Items != p1Itemss || p2Items != p2Itemss) {
                    this.cancel();

                    //startTradeCountdown(player1, getP1Gui(), player2, getP2Gui());
                }
            }
        }.runTaskTimer(SBX.getInstance(), 5, 20);
    }

    void completeTrade(Player player1, Player player2) {
        player1.closeInventory();
        player2.closeInventory();
        player1.sendMessage(ChatColor.GREEN + "Trade complete!");
        for (ItemStack s : p1Items) {
            player2.getInventory().addItem(s);
            if(s.hasItemMeta()) {
                player2.sendMessage(ChatColor.DARK_GRAY + "+ x" + s.getAmount() + " " + s.getItemMeta().getDisplayName());
                player1.sendMessage(ChatColor.DARK_GRAY + "- x" + s.getAmount() + " " + s.getItemMeta().getDisplayName());
            } else {
                player2.sendMessage(ChatColor.DARK_GRAY + "+ x" + s.getAmount() + " " + SUtil.firstLetterUpper(s.getType().name()).replace("_"," "));
                player1.sendMessage(ChatColor.DARK_GRAY + "- x" + s.getAmount() + " " + SUtil.firstLetterUpper(s.getType().name()).replace("_"," "));
            }
        }
        player2.sendMessage(ChatColor.GREEN + "Trade complete!");
        for (ItemStack s : p2Items) {
            player1.getInventory().addItem(s);
            player1.sendMessage(ChatColor.DARK_GRAY + "+ x" + s.getAmount() + " " + s.getItemMeta().getDisplayName());
            player2.sendMessage(ChatColor.DARK_GRAY + "- x" + s.getAmount() + " " + s.getItemMeta().getDisplayName());
        }
    }

    @Override
    public void updateItems() {
        setItems();
        getP1Gui().update();
        //getP1Gui().open(player1);
        getP2Gui().update();
        //getP2Gui().open(player2);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        getP1Gui().setDefaultClickAction(event1 -> {
            event1.setCancelled(true);

            if (event1.getRawSlot() >= 45) {
                if(event1.getCurrentItem().getType()!=Material.AIR) {
                    p1Items.add(event1.getCurrentItem());
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_HAGGLE, 2, 1);
                    player2.playSound(player1.getLocation(), Sound.VILLAGER_HAGGLE, 2, 1);
                    player1.getInventory().setItem(event1.getSlot(), new ItemStack(Material.AIR));
                    setItems();
                    startTradeCountdown(player1, p1Items, player2, p2Items);
                }
            } else {
                event1.setCancelled(true);
                if (event1.getCurrentItem() != null && event1.getCurrentItem().hasItemMeta()) {
                    if (!baseLeftSide.contains(event1.getRawSlot())) {
                        return;
                    }
                    if (baseRightSide.contains(event1.getRawSlot())) {
                        return;
                    }
                    player1.getInventory().addItem(event1.getCurrentItem());
                    getP1Gui().updateItem(event1.getRawSlot(), new ItemStack(Material.AIR));
                    p1Items.remove(event1.getCurrentItem());
                    for (int i : baseRightSide) {
                        getP2Gui().removeItem(i);
                    }
                    List<Integer> slots = new ArrayList<>(baseRightSide);
                    for (ItemStack c : p1Items) {
                        getP2Gui().updateItem(slots.get(0), c);
                        slots.remove(0);
                    }
                    startTradeCountdown(player1, p1Items, player2, p2Items);
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_NO, 2, 1);
                    player2.playSound(player1.getLocation(), Sound.VILLAGER_NO, 2, 1);
                }
            }

        });
        getP2Gui().setDefaultClickAction(event2 -> {
            event2.setCancelled(true);
            if (event2.getRawSlot() >= 45) {
                if(event2.getCurrentItem().getType()!=Material.AIR) {
                    p2Items.add(event2.getCurrentItem());
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_HAGGLE, 2, 1);
                    player2.playSound(player1.getLocation(), Sound.VILLAGER_HAGGLE, 2, 1);
                    setItems();
                    player2.getInventory().setItem(event2.getSlot(), new ItemStack(Material.AIR));
                    startTradeCountdown(player1, p1Items, player2, p2Items);
                }
            } else {
                if (event2.getCurrentItem() != null && event2.getCurrentItem().hasItemMeta()) {
                    if (!baseLeftSide.contains(event2.getRawSlot())) {
                        return;
                    }
                    if (baseRightSide.contains(event2.getRawSlot())) {
                        return;
                    }
                    player2.getInventory().addItem(event2.getCurrentItem());
                    getP2Gui().updateItem(event2.getRawSlot(), new ItemStack(Material.AIR));
                    p2Items.remove(event2.getCurrentItem());
                    for (int i : baseRightSide) {
                        getP1Gui().removeItem(i);
                    }
                    List<Integer> slots = new ArrayList<>(baseRightSide);
                    for (ItemStack c : p2Items) {
                        getP1Gui().updateItem(slots.get(0), c);
                        slots.remove(0);
                    }
                    startTradeCountdown(player1, p1Items, player2, p2Items);
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_NO, 2, 1);
                    player2.playSound(player1.getLocation(), Sound.VILLAGER_NO, 2, 1);
                }
            }

        });
    }

    @Override
    public boolean setClickActions() {
        getP1Gui().addSlotAction(39, event -> {
            event.setCancelled(true);
            if (!player1confirm && canp1confirm) {
                if (player2confirm) {
                    player1confirm = true;
                    completeTrade(player1, player2);
                } else {
                    getP2Gui().updateItem(41, new ItemStack(Material.INK_SACK, 1, (byte) 6));
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_YES, 2, 1);
                    player2.playSound(player2.getLocation(), Sound.VILLAGER_YES, 2, 1);
                    player1confirm = true;
                }

            }
        });
        getP2Gui().addSlotAction(39, event -> {
            event.setCancelled(true);
            if (!player2confirm && canp2Confirm) {
                if (player1confirm) {
                    player2confirm = true;
                    completeTrade(player1, player2);
                } else {
                    getP1Gui().updateItem(41, new ItemStack(Material.INK_SACK, 1, (byte) 6));
                    player1.playSound(player1.getLocation(), Sound.VILLAGER_YES, 2, 1);
                    player2.playSound(player2.getLocation(), Sound.VILLAGER_YES, 2, 1);
                    player2confirm = true;
                }

            }
        });
        getP1Gui().setCloseGuiAction(inventoryCloseEvent -> {
            if (player1confirm && player2confirm) {
            } else {
                getP2Gui().close(player2);
                for (ItemStack i : p1Items) {
                    player1.getInventory().addItem(i);
                }
                for (ItemStack i : p2Items) {
                    player2.getInventory().addItem(i);
                }
                player1.sendMessage(ChatColor.RED + "The trade was cancelled!");
                player2.sendMessage(ChatColor.RED + "The trade was cancelled!");
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 0);
                player2.playSound(player2.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 0);
            }
        });
        getP2Gui().setCloseGuiAction(inventoryCloseEvent -> {
            if (player1confirm && player2confirm) {
            } else {
                getP1Gui().close(player1);
            }
        });
        return false;
    }

    @Override
    public String getp1title() {
        return "      You              " + player2.getName();
    }

    @Override
    public String getp2title() {
        return "      You              " + player1.getName();
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setItem(getP1Gui(), 36, makeColorfulItem(Material.GOLD_NUGGET, "&6Coins transaction", 1, 0, "\n&eClick to add gold!"));
        setItem(getP1Gui(), 39, makeColorfulItem(Material.STAINED_CLAY, "&aTrading!", 1, DyeColor.GREEN.getData(), "&7Click an item in your\n&7inventory to off it for\n&7trade."));
        setItem(getP2Gui(), 36, makeColorfulItem(Material.GOLD_NUGGET, "&6Coins transaction", 1, 0, "\n&eClick to add gold!"));
        setItem(getP2Gui(), 39, makeColorfulItem(Material.STAINED_CLAY, "&aTrading!", 1, DyeColor.GREEN.getData(), "&7Click an item in your\n&7inventory to off it for\n&7trade."));
        setItem(getP1Gui(), 41, makeColorfulItem(Material.INK_SACK, "&eNew deal", 1, 8, "&7Trading with " + PlaceholderAPI.setPlaceholders(player2, "%luckperms_prefix%") + " " + player2.getName() + "&7."));
        setItem(getP2Gui(), 41, makeColorfulItem(Material.INK_SACK, "&eNew deal", 1, 8, "&7Trading with " + PlaceholderAPI.setPlaceholders(player1, "%luckperms_prefix%") + " " + player1.getName() + "&7."));
        for(int i:baseRightSide) {
            getP1Gui().setItem(i,ItemBuilder.from(Material.AIR).asGuiItem());
            getP2Gui().setItem(i,ItemBuilder.from(Material.AIR).asGuiItem());
        }
        for(int i:baseLeftSide) {
            getP1Gui().setItem(i,ItemBuilder.from(Material.AIR).asGuiItem());
            getP2Gui().setItem(i,ItemBuilder.from(Material.AIR).asGuiItem());
        }
        int i = 0;
        for (ItemStack item : p1Items) {
            getP1Gui().updateItem(p1Items.indexOf(item), ItemBuilder.from(item).asGuiItem());
            getP2Gui().updateItem(p2rightside.get(i), ItemBuilder.from(item).asGuiItem());
            i++;
        }
        i = 0;
        for (ItemStack item : p2Items) {
            getP2Gui().updateItem(p2Items.indexOf(item), ItemBuilder.from(item).asGuiItem());
            getP1Gui().updateItem(p1rightside.get(i), ItemBuilder.from(item).asGuiItem());
            i++;
        }
        getP2Gui().getFiller().fillBetweenPoints(1, 5, 5, 5, ItemBuilder.from(super.FILLER_GLASS).asGuiItem());
        getP1Gui().getFiller().fillBetweenPoints(1, 5, 5, 5, ItemBuilder.from(super.FILLER_GLASS).asGuiItem());
    }
}
