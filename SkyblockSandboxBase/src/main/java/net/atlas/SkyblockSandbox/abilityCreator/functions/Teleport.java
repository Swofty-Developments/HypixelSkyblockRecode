package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class Teleport extends AdvancedFunctions {
    public Teleport(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void runnable() {
        int range = Integer.parseInt(String.valueOf(getData(FunctionVariables.RANGE, getOwner().getItemInHand(), aindex, findex)));
        boolean message = (boolean) getData(FunctionVariables.MESSAGE, getOwner().getItemInHand(), aindex, findex);
        ArrayList<Block> blocks = (ArrayList<Block>) getOwner().getLineOfSight((Set<Material>) null, range);
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.getType() != Material.AIR || block.isLiquid()) {
                if (i == 0 || i == 1) {
                    if (message) {
                        getOwner().sendMessage("&cThere is a block in the way!");
                    }
                    range = 0;
                    return;
                }
                if (i + 1 < blocks.size()) {
                    if (message) {
                        getOwner().sendMessage("&cThere is a block in the way!");
                    }
                }
                range = i - 1;
                break;
            }
        }
        Block b = blocks.get(range);
        Location loc = new Location(b.getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5, getOwner().getLocation().getYaw(),
                getOwner().getLocation().getPitch());
        getOwner().teleport(loc);
    }

    public Object getData(FunctionVariables variable, ItemStack item, int aindex, int findex) {
        if (variable.type.equals(GUIType.BOOLEAN)) {
            Object o = AbilityData.retrieveFunctionData(name() + "_" + variable.guiName, item, aindex, findex);
            if ("True".equals(o)) {
                return true;
            } else if ("False".equals(o)) {
                return false;
            }
        }
        return(AbilityData.retrieveFunctionData(name() + "_" + variable.guiName, item, aindex, findex));
    }

    @Override
    public ArrayList<ItemStack> itemList() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Particle.FunctionVariables variables : Particle.FunctionVariables.values()) {
            if(variables.depend == null) {
                items.add(createItem(variables.guiName, variables.type, variables.material, variables.min, variables.max, variables.Enum));
            } else {
                Object depend = AbilityData.retrieveFunctionData(name() + "_" + variables.depend.guiName, getOwner().getItemInHand(), aindex, findex);
                if (!depend.equals("")) {
                    items.add(createItem(variables.guiName, variables.type, variables.material, variables.min, variables.max, variables.Enum));
                }
            }
        }
        return items;
    }

    @Override
    public String name() {
        return "Teleport";
    }

    @Override
    public String description() {
        return "&7Teleports you a certain\n&7amount of blocks.";
    }

    @Override
    public Material material() {
        return Material.ENDER_PEARL;
    }

    @Override
    public AdvancedFunctions getGUI() {
        return this;
    }

    public enum FunctionVariables{
        RANGE("range", Material.COMMAND, GUIType.INT, 1, 100, null),
        MESSAGE("message", Material.BOOK_AND_QUILL, GUIType.BOOLEAN, null);

        public final String guiName;
        public final GUIType type;
        public final Material material;
        public final String min;
        public final String max;
        public final Class Enum;
        public final FunctionVariables depend;

        FunctionVariables(String guiName, Material material,  GUIType type, FunctionVariables depend) {
            this.guiName = guiName;
            this.type = type;
            this.material = material;
            this.min = null;
            this.max = null;
            Enum = null;
            this.depend = depend;
        }
        FunctionVariables(String guiName, Material material, GUIType type, Class Enum, FunctionVariables depend) {
            this.guiName = guiName;
            this.type = type;
            this.material = material;
            this.min = null;
            this.max = null;
            this.Enum = Enum;
            this.depend = depend;
        }
        FunctionVariables(String guiName, Material material,  GUIType type, float min, float max, FunctionVariables depend) {
            this.guiName = guiName;
            this.type = type;
            this.material = material;
            this.min = String.valueOf(min);
            this.max = String.valueOf(max);
            Enum = null;
            this.depend = depend;
        }
    }
}
