package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;

import net.atlas.SkyblockSandbox.event.customEvents.ManaEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.event.EventHandler;

public class ManaUseListener extends SkyblockListener<ManaEvent> {

    @EventHandler
    public void callEvent(ManaEvent event) {
        String name = SUtil.colorize(event.getAbilityName());
        int manaCost = event.getManaCost();
        SBPlayer p = event.getPlayer();
        p.queueMiddleActionText(p,SUtil.colorize("&b    &b-" + manaCost + " Mana (&6" + name + "&b)    "),20L);
    }
}
