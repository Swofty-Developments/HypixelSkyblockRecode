package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
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
                String abilName = AbilityUtil.getAbilityData(stack, i, AbilityValue.NAME);
                String clickType = AbilityUtil.getAbilityData(stack, i, AbilityValue.CLICK_TYPE);
                String manaCost = AbilityUtil.getAbilityData(stack, i, AbilityValue.MANA_COST);
                String cooldown = AbilityUtil.getAbilityData(stack, i, AbilityValue.COOLDOWN);
                if (cooldown.equals("")) {
                    cooldown = AbilityUtil.getAbilityString(stack, i, "cooldown");
                    AbilityUtil.setAbilityData(stack,i,AbilityValue.COOLDOWN,cooldown);
                    if (cooldown.equals("")) {
                        cooldown = "&cNOT SET";
                    }
                }
                if (abilName.equals("")) {
                    abilName = AbilityUtil.getAbilityString(stack, i, "name");
                    AbilityUtil.setAbilityData(stack,i,AbilityValue.NAME,abilName);
                    if (abilName.equals("")) {
                        abilName = "&cNOT SET";
                    }
                }
                if (clickType.equals("")) {
                    clickType = AbilityUtil.getAbilityString(stack, i, "function");
                    AbilityUtil.setAbilityData(stack,i,AbilityValue.CLICK_TYPE,clickType.replace(" ","_").replace("_CLICK",""));
                    if (clickType.equals("")) {
                        clickType = "&cNOT SET";
                    }
                }
                if (manaCost.equals("")) {
                    manaCost = AbilityUtil.getAbilityString(stack, i, "mana_cost");
                    AbilityUtil.setAbilityData(stack,i,AbilityValue.MANA_COST,manaCost);
                    if (manaCost.equals("")) {
                        manaCost = "&cNOT SET";
                    }
                }
                setItem(i + 20, makeColorfulItem(Material.COMMAND, "&aAbility #" + (i + 1),
                        1, 0, "&7Ability Name: &b" + abilName,
                        "&7Click Type: &a" + clickType,
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
