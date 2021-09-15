package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BaseAbilitiesGUI extends NormalGUI {
    private final int index;

    public BaseAbilitiesGUI(SBPlayer owner, int index) {
        super(owner);
        this.index = index;
    }

    @Override
    public String getTitle() {
        return "Select base ability";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        SBPlayer player = getOwner();
        ItemStack item = player.getItemInHand();
        for(int i=10;i<11;i++) {
            setAction(i,event -> {
                if (event.isLeftClick()) {
                    if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().replace(" ", "_"))))
                        return;
                    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                    player.setItemInHand(AbilityData.setData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().replace(" ", "_")), index));
                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> player.setItemInHand(AbilityData.setData(EnumAbilityData.FUNCTION, player.getItemInHand(), "RIGHT_CLICK", index)), 2);
                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), super::open, 4);
                } else if (event.isRightClick()) {
                    if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("NONE"))
                        return;
                    player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1, 1);
                    player.setItemInHand(AbilityData.setData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), "NONE", index));
                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), super::open, 4);
                }
            });
        }
        setAction(31,event -> {
            new AbilityCreatorGUI(getOwner(), index).open();
        });
        return true;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        Player player = getOwner();
        ItemStack item = player.getItemInHand();

        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));

        if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("WITHER_IMPACT"))
            setItem(10, makeColorfulItem(Material.BOOK_AND_QUILL, "§dWither Impact", 1, 0, "&8Ability Scroll\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&aAlready selected!\n&bRight-Click to remove!", true));
        else
            setItem(10, makeColorfulItem(Material.BOOK_AND_QUILL, "§dWither Impact", 1, 0, "&8Ability Scroll\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&eClick to select!"));

        if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("INSTANT_TRANSMISSION"))
            setItem(11, makeColorfulItem(Material.BOOK_AND_QUILL, "§dInstant Transmission", 1, 0, "&8Item Ability\n\n&7Teleports you &a8 &7blocks\n&7ahead of you.\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&aAlready selected!\n&bRight-Click to remove!", true));
        else
            setItem(11, makeColorfulItem(Material.BOOK_AND_QUILL, "§dInstant Transmission", 1, 0, "&8Item Ability\n\n&7Teleports you &a8 &7blocks\n&7ahead of you.\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&eClick to select!"));
    }
}
