package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Implosion extends Function {
    public Implosion(SBPlayer player,ItemStack stack, int abilIndex, int functionIndex) {
        super(player,stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        SBPlayer player = getPlayer();
        int range = Integer.parseInt(String.valueOf(FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(),dataValues.TELEPORT_RANGE)));
        boolean message = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.SEND_MESSAGE));
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

    public enum dataValues implements Function.dataValues {
        IMPLOSION_RANGE, TELEPORT_RANGE, SEND_MESSAGE;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return Arrays.asList(Teleport.class);
    }
}
