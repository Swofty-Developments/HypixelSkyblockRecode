package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerEvent extends SkyblockListener<FoodLevelChangeEvent> {
    @EventHandler
    public void callEvent(FoodLevelChangeEvent event) {
        if(event.getFoodLevel()!=20) {
            event.setFoodLevel(20);
        } else {
            event.setCancelled(true);
        }

    }
}
