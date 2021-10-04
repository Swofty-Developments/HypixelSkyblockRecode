package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static net.atlas.SkyblockSandbox.item.ability.itemAbilities.ShortBowTerm.canfire;
import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

public class Shortbow extends Function {
    public Shortbow(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        SBPlayer player = getPlayer();
        if (!canfire.containsKey(player.getPlayer())) {
            canfire.put(player.getPlayer(), true);
        }
        if (canfire.get(player.getPlayer())) {
            canfire.put(player.getPlayer(), false);
            String arrowStr = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.ARROW_AMOUNT);
            if (arrowStr.isEmpty()) {
                arrowStr = "1";
            }
            if (!NumUtils.isInt(arrowStr)) {
                return;
            }
            int arrowAmt = Integer.parseInt(arrowStr);
            if (arrowAmt > 5) {
                arrowAmt = 5;
            }
            double angle = (100.76 / (arrowAmt - 1));
            Arrow a1 = null;
            for (int i = 0; i < arrowAmt; i++) {
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setCustomName("terminator");
                if (arrowAmt > 1) {
                    switch (i) {
                        case 0:
                            arrow.setVelocity(arrow.getVelocity().multiply(2.5));
                            a1 = arrow;
                            break;
                        case 1:
                            arrow.setVelocity(rotateVector(a1.getVelocity(), -angle));
                            break;
                        case 2:
                            arrow.setVelocity(rotateVector(a1.getVelocity(), angle));
                            break;
                        case 3:
                            arrow.setVelocity(rotateVector(a1.getVelocity(), -angle * (arrowAmt - 1) / 2));
                            break;
                        case 4:
                            arrow.setVelocity(rotateVector(a1.getVelocity(), angle * (arrowAmt - 1) / 2));
                            break;
                    }

                }
            }
            player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    canfire.put(player.getPlayer(), true);
                }
            }.runTaskLater(SBX.getInstance(), 87 / 13);

        }
    }

    public Vector rotateVector(Vector vector, double whatAngle) {
        double cos = Math.cos(whatAngle);
        double sin = Math.sin(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }

    public enum dataValues implements Function.dataValues {
        ARROW_AMOUNT();
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.SHORTBOW;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return null;
    }

    @Override
    public void getGuiLayout() {
        String arrows = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.ARROW_AMOUNT);
        if (arrows.isEmpty()) {
            arrows = "&cNOT SET &7(Defaults to one)";
        }
        setItem(13, makeColorfulItem(Material.ARROW, "&aSet arrow amount!", 1, 0, "&7Current arrow amount: &a" + arrows, "", "&eClick to set!", "&cMAX 5 ARROWS!"));
        setAction(13, event -> {
            anvilGUI(dataValues.ARROW_AMOUNT);
        });
    }
}
