package net.atlas.SkyblockSandbox.AuctionHouse.guis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.signGUI.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionDurationGUI extends NormalGUI implements Backable {
    public static Map<UUID, Integer> Selected = new HashMap();
    private final double price;

    public AuctionDurationGUI(SBPlayer owner) {
        super(owner);
        this.price = (Double)AuctionCreatorGUI.playerPrice.get(owner.getUniqueId());
    }

    public void handleMenu(InventoryClickEvent event) {
    }

    public boolean setClickActions() {
        SBPlayer player = this.getOwner();
        this.setAction(10, (e) -> {
            Selected.put(this.getOwner().getUniqueId(), 10);
            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), 1.0F);
            (new AuctionCreatorGUI(this.getOwner(), false)).open();
        });
        this.setAction(11, (e) -> {
            Selected.put(this.getOwner().getUniqueId(), 11);
            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), 6.0F);
            (new AuctionCreatorGUI(this.getOwner(), false)).open();
        });
        this.setAction(12, (e) -> {
            Selected.put(this.getOwner().getUniqueId(), 12);
            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), 12.0F);
            (new AuctionCreatorGUI(this.getOwner(), false)).open();
        });
        this.setAction(13, (e) -> {
            Selected.put(this.getOwner().getUniqueId(), 13);
            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), 24.0F);
            (new AuctionCreatorGUI(this.getOwner(), false)).open();
        });
        this.setAction(14, (e) -> {
            Selected.put(this.getOwner().getUniqueId(), 14);
            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), 48.0F);
            (new AuctionCreatorGUI(this.getOwner(), false)).open();
        });
        this.setAction(16, (e) -> {
            if (e.isLeftClick()) {
                Selected.put(this.getOwner().getUniqueId(), 16);
                (new SignGUI(SBX.getInstance().signManager, (event) -> {
                    if (NumUtils.isInt(event.getLines()[0])) {
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
                            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), Float.valueOf(event.getLines()[0]));
                            (new AuctionCreatorGUI(this.getOwner(), false)).open();
                        }, 3L);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.0F);
                        player.sendMessage("§cThat's not a valid number!");
                    }

                })).withLines(new String[]{"", "^^^^^^^^^^^^^^^", "Auction", "hours"}).open(player.getPlayer());
            } else if (e.isRightClick()) {
                Selected.put(this.getOwner().getUniqueId(), 16);
                (new SignGUI(SBX.getInstance().signManager, (event) -> {
                    if (NumUtils.isInt(event.getLines()[0])) {
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {
                            AuctionCreatorGUI.playerTime.put(this.getOwner().getUniqueId(), Float.parseFloat(event.getLines()[0]) / 60.0F);
                            (new AuctionCreatorGUI(this.getOwner(), false)).open();
                        }, 3L);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.0F);
                        player.sendMessage("§cThat's not a valid number!");
                    }

                })).withLines(new String[]{"", "^^^^^^^^^^^^^^^", "Auction", "minutes"}).open(player.getPlayer());
            }

        });
        return true;
    }

    public String getTitle() {
        return "Auction Duration";
    }

    public int getRows() {
        return 4;
    }

    public void setItems() {
        this.setMenuGlass();
        Selected.putIfAbsent(this.getOwner().getUniqueId(), 11);
        AuctionCreatorGUI.playerTime.putIfAbsent(this.getOwner().getUniqueId(), 6.0F);
        this.setItem(10, makeColorfulItem(Material.STAINED_CLAY, "&a1 hour", 1, 14, "\n&eClick to pick!", (Integer)Selected.get(this.getOwner().getUniqueId()) == 10));
        this.setItem(11, makeColorfulItem(Material.STAINED_CLAY, "&a6 hour", 1, 6, "\n&eClick to pick!", (Integer)Selected.get(this.getOwner().getUniqueId()) == 11));
        this.setItem(12, makeColorfulItem(Material.STAINED_CLAY, "&a12 hour", 1, 1, "\n&eClick to pick!", (Integer)Selected.get(this.getOwner().getUniqueId()) == 12));
        this.setItem(13, makeColorfulItem(Material.STAINED_CLAY, "&a24 hour", 1, 4, "\n&eClick to pick!", (Integer)Selected.get(this.getOwner().getUniqueId()) == 13));
        this.setItem(14, makeColorfulItem(Material.STAINED_CLAY, "&a2 days", 1, 0, "\n&eClick to pick!", (Integer)Selected.get(this.getOwner().getUniqueId()) == 14));
        this.setItem(16, makeColorfulItem(Material.WATCH, "&aCustom Duration", 1, 0, "\n&bRight-click for minutes!\n&eClick to set hours!"));
    }

    public void openBack() {
        new AuctionCreatorGUI(this.getOwner(), false);
    }

    public String backTitle() {
        return "Auction creator";
    }

    public int backItemSlot() {
        return 31;
    }
}