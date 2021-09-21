package net.atlas.SkyblockSandbox.abilityCreator.functions;

import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

public class Implosion extends Function {
    public Implosion(SBPlayer player,ItemStack stack, int abilIndex, int functionIndex) {
        super(player,stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        SBPlayer player = getPlayer();
        int range = Integer.parseInt(String.valueOf(FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(),dataValues.TELEPORT_RANGE)));
        boolean message = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(), FunctionValues.SEND_MESSAGE));
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
        IMPLOSION_RANGE, TELEPORT_RANGE
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return Arrays.asList(Teleport.class);
    }

    @Override
    public void getGuiLayout() {
        setItem(13, makeColorfulItem(Material.BOOK_AND_QUILL, "&aImplosion Range", 1, 0, "&7Edit the range of the","&bImplosion Function&7!","","&eClick to set!"));
        setItem(14, makeColorfulItem(Material.FEATHER, "&aToggle message", 1, 0, "&7Turn on and off the","&bImplosion Function &7message!","","&bRight-click to disable!","&eLeft-click to enable!"));
        setAction(13,event -> {
            anvilGUI(dataValues.IMPLOSION_RANGE);
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
