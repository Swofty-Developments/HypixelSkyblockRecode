package net.atlas.SkyblockSandbox.abilityCreator;

import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import static net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil.*;
import static net.atlas.SkyblockSandbox.abilityCreator.AbilityValue.*;
import static net.atlas.SkyblockSandbox.util.NBTUtil.getString;

public class Ability {
    public String name = "";
    public ItemStack stack;
    public int manaCost = 0;
    public int cooldown = 0;
    public int index;
    public ClickType clickType = ClickType.UNKNOWN;
    public ArrayList<String> description = new ArrayList<>();
    public HashMap<Integer, FunctionType> functions = new HashMap<>();
    public Ability(SBItemBuilder stack, int index) {
        this.index = index;
        this.stack = stack.stack;
        ItemStack item = stack.stack;
        name = getAbilityData(item, index, NAME);
        if(!getAbilityData(item, index, MANA_COST).equals("")) manaCost = Integer.parseInt(getAbilityData(item, index, MANA_COST));
        if(!getAbilityData(item, index, COOLDOWN).equals("")) cooldown = Integer.parseInt(getAbilityData(item, index, COOLDOWN));;
        if(!getAbilityData(item, index, CLICK_TYPE).equals("")) clickType = ClickType.valueOf(getAbilityData(item, index, CLICK_TYPE).replace("_CLICK", ""));

        description.addAll(getAbilityDescription(item, index));

        for (int i = 0; i < FunctionUtil.getFunctionAmount(item, index); i++) {
            functions.put(i, FunctionType.valueOf(FunctionUtil.getFunctionData(item, index, i, Function.FunctionValues.NAME).toUpperCase()));
        }
    }
    public Ability() {

    }

    public Ability name(String name) {
        this.name = name;
        return this;
    }
    public Ability cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }
    public Ability manaCost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }
    public Ability clickType(ClickType clickType) {
        this.clickType = clickType;
        return this;
    }
    public Ability addDescription(String line) {
        this.description.add(line);
        return this;
    }
    public Ability setDescription(int index, String line) {
        this.description.set(index, line);
        return this;
    }
    public Ability setWholeDescription(ArrayList<String> lines) {
        this.description = lines;
        return this;
    }
    @Deprecated() //Don't use this yet!
    public Ability addFunction(int fIndex, FunctionType function) {
        functions.put(fIndex, function);
        return this;
    }

    public SBItemBuilder build() {
        ItemStack item = stack;
        item = setAbilityData(item, index, NAME, name);
        item = setAbilityData(item, index, COOLDOWN, cooldown);
        item = setAbilityData(item, index, MANA_COST, manaCost);
        item = setAbilityData(item, index, CLICK_TYPE, clickType);
        if(!description.isEmpty()) {
            for (int i = 0; i < description.size(); i++) {
                item = setAbilityDescription(item, index, i, description.get(i));
            }
        }

        return new SBItemBuilder(item).ability(index, this);
    }
}
