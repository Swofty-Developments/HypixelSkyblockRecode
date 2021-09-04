package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Sound extends AdvancedFunctions {
    @Override
    public void runnable() {

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
        return "Sound";
    }

    @Override
    public String description() {
        return "&7Plays any sound you choose.";
    }

    @Override
    public Material material() {
        return Material.NOTE_BLOCK;
    }

    @Override
    public AdvancedFunctions getGUI() {
        return this;
    }

    public enum FunctionVariables{
        SOUND("Type", Material.NOTE_BLOCK, GUIType.ENUM, org.bukkit.Sound.class, null),
        VOLUME("Volume", Material.IRON_BLOCK, GUIType.INT, 1, 100, SOUND),
        PITCH("Pitch", Material.STICK, GUIType.FLOAT, 0.5F, 2.0F, SOUND),
        DELAY("Delay", Material.WATCH, GUIType.FLOAT, 0, 5.0F, SOUND),
        AMOUNT("Amount", Material.BOOK_AND_QUILL, GUIType.INT, 1, 30, SOUND);

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
