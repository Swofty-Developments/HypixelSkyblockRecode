package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.pets.Pet;
import net.atlas.SkyblockSandbox.player.pets.PetBuilder;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PetBuilderGUI extends NormalGUI {
    public PetBuilderGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Pet Builder";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31,makeColorfulItem(Material.ARROW,"&cGo Back",1,0,"&7To create an item"));
        setItem(13, makeColorfulItem(Material.NAME_TAG, "§aSet pet level", 1, 0,
                SUtil.colorize("","&eClick to set the level!")));
        setItem(23,makeColorfulSkullItem("&aSet Pet Type!","http://textures.minecraft.net/texture/49d0e833d9bda32f2d736d8c3c3be8b9b964addd59357c12263ffccb8b8dae",1,"&7Click to set the pet type!"));

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(31,event -> {
            ItemStack stack = PetBuilder.init().name("test")
                    .rarity(Rarity.COMMON)
                    .petType(Pet.PetType.COMBAT.getPetModifier())
                    .perk(1,"Test1","Perk1","This is the 1st perk")
                    .perk(2,"Test2","Perk2","This is the 2nd perk")
                    .perk(3,"Test3","Perk3","This is the 3rd perk")
                    .texture("http://textures.minecraft.net/texture/49d0e833d9bda32f2d736d8c3c3be8b9b964addd59357c12263ffccb8b8dae")
                    .level(100)
                    .xp(4000000)
                    .build();
            getOwner().getInventory().addItem(stack);

        });
        return true;
    }

}
