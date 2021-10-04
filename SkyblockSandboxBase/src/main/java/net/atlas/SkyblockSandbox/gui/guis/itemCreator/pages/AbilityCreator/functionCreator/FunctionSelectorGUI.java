package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.abilityCreator.*;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class FunctionSelectorGUI extends NormalGUI {
    private final int index;
    private final int funcIndex;

    public FunctionSelectorGUI(SBPlayer owner, int index, int funcIndex) {
        super(owner);
        this.index = index;
        this.funcIndex = funcIndex;
    }

    @Override
    public String getTitle() {
        return "Create a Function";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
        setAction(10,event1 -> {
            ItemStack i = FunctionUtil.setFunctionData(player.getItemInHand(),index,funcIndex,Function.FunctionValues.NAME, AbilityValue.FunctionType.TELEPORT.name());
            player.setItemInHand(i);
            new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.TELEPORT,index,funcIndex).open();
        });
        setAction(11,event1 -> {
            ItemStack i = FunctionUtil.setFunctionData(player.getItemInHand(),index,funcIndex,Function.FunctionValues.NAME, AbilityValue.FunctionType.IMPLOSION.name());
            player.setItemInHand(i);
            new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.IMPLOSION,index,funcIndex).open();
        });
        setAction(12,event1 -> {
            ItemStack i = FunctionUtil.setFunctionData(player.getItemInHand(),index,funcIndex,Function.FunctionValues.NAME, AbilityValue.FunctionType.PARTICLE.name());
            player.setItemInHand(i);
            new FunctionsEditorGUI(getOwner(),AbilityValue.FunctionType.PARTICLE,index,funcIndex).open();
        });
        setAction(13,event1 -> {
            ItemStack i = FunctionUtil.setFunctionData(player.getItemInHand(),index,funcIndex,Function.FunctionValues.NAME, AbilityValue.FunctionType.SOUND.name());
            player.setItemInHand(i);
            new FunctionsEditorGUI(getOwner(),AbilityValue.FunctionType.SOUND,index,funcIndex).open();
        });
        setAction(17,event1 -> {
            ItemStack i = FunctionUtil.setFunctionData(player.getItemInHand(),index,funcIndex,Function.FunctionValues.NAME, AbilityValue.FunctionType.SHORTBOW.name());
            player.setItemInHand(i);
            new FunctionsEditorGUI(getOwner(),AbilityValue.FunctionType.SHORTBOW,index,funcIndex).open();
        });
        if (event.getSlot() == 31) {
            new FunctionsMainGUI(player, index).open();
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).asGuiItem());
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));

        setItem(10, makeColorfulItem(Material.ENDER_PEARL, "&aTeleport Function", 1, 0, "&7Set the range and toggle\n&7the message.\n\n&6Functionality:\n&7Teleport forwards a set\n&7range!\n\n&7Maximum: &a100\n&7Minimum: &a0\n\n&eClick to set!"));
        setItem(11, makeColorfulItem(Material.BOOK_AND_QUILL, "&aImplosion Function", 1, 0, "&7Set the range and toggle\n&7the message.\n\n&6Functionality:\n&7Kills all nearby entities in\n&7a range!\n\n&7Maximum: &a15\n&7Minimum: &a0\n\n&eClick to set!"));
        setItem(12, makeColorfulSkullItem("&aParticle Generator", "http://textures.minecraft.net/texture/9b1e20410bb6c7e6968afcd3ec855520c37a40d54a54e8dafc2e6b6f2f9a1915", 1, "&7Set the particle, amount,\n&7distance from player and\n&7shape.\n\n&6Functionality:\n&7Summon a particle that\n&7can kill mobs in its\n&7path, aswell as create\n&7a circle around you!\n\n&eClick to set!"));
        setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aSound Function", 1, 0, "&7Set the sound played, pitch,\n&7amount, delay and volume.\n\n&6Functionality:\n&7Play any sound in minecraft!\n\n&eClick to set!"));
        setItem(14, makeColorfulSkullItem("&aHead Shooter", "http://textures.minecraft.net/texture/41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583", 1, "&7Set the head, range, toggle\n&7the damage and set the\n&7base damage.\n\n&6Functionality:\n&7Choose any skull in your\n&7inventory and shoot it!\n\n&eClick to set!"));
        setItem(15, makeColorfulSkullItem("&aEntity Shooter", "http://textures.minecraft.net/texture/9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc", 1, "&7Set the entity.\n\n&c&lTHIS HASN'T BEEN IMPLEMENTED YET!\n\n&eClick to set!"));
        setItem(16, makeColorfulItem(Material.DIAMOND_AXE, "&aProjectile Shooter", 1, 0, "&7Set the projectile and toggle\n&7the message.\n\n&6Functionality:\n&7Launch a projectile\n&7forward!\n\n&eClick to set!"));
        setItem(17,makeColorfulItem(Material.BOW,"&aShortbow Function",1,2,"&7Set the arrow amount.","","&6Functionality: &7Shoot arrows forward instantly!","","&eClick to set!"));


        /*int ii = 0;
        for (ItemStack i : getGui().getInventory().getContents()) {
            if (i == null || i.getType().equals(Material.AIR)) {
                setItem(ii, super.FILLER_GLASS);
            }
            ii++;
        }*/
    }

}
