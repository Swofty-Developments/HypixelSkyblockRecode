package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.abilityCreator.*;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class FunctionsGUI extends NormalGUI {
    private final int index;

    public FunctionsGUI(SBPlayer owner, int index) {
        super(owner);
        this.index = index;
    }

    @Override
    public String getTitle() {
        return "Item Functions";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getCurrentItem().getType()) {
            case ARROW: {
                new AbilityCreatorGUI(getOwner(), index).open();
                break;
            }
            case BOOK_AND_QUILL: {
                int count = FunctionUtil.getFunctionAmount(player.getItemInHand(),index);
                if (count >= 3) {
                    player.sendMessage(SUtil.colorize("&cYou have already added 3 functions."));
                    player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,1,0);
                    return;
                }
                count--;
                new FunctionSelectorGUI(getOwner(),index,count+1).open();
                break;
            }
        }
        if (event.getCurrentItem().getType().equals(Material.COMMAND)) {
            int i = event.getSlot() - 10;
            if (event.getClick().equals(ClickType.LEFT)) {
                AbilityValue.FunctionType type = Enums.getIfPresent(AbilityValue.FunctionType.class,FunctionUtil.getFunctionData(player.getItemInHand(),index,i,Function.FunctionValues.NAME)).orNull();
                new FunctionsEditorGUI(getOwner(), type,index,i).open();
            }
            if (event.getClick().equals(ClickType.RIGHT)) {
                player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, i, player));
                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 2, 1);
                updateItems();
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        SBPlayer player = getOwner();
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        setItem(8, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAdd Function", 1, 0, "§7Add a function to your item!"));
        List<String> functions = new ArrayList<>();
        for (int i = 0; i < FunctionUtil.getFunctionAmount(player.getItemInHand(), index)-1; i++) {
            String name = "" + AbilityUtil.getAbilityData(player.getItemInHand(), i, AbilityValue.NAME);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("&7Function name: &b" + SUtil.firstLetterUpper(name) + " Function");

            for (String value : AbilityData.listFunctionData(player.getItemInHand(), index, i)) {
                if (!value.equals("id") && !value.equals("name")) {
                    lore.add("&7" + value.replace("_", " ") + ": &a" + AbilityData.retrieveFunctionData(value, player.getItemInHand(), index, i));
                }
            }
            lore.add("");
            lore.add("");
            lore.add("&eClick to edit!");
            lore.add("&bRight-click to remove");
            setItem(10 + i, makeColorfulItem(Material.COMMAND, "&aFunction #" + (i+1), 1, 0, lore));
            if (functions.contains(name)) {
                player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, i, player));
                player.sendMessage(ChatColor.RED + "You cannot have more than one of the same function!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 0);
                setItems();
            } else {
                functions.add(name);
            }

        }
    }
}
