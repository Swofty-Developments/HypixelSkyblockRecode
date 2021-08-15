package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.command.commands.Command_spawnmob;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProjectileChooserGUI extends SBGUI {
    private final int index2;
    private final int count;
    private final boolean update;
    private Material projectileMat = null;
    private final Gui gui;
    public ProjectileChooserGUI(SBPlayer owner, int index, int count, boolean update) {
        super(owner);
        this.index2 = index;
        this.count = count;
        this.update = update;
        this.gui = Gui.gui()
                .title(Component.text("Projectile Chooser GUI"))
                .disableAllInteractions()
                .rows(4)
                .create();
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = playerMenuUtility.getOwner();
        if(event.getClickedInventory().equals(inventory)) {
            if(event.getSlot() == 31) {
                new FunctionsEditorGUI(new PlayerMenuUtility(playerMenuUtility.getOwner()), "Projectile Shooter Function", index2, count, update).open();
            }
            if(event.getSlot() == 13) {
                if(event.getClick().isLeftClick()) {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Projectile Shooter Function"));
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.ID) && (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE))) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, index2, "Projectile Shooter Function"));
                        if(projectileMat == null) {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, count, ""));
                        } else {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, count, projectileMat));
                        }

                    } else {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, index2, "Projectile Shooter Function"));
                        if(projectileMat == null) {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, count, ""));
                        } else {
                            playerMenuUtility.getOwner().setItemInHand(AbilityData.setFunctionData(playerMenuUtility.getOwner().getItemInHand(), index2, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, count, projectileMat));

                        }
                    }
                    new FunctionsEditorGUI(new PlayerMenuUtility(playerMenuUtility.getOwner()),"Projectile Shooter Function", index2, count, update, projectileMat).open();
                } else if (event.getClick().isRightClick()){
                    projectileMat = null;
                    setItems();
                }
            }
        } else {
            if (!event.getCurrentItem().getType().equals(Material.AIR) && !event.getCurrentItem().getType().equals(Material.SKULL) && !event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                projectileMat = event.getCurrentItem().getType();
                setItems();
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public void setItems() {
        setFillerGlass();
        inventory.setItem(31, makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To Function editor #" + count));
        if(projectileMat == null) {
            inventory.setItem(13, makeColorfulItem(Material.IRON_SWORD, "&aCurrent Selected Projectile", 1, 0, "&7The current selected projectile.\n&7To select a different projectile,\n&7click on any item in your\n&7inventory and it will\n&7add it to the &bProjectile\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        } else {
            inventory.setItem(13, makeColorfulItem(projectileMat,"&aCurrent Selected Projectile", 1,0, "&7The current selected projectile.\n&7To select a different projectile,\n&7click on any item in your\n&7inventory and it will\n&7add it to the &bProjectile\n&bShooter Function&7!\n\n&eClick to confirm!\n&bRight-Click to reset!"));
        }
    }

    @Override
    public Gui getGUI() {
        return gui;
    }
}
