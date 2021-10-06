package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

public class Teleport extends Function {


    public Teleport(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        int range = Integer.parseInt(String.valueOf(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.TELEPORT_RANGE)));
        boolean message = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),FunctionValues.SEND_MESSAGE));
        ArrayList<Block> blocks = (ArrayList<Block>) getPlayer().getLineOfSight((Set<Material>) null, range);
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.getType() != Material.AIR || block.isLiquid()) {
                if (i == 0 || i == 1) {
                    if (message) {
                        getPlayer().sendMessage("&cThere are blocks in the way!");
                    }
                    range = 0;
                    return;
                }
                if (i + 1 < blocks.size()) {
                    if (message) {
                        getPlayer().sendMessage("&cThere are blocks in the way!");
                    }
                }
                range = i - 1;
                break;
            }
        }
        Block b = blocks.get(range);
        Location loc = new Location(b.getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5, getPlayer().getLocation().getYaw(),
                getPlayer().getLocation().getPitch());
        getPlayer().teleport(loc);
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.TELEPORT;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return Arrays.asList(Implosion.class);
    }

    public enum dataValues implements Function.dataValues {
        TELEPORT_RANGE;
    }

    @Override
    public void getGuiLayout() {
        setItem(13, makeColorfulItem(Material.ENDER_PEARL, "&aTeleport Range", 1, 0, "&7Edit the range of the\n&bTeleport Function&7!\n&7Maximum: &a100\n&7Minimum: &a0\n\n&eClick to set!"));
        setItem(14, makeColorfulItem(Material.FEATHER, "&aToggle message", 1, 0, "&7Turn on and off the\n&bTeleport Function &7message!\n\n&bRight-click to disable!\n&eLeft-click to enable!"));
        setAction(13,event -> {
            anvilGUI(dataValues.TELEPORT_RANGE,0,100);
        });
        setAction(14,event -> {
            ItemStack stack = getStack();
            switch (event.getClick()) {
                case LEFT:
                    stack = FunctionUtil.setFunctionData(stack,getAbilIndex(),getFunctionIndex(),FunctionValues.SEND_MESSAGE,"true");
                    break;
                case RIGHT:
                    stack = FunctionUtil.setFunctionData(stack,getAbilIndex(),getFunctionIndex(),FunctionValues.SEND_MESSAGE,"false");
                    break;
            }
            if(getStack()!=stack) {
                getPlayer().setItemInHand(stack);
            }
        });
    }

}
