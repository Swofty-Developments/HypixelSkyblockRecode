package net.atlas.SkyblockSandbox.abilityCreator;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue.FunctionType;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Implosion;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Particle;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Sound;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Teleport;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil.*;

public class AbilityHandler {
    public void callEvent(PlayerInteractEvent event) {
        SBPlayer p = new SBPlayer(event.getPlayer());
        ItemStack craftItem = event.getItem();
        SBItemStack sbItem = new SBItemStack(craftItem);
        if(getGenericAbilityString(craftItem,"has-ability").equalsIgnoreCase("true")) {
            for (int i = 0; i < getAbilityAmount(craftItem); i++) {
                String abilName = getAbilityString(craftItem,i,AbilityValue.NAME.name());

                String clickTypeString = getAbilityData(craftItem,i,AbilityValue.CLICK_TYPE);
                clickTypeString = clickTypeString.split("_")[0];
                ClickType type = Enums.getIfPresent(ClickType.class,clickTypeString).orNull();
                if(type!=null) {
                    if(event.getAction().name().contains(type.name())) {
                        for(int ii=0;ii<FunctionUtil.getFunctionAmount(craftItem,i);i++) {
                            getFunction(p, craftItem, i, ii).getFunction(p,craftItem,i,ii).applyFunction();
                        }
                    }
                }
            }
        }
    }

    public FunctionType getFunction(SBPlayer p,ItemStack item,int abilIndex,int funcIndex) {
            String funcType = FunctionUtil.getFunctionData(item,abilIndex,funcIndex, Function.FunctionValues.NAME);
            FunctionType type = Enums.getIfPresent(FunctionType.class,funcType).orNull();
            if(type==null) {
                p.sendMessage(SUtil.colorize("&cSomething went wrong while parsing functions! Please contact an admin if this issue persists."));
                return null;
            }
            return type;
    }
}
