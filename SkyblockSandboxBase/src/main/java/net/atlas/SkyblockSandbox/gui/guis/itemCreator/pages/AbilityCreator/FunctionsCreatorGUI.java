package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class FunctionsCreatorGUI extends NormalGUI {
    private final int index;
    private final int count;
    private final boolean update;

    public FunctionsCreatorGUI(SBPlayer owner, int index, int count, boolean update) {
        super(owner);
        this.index = index;
        this.count = count;
        this.update = update;
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
        SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
        List<String> functions = new ArrayList<>();
        for (int b = 0; b < AbilityData.getAbilityAmount(player.getItemInHand()); b++) {
            for (int i = 0; i < 5; i++) {
                String name = (String) AbilityData.retrieveFunctionData(EnumFunctionsData.NAME, player.getItemInHand(), b, (i + 1));
                functions.add(name);
            }
        }
        if (event.getSlot() == 31) {
            new FunctionsGUI(player, index).open();
        }
        Reflections reflection = new Reflections("net.atlas.SkyblockSandbox.abilityCreator.functions");
        for(Class<? extends AdvancedFunctions> l:reflection.getSubTypesOf(AdvancedFunctions.class)) {
            try {
                AdvancedFunctions function = l.newInstance();
                if(function.name().equals(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replace(" Function", ""))) {
                    function.setOwner(player);
                    function.setAindex(index);
                    function.setFindex(count);
                    function.setBackGUI(getGui());
                    function.open();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        Reflections reflection = new Reflections("net.atlas.SkyblockSandbox.abilityCreator.functions");
        int i = 0;
        for(Class<? extends AdvancedFunctions> l:reflection.getSubTypesOf(AdvancedFunctions.class)) {
            try {
                AdvancedFunctions function = l.newInstance();

                setItem(10 + i, makeColorfulItem(function.material(), "&a" + function.name() + " Function", 1, 0, "&6Functionality:\n" + function.description() + "\n\n&eClick to set!"));
                i++;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        /*
        setItem(10, makeColorfulItem(Material.ENDER_PEARL, "&aTeleport Function", 1, 0, "&7Set the range and toggle\n&7the message.\n\n&6Functionality:\n&7Teleport forwards a set\n&7range!\n\n&7Maximum: &a100\n&7Minimum: &a0\n\n&eClick to set!"));
        setItem(11, makeColorfulItem(Material.BOOK_AND_QUILL, "&aImplosion Function", 1, 0, "&7Set the range and toggle\n&7the message.\n\n&6Functionality:\n&7Kills all nearby entities in\n&7a range!\n\n&7Maximum: &a15\n&7Minimum: &a0\n\n&eClick to set!"));
        setItem(12, makeColorfulSkullItem("&aParticle Generator", "http://textures.minecraft.net/texture/9b1e20410bb6c7e6968afcd3ec855520c37a40d54a54e8dafc2e6b6f2f9a1915", 1, "&7Set the particle, amount,\n&7distance from player and\n&7shape.\n\n&6Functionality:\n&7Summon a particle that\n&7can kill mobs in its\n&7path, aswell as create\n&7a circle around you!\n\n&eClick to set!"));
        setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aSound Function", 1, 0, "&7Set the sound played, pitch,\n&7amount, delay and volume.\n\n&6Functionality:\n&7Play any sound in minecraft!\n\n&eClick to set!"));
        setItem(14, makeColorfulSkullItem("&aHead Shooter", "http://textures.minecraft.net/texture/41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583", 1, "&7Set the head, range, toggle\n&7the damage and set the\n&7base damage.\n\n&6Functionality:\n&7Choose any skull in your\n&7inventory and shoot it!\n\n&eClick to set!"));
        setItem(15, makeColorfulSkullItem("&aEntity Shooter", "http://textures.minecraft.net/texture/9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc", 1, "&7Set the entity.\n\n&c&lTHIS HASN'T BEEN IMPLEMENTED YET!\n\n&eClick to set!"));
        setItem(16, makeColorfulItem(Material.DIAMOND_AXE, "&aProjectile Shooter", 1, 0, "&7Set the projectile and toggle\n&7the message.\n\n&6Functionality:\n&7Launch a projectile\n&7forward!\n\n&eClick to set!"));
    */
    }

}
