package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AbilityEditorGUI extends NormalGUI {

    public AbilityEditorGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getCurrentItem().getType()) {
            case ARROW:
                new AbilityEditorGUI(getOwner()).open();
                break;
            case WOOD_BUTTON: {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    break;
                }
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
                switch (event.getSlot()) {
                    case 11:
                        new AbilityCreatorGUI(getOwner(), 1).open();
                        break;
                    case 12:
                        new AbilityCreatorGUI(getOwner(), 2).open();
                        break;
                    case 13:
                        new AbilityCreatorGUI(getOwner(), 3).open();
                        break;
                    case 14:
                        new AbilityCreatorGUI(getOwner(), 4).open();
                        break;
                    case 15:
                        new AbilityCreatorGUI(getOwner(), 5).open();
                        break;
                    default:
                        break;
                }
                break;
            }
        }
    }

    @Override
    public boolean setClickActions() {
        SBPlayer owner = getOwner();
        SBItemStack it = new SBItemStack(getOwner().getItemInHand());
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            setAction(i + 11, event -> {

                if (event.getClick().isLeftClick()) {
                    owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 2, 1);
                    new AbilityCreatorGUI(owner,finalI+1).open();
                } else {
                    if (event.getClick().isRightClick()) {
                        if (event.getCurrentItem().getType().equals(Material.WOOD_BUTTON)) {
                        } else {
                            owner.setItemInHand(it.removeAbil(it.asBukkitItem(), finalI + 1));
                            new AbilityEditorGUI(owner).open();
                            owner.playSound(owner.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                        }
                    }
                }
            });
        }
        setAction(31, event -> new ItemCreatorGUIMain(owner).open());
        return true;
    }

    @Override
    public String getTitle() {
        return "Edit Item Ability";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        SBPlayer player = getOwner();
        ItemStack item = player.getItemInHand();
        SBItemStack sbitem = new SBItemStack(item);

        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an item"));

        for (int index = 1; index <= 5; index++) {
            if (!(sbitem.hasAbility(index)))
                setItem(11 + (index - 1), makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, "&7This ability slot is empty!\n&7this means you can put a\n&7new ability in here!\n\n&7Slot index: &a" + index + "\n\n&eClick to add ability!"));
            else {
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("&7This slot has an ability!");
                lore.add("");
                if (sbitem.getAbilData(EnumAbilityData.NAME, index).equals("")) {
                    lore.add("&7Ability Name: &cNOT SET");
                } else {
                    lore.add("&7Ability Name: &b" + sbitem.getAbilData(EnumAbilityData.NAME, index));
                }
                lore.add("");
                if (sbitem.getAbilData(EnumAbilityData.COOLDOWN, index).equals("")) {
                    lore.add("&7Ability Cooldown: &a0");
                } else {
                    lore.add("&7Ability Cooldown: &a" + sbitem.getAbilData(EnumAbilityData.COOLDOWN, index));
                }
                if (sbitem.getAbilData(EnumAbilityData.MANA_COST, index).equals("")) {
                    lore.add("&7Ability Mana Cost: &a0");
                } else {
                    lore.add("&7Ability Mana Cost: &a" + sbitem.getAbilData(EnumAbilityData.MANA_COST, index));
                }
                if (sbitem.getAbilData(EnumAbilityData.FUNCTION, index).equals("")) {
                    lore.add("&7Click Type: &cNOT SET");
                } else {
                    lore.add("&7Click Type: &b" + sbitem.getAbilData(EnumAbilityData.FUNCTION, index));
                }
                if (sbitem.hasFunction(index)) {
                    lore.add("&7Advanced Functions:");
                    for (int i = 1; i <= 3; i++) {
                        String name = "" + AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, i);
                        if (!name.equals("")) {
                            lore.add("&b" + name.replace("_", " "));
                        }
                    }
                } else {
                    if (sbitem.getAbilData(EnumAbilityData.BASE_ABILITY, index).equals("")) {
                        lore.add("&7Base Ability: &bNONE");
                    } else {
                        lore.add("&7Base Ability: &b" + sbitem.getAbilData(EnumAbilityData.BASE_ABILITY, index));
                    }
                }
                lore.add("");
                lore.add("&eLeft-Click to view!");
                lore.add("&bRight-Click to remove!");
                setItem(11 + (index - 1), makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #" + index, 1, 0, lore));
            }
        }
    }
}
