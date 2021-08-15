package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityDescriptionPicker extends NormalGUI {

    public AbilityDescriptionPicker(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Choose Ability";
    }

    @Override
    public int getRows() {
        return 4;
    }


    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        int b=0;
        for(int i=11;i<15;i++) {
            b++;
            int finalB = b;
            setAction(i, event -> {
                new SetAbilityDescriptionMenu(getOwner(), finalB).open();
            });
        }
        return true;
    }

    @Override
    public void setItems() {
        Player player = getOwner();
        ItemStack item = player.getItemInHand();
        SBItemStack sbItem = new SBItemStack(item);

        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create an item"));

        if (!(sbItem.hasAbility(1)))
            setItem(11, makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, SUtil.colorize("&7This ability slot is empty!", "&7this means you can put a", "&7new ability in here!", "", "&7Slot index: &a1", "", "&eClick to add ability!")));
        else
            setItem(11, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #1", 1, 0, sbItem.getAbilityDescription(item, 1)));

        if (!(sbItem.hasAbility(2)))
            setItem(12, makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, SUtil.colorize("&7This ability slot is empty!", "&7this means you can put a", "&7new ability in here!", "", "&7Slot index: &a2", "", "&eClick to add ability!")));
        else
            setItem(12, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #2", 1, 0, sbItem.getAbilityDescription(item, 2)));

        if (player.hasPermission("items.vip")) {
            if (!(sbItem.hasAbility(3)))
                setItem(13, makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, SUtil.colorize("&7This ability slot is empty!", "&7this means you can put a", "&7new ability in here!", "", "&7Slot index: &a3", "", "&eClick to add ability!")));
            else
                setItem(13, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #3", 1, 0, sbItem.getAbilityDescription(item, 3)));
        } else {
            setItem(13, makeColorfulItem(Material.BEDROCK, "§cLocked Slot", 1, 0, "§7This slot is locked for you!", "§7To unlock this ability slot", "§7you can buy a rank at our", "§7store!", "", "§cLocked!"));
        }

        if (player.hasPermission("items.mvp")) {
            if (!(sbItem.hasAbility(4)))
                setItem(14, makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, SUtil.colorize("&7This ability slot is empty!", "&7this means you can put a", "&7new ability in here!", "", "&7Slot index: &a4", "", "&eClick to add ability!")));
            else
                setItem(14, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #4", 1, 0, sbItem.getAbilityDescription(item, 4)));
        } else {
            setItem(14, makeColorfulItem(Material.BEDROCK, "§cLocked Slot", 1, 0, "§7This slot is locked for you!", "§7To unlock this ability slot", "§7you can buy a rank at our", "§7store!", "", "§cLocked!"));
        }

        if (player.hasPermission("items.mvp++")) {
            if (!(sbItem.hasAbility(5)))
                setItem(15, makeColorfulItem(Material.WOOD_BUTTON, "§cEmpty slot", 1, 0, SUtil.colorize("&7This ability slot is empty!", "&7this means you can put a", "&7new ability in here!", "", "&7Slot index: &a5", "", "&eClick to add ability!")));
            else
                setItem(15, makeColorfulItem(Material.BOOK_AND_QUILL, "§aAbility #5", 1, 0, sbItem.getAbilityDescription(item, 5)));
        } else {
            setItem(15, makeColorfulItem(Material.BEDROCK, "§cLocked Slot", 1, 0, "§7This slot is locked for you!", "§7To unlock this ability slot", "§7you can buy a rank at our", "§7store!", "", "§cLocked!"));
        }
    }
}
