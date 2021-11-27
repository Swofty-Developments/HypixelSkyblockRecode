package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.gui.guis.ProfileViewer;
import net.atlas.SkyblockSandbox.gui.guis.slayerGUI.MaddoxGUI;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerRCEntity extends SkyblockListener<PlayerInteractAtEntityEvent> {

    @EventHandler
    public void callEvent(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() instanceof Player && !(event.getRightClicked() instanceof NPC)) {
            SBPlayer clicked = new SBPlayer(((Player) event.getRightClicked()));
            SBPlayer p = new SBPlayer(event.getPlayer());
            new ProfileViewer(p,clicked.getUniqueId().toString()).open();
        }
    }
}
