package net.atlas.SkyblockSandbox.abilityCreator;

import net.atlas.SkyblockSandbox.abilityCreator.functions.*;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum AbilityValue {
    NAME, CLICK_TYPE, STAT_COST,COOLDOWN,MANA_COST;

    AbilityValue() {

    }

    public enum FunctionType {
        IMPLOSION(Implosion.class),
        TELEPORT(Teleport.class),
        PARTICLE(Particle.class),
        SOUND(Sound.class),
        PROJECTILE(Projectile.class),
        HEAD(Head.class),
        SHORTBOW(Shortbow.class);

        private Class<? extends Function> function;

        FunctionType(Class<? extends Function> function) {
            this.function = function;
        }

        public Function getFunction(SBPlayer p,ItemStack stack,int abilIndex,int funcIndex) {
            try {
                Constructor<? extends Function> ctor = function.getDeclaredConstructor(SBPlayer.class, ItemStack.class, int.class, int.class);
                Function func = ctor.newInstance(p,stack,abilIndex,funcIndex);
                return func;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
