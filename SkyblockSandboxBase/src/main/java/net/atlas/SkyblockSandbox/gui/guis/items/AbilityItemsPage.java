package net.atlas.SkyblockSandbox.gui.guis.items;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.item.SkyblockItems;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class AbilityItemsPage extends PaginatedGUI {
    public static final HashMap<UUID,String> search = new HashMap<>();
    public AbilityItemsPage(SBPlayer owner) {
        super(owner);
    }

    @Override
    public int getPageSize() {
        return 28;
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        SBPlayer p = new SBPlayer((Player) event.getWhoClicked());
        if(event.getClickedInventory().equals(p.getInventory())) return;
        switch (event.getSlot()) {
            case 45:
                if(getGui().getPrevPageNum()==1&&getGui().getCurrentPageNum()==1) {
                    p.sendMessage("&cYou are on the last page");
                }  else {
                    getGui().previous();
                }
                break;
            case 49: {
                new ItemMainPage(p).open();
                break;
            }
            case 50: {
                if(event.getClick().equals(ClickType.RIGHT)) {
                    if (search.containsKey(p.getUniqueId())) {
                        search.remove(p.getUniqueId());
                        p.playSound(p.getLocation(), Sound.CAT_MEOW, 1f, 1.5f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new AbilityItemsPage(p).open();
                            }
                        }.runTaskLater(SBX.getInstance(), 3);
                        return;
                    }
                    event.setCancelled(true);
                    return;
                }

                new AnvilGUI(p.getPlayer(), e -> {
                    if(e.getName() == null || e.getName() == "") {
                        p.sendMessage("&cInvalid search!");
                        return;
                    }

                    search.put(p.getUniqueId(), e.getName());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new AbilityItemsPage(p).open();
                        }
                    }.runTaskLater(SBX.getInstance(), 6);
                }).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter search", 1, 0, colorize("&6^^^^^^^\n&3Your Search"))).open();


                break;
            }
            case 53:
                if(getGui().getNextPageNum()==getGui().getPagesNum()&&getGui().getCurrentPageNum()==getGui().getPagesNum()) {
                    p.sendMessage("&cYou are on the last page");
                }  else {
                    getGui().next();
                }
                break;
            default:
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(colorize("&7 "))) return;
                if (p.hasSpace()) {
                    p.getInventory().addItem(event.getCurrentItem());
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                }
                break;
        }
    }

    @Override
    public String getTitle() {
        return "Ability items";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(colorize("&7 "))).asGuiItem());

        setItem(49, makeColorfulItem(Material.ARROW, "&aGo back", 1, 0, "&7Go back to the main page."));

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);

        ItemStack searchItem = makeColorfulItem(Material.ANVIL, "§aSearch Items", 1, 0,
                "§7Search through all Items.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeColorfulItem(Material.ANVIL, "§aSearch Items", 1, 0,
                colorize("&7Search through all Items.\n&7Filtered: &6" + search.get(getOwner().getUniqueId()) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        if (search.containsKey(getOwner().getUniqueId())) {
            setItem(50, searchItemsReset);
        } else {
            setItem(50, searchItem);

        }
        setItem(45, prev);
        for(ItemStack item : items(getOwner())) {
            getGui().addItem(ItemBuilder.from(item).asGuiItem());
        }
    }

    private Collection<ItemStack> items(SBPlayer player) {
        Collection<ItemStack> items = new ArrayList<>(SkyblockItems.getAllPublicItems());
        if (search.containsKey(player.getUniqueId())) {
            ArrayList<ItemStack> searchItems = new ArrayList<>();
            for (ItemStack item : items) {
                if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).toUpperCase().contains(search.get(player.getUniqueId()).toUpperCase())) {
                    searchItems.add(item);
                }
            }
            return searchItems;
        }
        return items;
    }
}
