package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadChooserGUI extends SBGUI {
    private final int index2;
    private final int count;
    private final boolean update;
    private String headTexture = null;
    public HeadChooserGUI(SBPlayer owner, int index, int count, boolean update) {
        super(owner);
        this.index2 = index;
        this.count = count;
        this.update = update;
    }

    @Override
    public String getTitle() {
        return "Head Chooser GUI";
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getSize() {
        return 4*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = playerMenuUtility.getOwner();
        if(event.getClickedInventory().equals(inventory)) {
            if(event.getSlot() == 31) {
                new FunctionsEditorGUI(new PlayerMenuUtility(playerMenuUtility.getOwner()), "Head Shooter Function", index2, count, update).open();
            }
            if(event.getSlot() == 13) {
                if(event.getClick().isLeftClick()) {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Head Shooter Function"));
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.ID) && (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.HEAD_SHOOTER_TYPE))) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, index2, "Head Shooter Function"));
                        if(headTexture == null) {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, count, ""));
                        } else {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, count, headTexture));
                        }

                    } else {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, index2, "Head Shooter Function"));
                        if(headTexture == null) {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, count, ""));
                        } else {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.HEAD_SHOOTER_TYPE, count, headTexture));

                        }
                    }
                    new FunctionsEditorGUI(new PlayerMenuUtility(playerMenuUtility.getOwner()),"Head Shooter Function", index2, count, update, headTexture).open();
                } else if (event.getClick().isRightClick()){
                    headTexture = null;
                    setItems();
                }
            }
        } else {
            if(event.getCurrentItem().getType().equals(Material.SKULL_ITEM) || event.getCurrentItem().getType().equals(Material.SKULL)) {
                ItemStack skull = event.getCurrentItem();
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                headTexture = IUtil.textureFromSkullMeta(skullMeta, skull);
                setItems();
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
        inventory.setItem(31, makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To Function editor #" + count));
        if(headTexture == null) {
            inventory.setItem(13, makeColorfulItem(Material.SKULL_ITEM, "&aCurrent Selected Head", 1, 3, "&7The current selected head.\n&7To select a different head,\n&7click on any skull in you\n&7inventory and it will\n&7add it to the &bHead\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        } else {
            inventory.setItem(13, makeTexturedSkullItem(headTexture,"&aCurrent Selected Head", 1, "&7The current selected head.\n&7To select a different head,\n&7click on any skull in you\n&7inventory and it will\n&7add it to the &bHead\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        }
    }
}
