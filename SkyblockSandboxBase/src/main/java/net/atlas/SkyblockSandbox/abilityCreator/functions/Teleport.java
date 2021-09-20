package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
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

public class Teleport extends Function {


    public Teleport(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        int range = Integer.parseInt(String.valueOf(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.TELEPORT_RANGE)));
        boolean message = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.SEND_MESSAGE));
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
    public List<Class<? extends Function>> conflicts() {
        return Arrays.asList(Implosion.class);
    }

    public enum dataValues implements Function.dataValues {
        TELEPORT_RANGE, SEND_MESSAGE;
    }

}
