package net.atlas.SkyblockSandbox.gui.guis;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

public class ProfileViewer extends SBGUI {
    private Player openedProfile;

    public ProfileViewer(SBPlayer owner, String uuid) {
        super(owner);
        this.openedProfile = Bukkit.getPlayer(UUID.fromString(uuid));

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        setAction(16, event -> {
            getOwner().closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(getOwner(), "trade " + openedProfile.getName());
                }
            }.runTaskLater(SBX.getInstance(), 1L);
        });
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack a = openedProfile.getItemInHand();
                gui().updateItem(1, openedProfile.getItemInHand());
                if (!openedProfile.isOnline()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(SBX.getInstance(), 0, 10);
        gui().setCloseGuiAction(event -> {
            task.cancel();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return openedProfile.getName() + "'s Profile";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        setMenuGlass();


        if (!openedProfile.getItemInHand().getType().equals(Material.AIR) || openedProfile.getItemInHand() != null) {
            setItem(1, openedProfile.getItemInHand());
        }

        setItem((10), openedProfile.getInventory().getArmorContents()[3]);
        setItem((19), openedProfile.getInventory().getArmorContents()[2]);
        setItem((28), openedProfile.getInventory().getArmorContents()[1]);
        setItem((37), openedProfile.getInventory().getArmorContents()[0]);
        if (openedProfile.isValid()) {
            SBPlayer pl = new SBPlayer(openedProfile);
            DecimalFormat format = new DecimalFormat("###,###");
            setItem(22, makeColorfulSkullItem("&a" + openedProfile.getName() + "'s Profile", openedProfile.getName(), 1,
                    Arrays.asList("&cHealth &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.HEALTH)),
                            "&aDefense &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.DEFENSE)),
                            "&cStrength &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.STRENGTH)),
                            "&rSpeed &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.SPEED)),
                            "&9Crit Chance &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.CRIT_CHANCE) + "%"),
                            "&9Crit Damage &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.CRIT_DAMAGE) + "%"),
                            "&bIntelligence &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE)),
                            "&eBonus Attack Speed &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.ATTACK_SPEED)) + "%",
                            "&cFerocity &r" + format.format(pl.getMaxStat(SBPlayer.PlayerStat.FEROCITY)))));
            setItem(15, makeColorfulItem(Material.FEATHER, "&aVisit Player", 1, 0, "&c&lCOMING SOON"));
            setItem(16, makeColorfulItem(Material.EMERALD, "&aSend Trade request!", 1, 0, "&7Click to send a trade request!"));

            setItem(25, makeColorfulItem(Material.DIAMOND, "&aSend Coop request!", 1, 0, "&c&lCOMING SOON"));

        }
    }
}
