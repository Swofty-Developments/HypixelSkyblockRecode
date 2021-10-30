package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.abilityCreator.Ability;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AbilitySelectorGUI extends NormalGUI {
    public AbilitySelectorGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Pick an Ability";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        ItemStack stack = getOwner().getItemInHand();
        int abilAmount = AbilityUtil.getAbilityAmount(stack);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            if (i + 1 <= abilAmount - 1) {
                Ability ability = new Ability(new SBItemBuilder(stack), i);
                int cooldown = ability.cooldown;
                String name = ability.name;
                ClickType type = ability.clickType;
                int manaCost = ability.manaCost;
                setItem(i + 20, makeColorfulItem(Material.COMMAND, "&aAbility #" + (i + 1),
                        1, 0, "&7Ability Name: &b" + (!name.equals("") ? name : "&cNOT SET"),
                        "&7Click Type: &a" + type + " CLICK",
                        "&7Cooldown: &a" + cooldown,
                        "&7Mana Cost: &a" + manaCost,
                        "",
                        "&eClick to edit!",
                        "&bRight-Click to remove"));


            } else {
                setItem(i + 20, makeColorfulItem(Material.WOOD_BUTTON, "&aCreate Ability #" + (i + 1), 1, 0, "", "&eClick to add an item ability!"));
            }
            setAction(i + 20, event -> {
                if (event.getClick() == ClickType.RIGHT) {
                    getOwner().setItemInHand(AbilityUtil.removeAbility(getOwner().getItemInHand(), finalI));
                    getOwner().playSound(getOwner().getLocation(), Sound.HORSE_ARMOR, 2, 1);
                } else {
                    new AbilityCreatorGUI(getOwner(), finalI).open();
                }
            });
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        return true;
    }
}
