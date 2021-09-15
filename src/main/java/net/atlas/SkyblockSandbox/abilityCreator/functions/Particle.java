package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Particle extends AdvancedFunctions {
    public Particle(SBPlayer owner) {
        super(owner);
    }

    @Override
    public void runnable() {

    }

    @Override
    public ArrayList<ItemStack> itemList() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (FunctionVariables variables : FunctionVariables.values()) {
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
        return "Particle";
    }

    @Override
    public String description() {
        return "&7Play any particle you want.";
    }

    @Override
    public Material material() {
        return Material.REDSTONE;
    }

    @Override
    public AdvancedFunctions getGUI() {
        return this;
    }

    public enum FunctionVariables{
        PARTICLE("Type", Material.REDSTONE, GUIType.ENUM, Particles.class, null),
        RANGE("Range", Material.IRON_BLOCK, GUIType.INT, 1, 30, FunctionVariables.PARTICLE),
        DAMAGE("Damage", Material.STICK, GUIType.FLOAT, 0, 10000, FunctionVariables.PARTICLE);

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

    public enum Particles {
        FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK, Material.FIREWORK),
        CRIT(EnumParticle.CRIT, Material.IRON_SWORD),
        SPELL(EnumParticle.SPELL, Material.STICK),
        NOTE(EnumParticle.NOTE, Material.NOTE_BLOCK),
        PORTAL(EnumParticle.PORTAL, Material.ENDER_PORTAL_FRAME),
        FLAME(EnumParticle.FLAME, Material.FLINT_AND_STEEL),
        FOOTSTEP(EnumParticle.FOOTSTEP, Material.ARROW),
        SMOKE_NORMAL(EnumParticle.SMOKE_NORMAL, Material.FLINT),
        EXPLOSION_HUGE(EnumParticle.EXPLOSION_HUGE, Material.TNT),
        EXPLOSION_LARGE(EnumParticle.EXPLOSION_LARGE, Material.TNT),
        EXPLOSION_NORMAL(EnumParticle.EXPLOSION_NORMAL, Material.TNT),
        SMOKE_LARGE(EnumParticle.SMOKE_LARGE, Material.FLINT),
        CLOUD(EnumParticle.CLOUD, Material.CLAY_BALL),
        SNOWBALL(EnumParticle.SNOWBALL, Material.SNOW_BALL),
        WATERDRIP(EnumParticle.WATER_DROP, Material.WATER_BUCKET),
        LAVADRIP(EnumParticle.DRIP_LAVA, Material.LAVA_BUCKET),
        SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL, Material.IRON_SPADE),
        SLIME(EnumParticle.SLIME, Material.SLIME_BALL),
        HEART(EnumParticle.HEART, Material.COOKED_BEEF),
        ITEM_CRACK(EnumParticle.ITEM_CRACK, Material.IRON_PICKAXE),
        ITEM_TAKE(EnumParticle.ITEM_TAKE, Material.IRON_PICKAXE),
        WATER_BUBBLE(EnumParticle.WATER_BUBBLE, Material.WATER_BUCKET),
        WATER_SPLASH(EnumParticle.WATER_SPLASH, Material.WATER_BUCKET),
        WATER_WAKE(EnumParticle.WATER_WAKE, Material.WATER_BUCKET),
        SUSPENDED(EnumParticle.SUSPENDED, Material.CLAY_BALL),
        SUSPENDED_DEPTH(EnumParticle.SUSPENDED_DEPTH, Material.CLAY_BALL),
        CRIT_MAGIC(EnumParticle.CRIT_MAGIC, Material.STICK),
        SPELL_INSTANT(EnumParticle.SPELL_INSTANT, Material.STICK),
        SPELL_MOB(EnumParticle.SPELL_MOB, Material.STICK),
        SPELL_MOB_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT, Material.STICK),
        SPELL_WITCH(EnumParticle.SPELL_WITCH, Material.STICK),
        DRIP_WATER(EnumParticle.DRIP_WATER, Material.WATER_BUCKET),
        DRIP_LAVA(EnumParticle.DRIP_LAVA, Material.LAVA_BUCKET),
        VILLAGER_ANGRY(EnumParticle.VILLAGER_ANGRY, Material.HAY_BLOCK),
        VILLAGER_HAPPY(EnumParticle.VILLAGER_HAPPY, Material.HAY_BLOCK),
        TOWN_AURA(EnumParticle.TOWN_AURA, Material.HAY_BLOCK),
        ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE, Material.ENCHANTMENT_TABLE),
        LAVA(EnumParticle.LAVA, Material.LAVA_BUCKET),
        REDSTONE(EnumParticle.REDSTONE, Material.REDSTONE),
        BARRIER(EnumParticle.BARRIER, Material.BARRIER),
        BLOCK_CRACK(EnumParticle.BLOCK_CRACK, Material.IRON_PICKAXE),
        BLOCK_DUST(EnumParticle.BLOCK_CRACK, Material.IRON_PICKAXE),
        WATER_DROP(EnumParticle.WATER_DROP, Material.WATER_BUCKET);

        private final EnumParticle a;
        private final Material b;

        public EnumParticle getA() {
            return this.a;
        }

        public Material getB() {
            return this.b;
        }

        Particles(EnumParticle a, Material b) {
            this.a = a;
            this.b = b;
        }
    }
}


