package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Function;

public class Implosion extends AdvancedFunctions {
    @Override
    public void runnable() {
        SBPlayer player = getOwner();
        int range = Integer.parseInt(String.valueOf(getData(FunctionVariables.RANGE, getOwner().getItemInHand(), aindex, findex)));
        boolean message = (boolean) getData(FunctionVariables.MESSAGE, getOwner().getItemInHand(), aindex, findex);
        double d = 10000 * (1 + (player.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) / 100) * 0.3)/*todo + damage*/;
        int i = 0;
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
            if (e instanceof LivingEntity) {
                if (!(e instanceof Player || e instanceof ArmorStand || e instanceof NPC)) {
                    if (e.isDead()) {
                        ((LivingEntity) e).damage(0);
                    } else {
                        ++i;
                        ((LivingEntity) e).damage(d);
                    }
                }
            }
        }
        if (message) {
            if (i >= 1) {
                DecimalFormat format = new DecimalFormat("#,###");
                int damage = (int) (d * i);
                player.sendMessage("&7Your Implosion Function hit &c" + i + "&7 enemies dealing &c" + format.format(damage) + " damage&7.");
            }
        } else {
            if (i >= 1) {
                DecimalFormat format = new DecimalFormat("#,###");
                int damage = (int) (d * i);
                player.sendMessage("&7Your Implosion Function hit &c" + i + "&7 enemies dealing &c" + format.format(damage) + " damage&7.");
            }
        }
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
        return "Implosion";
    }

    @Override
    public String description() {
        return "&7Kills all nearby entities in\n&7a range!";
    }

    @Override
    public Material material() {
        return Material.BOOK_AND_QUILL;
    }

    @Override
    public AdvancedFunctions getGUI() {
        return this;
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

    public enum FunctionVariables{
        RANGE("range", Material.COMMAND, GUIType.INT, 1, 20, null),
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
