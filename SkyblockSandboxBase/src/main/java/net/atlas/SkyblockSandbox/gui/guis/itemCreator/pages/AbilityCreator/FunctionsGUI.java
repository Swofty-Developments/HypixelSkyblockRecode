package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Particles;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class FunctionsGUI extends SBGUI {
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
                if(!AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), index).equals("")){
                    player.setItemInHand(AbilityData.setData(EnumAbilityData.BASE_ABILITY, player.getItemInHand(), "", index));
                }
                List<String> functions = new ArrayList<>();
                int count = 1;
                if(inventory.getItem(11).equals(FILLER_GLASS)){
                    new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, 1, true).open();
                    functions.add((String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, count));
                    return;
                } else {
                    count++;
                }
                if(inventory.getItem(12).equals(FILLER_GLASS)){
                    new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, 2, true).open();
                    functions.add((String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, count));
                    return;
                } else {
                    count++;
                }
                if(inventory.getItem(13).equals(FILLER_GLASS)){
                    new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, 3, true).open();
                    functions.add((String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, count));
                    return;
                } else {
                    count++;
                }
                if(inventory.getItem(14).equals(FILLER_GLASS)){
                    new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, 4, true).open();
                    functions.add((String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, count));
                    return;
                } else {
                    count++;
                }
                if(inventory.getItem(15).equals(FILLER_GLASS)){
                    new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, 5, true).open();
                    functions.add((String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, count));
                    return;
                } else {
                    count++;
                }
                if(count >= 3){
                    player.sendMessage(IUtil.colorize("&cYou have already added 3 functions."));
                    return;
                }
                break;
            }
        }
        for (int i = 1; i <= 5; i++) {
            if(event.getSlot() == 10 + i) {
                if(!event.getCurrentItem().getType().equals(Material.COMMAND)){
                    return;
                }
                if(event.getClick().equals(ClickType.LEFT)){
                    if(event.getCurrentItem().getItemMeta().getLore().get(0).replace(IUtil.colorize("&7Function name: &b"), "").equals("Particle Function")) {
                        new FunctionsEditorGUI(new PlayerMenuUtility(player), event.getCurrentItem().getItemMeta().getLore().get(0).replace(SUtil.colorize("&7Function name: &b"), ""), index, i, false, Particles.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).replace(SUtil.colorize("&7Particle Type: &a"), ""))).open();
                    } else if (event.getCurrentItem().getItemMeta().getLore().get(0).replace(IUtil.colorize("&7Function name: &b"), "").equals("Sound Function")){
                        new FunctionsEditorGUI(new PlayerMenuUtility(player), event.getCurrentItem().getItemMeta().getLore().get(0).replace(SUtil.colorize("&7Function name: &b"), ""), index, i, false, Sound.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).replace(SUtil.colorize("&7Sound Type: &a"), ""))).open();
                    } else {
                    new FunctionsEditorGUI(new PlayerMenuUtility(player), event.getCurrentItem().getItemMeta().getLore().get(0).replace(SUtil.colorize("&7Function name: &b"), ""), index, i, false).open();
                    }
                }
                if(event.getClick().equals(ClickType.RIGHT)){
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, i, player));
                    setItems();
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
        Player player = playerMenuUtility.getOwner();
        setFillerGlass();
        inventory.setItem(31, makeItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        inventory.setItem(8, makeItem(Material.BOOK_AND_QUILL, "§aAdd Function", 1, 0, "§7Add a function to your item!"));
        List<String> functions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            if(AbilityData.hasFunctionData(player.getItemInHand(),index, i, EnumFunctionsData.ID)) {
                String name = "" + AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), index, i);
                ArrayList<String> lore = new ArrayList<>();
                lore.add("&7Function name: &b" + name);
                for (String value : AbilityData.AFromB(2)) {
                    if (!AbilityData.retrieveFunctionData(AbilityData.AFromString(value), player.getItemInHand(), index, i).equals("") && AbilityData.AFromString(value).getC()) {

                        lore.add("&7" + value.replace("_", " ") + ": &a" + AbilityData.retrieveFunctionData(AbilityData.AFromString(value), player.getItemInHand(), index, i));
                    }
                }
                lore.add("");
                lore.add("");
                lore.add("&eClick to edit!");
                lore.add("&bRight-click to remove");
                inventory.setItem(10 + i, makeColorfulItem(Material.COMMAND, "&aFunction #" + i, 1, 0, lore));
                if(functions.contains(name)) {
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, i, player));
                    player.sendMessage(ChatColor.RED + "You cannot have more than one of the same function!");
                    player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,2,0);
                    setItems();
                } else {
                    functions.add(name);
                }
            } else {
                inventory.setItem(10 + i, FILLER_GLASS);
            }
        }
    }
}
