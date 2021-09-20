package net.atlas.SkyblockSandbox.abilityCreator;

import net.atlas.SkyblockSandbox.abilityCreator.functions.Implosion;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Particle;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Sound;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Teleport;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum AbilityValue {
    NAME, CLICK_TYPE, STAT_COST;

    AbilityValue() {

    }

    public static enum FunctionType {
        IMPLOSION(Implosion.class),
        TELEPORT(Teleport.class),
        PARTICLE(Particle.class),
        SOUND(Sound.class),
        PROJECTILE(Implosion.class);

        private Class<? extends Function> function;

        FunctionType(Class<? extends Function> function) {
            this.function = function;
        }

        public Function getFunction(SBPlayer p,ItemStack stack,int abilIndex,int funcIndex) {
            try {
                Constructor<? extends Function> ctor = function.getDeclaredConstructor(SBPlayer.class, ItemStack.class, int.class, int.class);
                return ctor.newInstance(p,stack,abilIndex,funcIndex);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
