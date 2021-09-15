package net.atlas.SkyblockSandbox.gui.guis.slayerGUI;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

import static net.atlas.SkyblockSandbox.SBX.activeSlayers;

public class ConfirmGUI extends NormalGUI {

    private SBPlayer owner;
    private Slayers slayer;
    private SlayerTier tier;
    private boolean cancel;

    public ConfirmGUI(SBPlayer owner, Slayers slayer, SlayerTier tier, boolean cancel) {
        super(owner);
        this.owner = owner;
        this.slayer = slayer;
        this.tier = tier;
        this.cancel = cancel;

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        if (cancel) {
            setAction(11, event -> {
                owner.playSound(owner.getLocation(), Sound.CLICK, 2, 2);
                owner.sendMessage(SUtil.colorize("&eYour slayer quest was cleared out!"));
                activeSlayers.remove(owner.getUniqueId());
                owner.closeInventory();
            });
        } else {
            setAction(11, event -> {
                owner.playSound(owner.getLocation(), Sound.ENDERDRAGON_GROWL, 2, 2);
                owner.closeInventory();
                owner.sendMessage(SUtil.colorize("&5&lSLAYER QUEST STARTED!"));
                owner.sendMessage(SUtil.colorize("&5> &7Slay &c" + tier.getXpAmt() + " Combat XP &7worth of " + slayer.getPlural() + " to spawn the boss."));
                HashMap<SlayerTier, Double> map1 = new HashMap<>();
                map1.put(tier, 0D);
                HashMap<Slayers, HashMap<SlayerTier, Double>> map2 = new HashMap<>();
                map2.put(slayer, map1);
                activeSlayers.put(owner.getUniqueId(), map2);
            });
        }
        setAction(15, event -> owner.closeInventory());
        return true;
    }

    @Override
    public String getTitle() {
        return "Confirm";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(11, makeColorfulItem(Material.STAINED_CLAY, "&aConfirm", 1, 13, "&7Kill " + slayer.getPlural().toLowerCase() + " to spawn the", "&7boss!", "", "&eClick to start quest!"));
        setItem(15, makeColorfulItem(Material.STAINED_CLAY, "&cCancel", 1, 14, "&7Click to cancel!"));
    }
}
