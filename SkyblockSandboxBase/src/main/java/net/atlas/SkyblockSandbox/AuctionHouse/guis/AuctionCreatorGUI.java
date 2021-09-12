package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.signgui.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class AuctionCreatorGUI extends NormalGUI implements Backable {
    public static HashMap<UUID, String> PlayerItems = new HashMap<>();
    public double price;
    public int timeInHours;
    public AuctionCreatorGUI(SBPlayer owner, boolean bin, double price, int timeInHours) {
        super(owner);
        this.price = price;
        this.timeInHours = timeInHours;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        SBPlayer player = getOwner();
        if (event.getClickedInventory().equals(getOwner().getInventory())) {
            if (!event.getCurrentItem().hasItemMeta()) {
                player.sendMessage("&cThis item must have a Display name or Lore!");
                return;
            }
            if (PlayerItems.containsKey(getOwner().getUniqueId())) return;
            PlayerItems.put(getOwner().getUniqueId(), BukkitSerilization.itemStackToBase64(event.getCurrentItem()));
            player.getInventory().setItem(event.getSlot(), null);
            updateItems();
            return;
        }
        switch (event.getSlot()) {
            case 13: {
                if (PlayerItems.containsKey(getOwner().getUniqueId())) {
                    player.getInventory().addItem(BukkitSerilization.itemStackFromBase64(PlayerItems.get(player.getUniqueId())));
                    PlayerItems.remove(getOwner().getUniqueId());
                    updateItems();
                }
                break;
            }
            case 29: {
                if (PlayerItems.containsKey(getOwner().getUniqueId())) {
                    UUID auctionID = UUID.randomUUID();
                    AuctionItemHandler.ITEMS.put(auctionID, new AuctionItemHandler(auctionID, getOwner().getUniqueId(), PlayerItems.get(getOwner().getUniqueId()), ZonedDateTime.now(ZoneId.of("-05:00")), ZonedDateTime.now(ZoneId.of("-05:00")), false, price, price, false));
                    System.out.println(AuctionItemHandler.ITEMS.get(auctionID));
                }
            }
            case 31: {
                /*new SignGUI(player, new String[] {"^^^^^^", "Enter your", "query."}, (player1, input) ->
                {
                    if (NumUtils.isInt(input))
                    {
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () ->
                        {
                            new AuctionCreatorGUI(getOwner(), false, Integer.parseInt(input)).open();
                        }, 3);
                    }
                    else
                    {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.sendMessage("§cThat's not a valid number!");
                    }
                });

                 */
                new SignGUI(SBX.getInstance().signManager, e-> {
                    if (NumUtils.isInt(e.getLines()[0]))
                    {
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () ->
                        {
                            new AuctionCreatorGUI(getOwner(), false, Integer.parseInt(e.getLines()[0]), 6).open();
                        }, 3);
                    }
                    else
                    {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.sendMessage("§cThat's not a valid number!");
                    }
                }).withLines("","^^^^^^^^^^^^^^^","Auction starting", "bid price")
                .open(player.getPlayer());
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Auction Creator";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        if (PlayerItems.containsKey(getOwner().getUniqueId())) {
            setItem(13, BukkitSerilization.itemStackFromBase64(PlayerItems.get(getOwner().getUniqueId())));
        } else {
            setItem(13, makeColorfulItem(Material.STONE_BUTTON, "&7No item", 1, 0));
        }
        setItem(31, makeColorfulItem(Material.POWERED_RAIL, "&fStarting bid &6" + price + " coins", 1, 0));
        if (PlayerItems.containsKey(getOwner().getUniqueId())) {
            setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§aCreate", 1, 5, "§eClick to create auction!"));
        } else {
            setItem(29, makeColorfulItem(Material.STAINED_CLAY, "§cCreate", 1, 14, "§cCan't create auction!"));
        }
        setItem(33, makeColorfulItem(Material.WATCH, "&fDuration: &e" + timeInHours, 1, 0));
    }

    @Override
    public void openBack() {
        new AuctionHouseGUI(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Auction House";
    }

    @Override
    public int backItemSlot() {
        return 49;
    }
}
