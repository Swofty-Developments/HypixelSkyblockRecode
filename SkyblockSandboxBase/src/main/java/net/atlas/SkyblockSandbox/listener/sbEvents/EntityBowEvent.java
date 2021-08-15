package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.entity.customEntity.CustomArrow;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.minecraft.server.v1_8_R3.EntityPotion;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityBowEvent extends SkyblockListener<EntityShootBowEvent> {
    @EventHandler
    public void callEvent(EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            int j = 72000-71950;
            float f = (float)j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if ((double)f < 0.1D) {
                return;
            }


            if (f > 1.0F) {
                f = 1.0F;
            }
            //CustomArrow customArrow = new CustomArrow(((CraftWorld)p.getWorld()).getHandle(),((CraftPlayer)p).getHandle(),0.4f);
            //event.getProjectile().remove();
            //((CraftWorld)p.getWorld()).getHandle().addEntity(customArrow);

        }
    }
}
