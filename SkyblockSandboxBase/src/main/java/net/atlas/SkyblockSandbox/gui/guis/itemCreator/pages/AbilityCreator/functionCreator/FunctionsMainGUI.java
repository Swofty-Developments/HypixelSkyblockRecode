package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator;

import com.google.common.base.Enums;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.abilityCreator.*;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilityCreatorGUI;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FunctionsMainGUI extends NormalGUI {
    private final int index;

    public FunctionsMainGUI(SBPlayer owner, int index) {
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
    }

    @Override
    public boolean setClickActions() {
        SBPlayer player = getOwner();
        setAction(31,event -> {
            new AbilityCreatorGUI(getOwner(), index).open();
        });
        setAction(8,event -> {
            int count = FunctionUtil.getFunctionAmount(player.getItemInHand(), index);
            if (count >= 5) {
                player.sendMessage(SUtil.colorize("&cYou have already added 5 functions."));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                return;
            }
            count--;
            new FunctionSelectorGUI(getOwner(), index, count + 1).open();
        });
        for(int i=0;i<FunctionUtil.getFunctionAmount(player.getItemInHand(),index);i++) {
            int finalI = i;
            setAction(i+10, event -> {
                if (event.getClick().equals(ClickType.LEFT)) {
                    AbilityValue.FunctionType type = Enums.getIfPresent(AbilityValue.FunctionType.class,FunctionUtil.getFunctionData(player.getItemInHand(),index, finalI,Function.FunctionValues.NAME)).orNull();
                    new FunctionsEditorGUI(getOwner(), type,index, finalI).open();
                }
                if (event.getClick().equals(ClickType.RIGHT)) {
                    ItemStack newItem = AbilityData.removeFunction(player.getItemInHand(), index, finalI, player);
                    player.setItemInHand(newItem);
                    player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 2, 1);
                    updateItems();
                }
            });

        }
        return true;
    }

    @Override
    public void setItems() {
        SBPlayer player = getOwner();
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        setItem(8, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAdd Function", 1, 0, "§7Add a function to your item!"));
        List<String> functions = new ArrayList<>();
        for (int i = 0; i < FunctionUtil.getFunctionAmount(player.getItemInHand(), index); i++) {
            String name = FunctionUtil.getFunctionData(player.getItemInHand(), index,i, Function.FunctionValues.NAME);
            ArrayList<String> lore = new ArrayList<>();
            if(!name.equals("")) {
                lore.add("&7Function name: &b" + SUtil.firstLetterUpper(name) + " Function");
            }

            for (String value : AbilityData.listFunctionData(player.getItemInHand(), index, i)) {
                if (!value.equals("id") && !value.equalsIgnoreCase("name")) {
                    lore.add("&7" + value.replace("_", " ") + ": &a" + FunctionUtil.getFunctionString(player.getItemInHand(),index,i,value));
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
            } else {
                functions.add(name);
            }

        }
    }
}
