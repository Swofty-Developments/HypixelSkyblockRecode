package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionsEditorGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.StackUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class HeadChooserGUI extends NormalGUI {
    private final int index2;
    private final int funcIndex;
    private String headTexture = null;
    public HeadChooserGUI(SBPlayer owner, int index, int funcIndex) {
        super(owner);
        this.index2 = index;
        this.funcIndex = funcIndex;
    }

    @Override
    public String getTitle() {
        return "Head Chooser GUI";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        SBPlayer player = getOwner();
        if(event.getClickedInventory().equals(getGui().getInventory())) {
            if(event.getSlot() == 31) {
                new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.HEAD, index2, funcIndex).open();
            }
            if(event.getSlot() == 13) {
                if(event.getClick().isLeftClick()) {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, funcIndex, "Head Shooter Function"));
                    if (!AbilityData.hasFunctionData(player.getItemInHand(), index2, funcIndex, EnumFunctionsData.ID) || (!AbilityData.hasFunctionData(player.getItemInHand(), index2, funcIndex, EnumFunctionsData.HEAD_SHOOTER_TYPE))) {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, funcIndex, player));
                    }
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, index2, "Head Shooter Function"));
                    if(headTexture == null) {
                        getOwner().setItemInHand(AbilityData.setFunctionData(getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, funcIndex, ""));
                    } else {
                        getOwner().setItemInHand(AbilityData.setFunctionData(getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, funcIndex, headTexture));
                    }
                    new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.HEAD, index2, funcIndex).open();
                } else if (event.getClick().isRightClick()){
                    headTexture = null;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateItems();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                }
            }
        } else {
            if(event.getCurrentItem().getType().equals(Material.SKULL_ITEM) || event.getCurrentItem().getType().equals(Material.SKULL)) {
                ItemStack skull = event.getCurrentItem();
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                headTexture = StackUtils.textureFromSkullMeta(skullMeta, skull);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateItems();
                    }
                }.runTaskLater(SBX.getInstance(), 2);
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To Function editor #" + funcIndex));
        if(headTexture == null) {
            setItem(13, makeColorfulItem(Material.SKULL_ITEM, "&aCurrent Selected Head", 1, 3, "&7The current selected head.\n&7To select a different head,\n&7click on any skull in you\n&7inventory and it will\n&7add it to the &bHead\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        } else {
            setItem(13, makeColorfulSkullItem(headTexture,"&aCurrent Selected Head", 1, "&7The current selected head.\n&7To select a different head,\n&7click on any skull in you\n&7inventory and it will\n&7add it to the &bHead\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        }
    }
}
