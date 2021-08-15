package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.maploop.items.Items;
import net.maploop.items.data.AbilityData;
import net.maploop.items.data.EnumAbilityData;
import net.maploop.items.gui.GUI;
import net.maploop.items.gui.PlayerMenuUtility;
import net.maploop.items.util.IUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BaseAbilitiesGUI extends SBGUI {
    private final int index;

    public BaseAbilitiesGUI(PlayerMenuUtility playerMenuUtility, int index) {
        super(playerMenuUtility);
        this.index = index;
    }

    @Override
    public String getTitle() {
        return "Select base ability";
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(inventory.equals(player.getInventory())) return;
        ItemStack item = player.getItemInHand();

        switch (event.getCurrentItem().getType()) {
            case ARROW:
                new AbilityCreatorGUI(new PlayerMenuUtility(player), index).open();
                break;
            case BOOK_AND_QUILL: {
                if(event.isLeftClick()) {
                    if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals(event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().replace(" ", "_").replace("§D", "")))
                        return;
                    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                    player.setItemInHand(AbilityData.setData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().replace(" ", "_").replace("§D", ""), index));
                    Bukkit.getScheduler().runTaskLater(Items.getInstance(), () -> player.setItemInHand(AbilityData.setData(EnumAbilityData.FUNCTION, player.getItemInHand(), "RIGHT_CLICK", index)), 2);
                    Bukkit.getScheduler().runTaskLater(Items.getInstance(), super::open, 4);
                    break;
                } else if(event.isRightClick()){
                    if (AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("NONE"))
                        return;
                    player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1, 1);
                    player.setItemInHand(AbilityData.setData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), "NONE", index));
                    Bukkit.getScheduler().runTaskLater(Items.getInstance(), super::open, 4);
                }
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        setFillerGlass();
        Player player = playerMenuUtility.getOwner();
        ItemStack item = player.getItemInHand();

        inventory.setItem(31, makeItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));

        if(AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("WITHER_IMPACT"))
            inventory.setItem(10, makeItem(Material.BOOK_AND_QUILL, "§dWither Impact", 1, 0, IUtil.colorize("&8Ability Scroll\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&aAlready selected!\n&bRight-Click to remove!"), true));
        else
            inventory.setItem(10, makeItem(Material.BOOK_AND_QUILL, "§dWither Impact", 1, 0, IUtil.colorize("&8Ability Scroll\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&eClick to select!")));

        if(AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, index).equals("INSTANT_TRANSMISSION"))
            inventory.setItem(11, makeItem(Material.BOOK_AND_QUILL, "§dInstant Transmission", 1, 0, IUtil.colorize("&8Item Ability\n\n&7Teleports you &a8 &7blocks\n&7ahead of you.\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&aAlready selected!\n&bRight-Click to remove!"), true));
        else
            inventory.setItem(11, makeItem(Material.BOOK_AND_QUILL, "§dInstant Transmission", 1, 0, IUtil.colorize("&8Item Ability\n\n&7Teleports you &a8 &7blocks\n&7ahead of you.\n\n&7Function: &aRIGHT_CLICK\n\n&7Description not included\n\n&eClick to select!")));
    }
}
