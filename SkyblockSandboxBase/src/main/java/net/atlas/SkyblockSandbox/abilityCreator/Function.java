package net.atlas.SkyblockSandbox.abilityCreator;

import dev.triumphteam.gui.components.GuiAction;
import lombok.Getter;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilityCreatorGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionsEditorGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

@Getter
public abstract class Function {

    private final SBPlayer player;
    private ItemStack stack;
    private final int abilIndex;
    private final int functionIndex;

    HashMap<Integer, ItemStack> guiItems = new HashMap<>();
    HashMap<Integer, GuiAction<InventoryClickEvent>> clickActions = new HashMap<>();

    public Function(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        this.player = player;
        this.stack = stack;
        this.abilIndex = abilIndex;
        this.functionIndex = functionIndex;
    }

    public abstract void applyFunction();

    public abstract AbilityValue.FunctionType getFunctionType();

    public abstract List<Class<? extends Function>> conflicts();

    public interface dataValues {
    }

    public HashMap<Integer, ItemStack> getGuiItems() {
        return guiItems;
    }

    public HashMap<Integer, GuiAction<InventoryClickEvent>> getClickActions() {
        return clickActions;
    }

    public enum FunctionValues implements dataValues {
        SEND_MESSAGE, NAME;
    }

    public void setAction(int slot, GuiAction<InventoryClickEvent> event) {
        clickActions.put(slot,event);
    }

    public void setItem(int slot,ItemStack item) {
        guiItems.put(slot,item);
    }

    public abstract void getGuiLayout();

    public AnvilGUI anvilGUI(Enum<? extends dataValues> value) {
        AnvilGUI gui = new AnvilGUI(player.getPlayer(),event -> {
           event.setWillClose(true);
           String output = String.valueOf(event.getName());
           ItemStack stack = FunctionUtil.setFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),value,output);
           player.setItemInHand(stack);
            new BukkitRunnable() {
                @Override
                public void run() {
                    new FunctionsEditorGUI(player,getFunctionType(),abilIndex,functionIndex).open();
                }
            }.runTaskLater(SBX.getInstance(), 2);
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT,makeColorfulItem(Material.PAPER,"&aSet the " + value.name(),1,0,"&aInput: &e^^^^^"));
        gui.open();
        return gui;
    }

    public AnvilGUI anvilGUI(Enum<? extends dataValues> value,int min,int max) {
        AnvilGUI gui = new AnvilGUI(player.getPlayer(),event -> {
            event.setWillClose(true);
            String output = String.valueOf(event.getName());
            if(NumUtils.isInt(output)) {
                int s = Integer.parseInt(output);
                if (s < min || s > max) {
                    invalidInputError("&cThat number is out of the provided range!");
                    return;
                }
            } else {
                invalidInputError("&cInvalid number!");
                return;
            }
            ItemStack stack = FunctionUtil.setFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),value,output);
            player.setItemInHand(stack);
            new BukkitRunnable() {
                @Override
                public void run() {
                    new FunctionsEditorGUI(player,getFunctionType(),abilIndex,functionIndex).open();
                }
            }.runTaskLater(SBX.getInstance(), 2);
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT,makeColorfulItem(Material.PAPER,"&aSet the " + value.name(),1,0,"&aInput: &e^^^^^"));
        gui.open();
        return gui;
    }

    public void invalidInputError(String msg) {
        player.sendMessage(SUtil.colorize(msg));
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT,1,0);
        player.closeInventory();
        new BukkitRunnable() {
            @Override
            public void run() {
                new FunctionsEditorGUI(player,getFunctionType(),abilIndex,functionIndex).open();
            }
        }.runTaskLater(SBX.getInstance(), 2);
    }

}
