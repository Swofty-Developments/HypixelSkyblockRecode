package net.atlas.SkyblockSandbox.abilityCreator;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil.*;

public class AbilityHandler extends SkyblockListener<PlayerInteractEvent> {
    @EventHandler
    public void callEvent(PlayerInteractEvent event) {
        SBPlayer p = new SBPlayer(event.getPlayer());
        ItemStack craftItem = event.getItem();
        SBItemStack sbItem = new SBItemStack(craftItem);

        if(getGenericAbilityString(craftItem,"has-ability").equalsIgnoreCase("true")) {
            for (int i = 0; i < getAbilityAmount(craftItem); i++) {
                String abilName = getAbilityString(craftItem,i,AbilityValue.NAME.name());
                String clickTypeString = getAbilityString(craftItem,i,AbilityValue.CLICK_TYPE.name());
                ClickType type = Enums.getIfPresent(ClickType.class,clickTypeString).orNull();

            }
        }
    }
}
