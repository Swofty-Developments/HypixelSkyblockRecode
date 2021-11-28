package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.gui.guis.ProfileViewer;
import net.atlas.SkyblockSandbox.gui.guis.ShowcaseGUI;
import net.atlas.SkyblockSandbox.gui.guis.slayerGUI.MaddoxGUI;
import net.atlas.SkyblockSandbox.island.islands.bossRush.BossHall;
import net.atlas.SkyblockSandbox.island.islands.bossRush.DungeonBoss;
import net.atlas.SkyblockSandbox.island.islands.bossRush.components.NecronBoss;
import net.atlas.SkyblockSandbox.item.SBItemStack;
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
        } else {
            if(event.getRightClicked().hasMetadata("showcase")) {
                int index = event.getRightClicked().getMetadata("showcase").get(0).asInt();
                new ShowcaseGUI(new SBPlayer(event.getPlayer()),index).open();
                event.setCancelled(true);
            }
            if(event.getRightClicked().hasMetadata("br_pedestal")) {
                String bossStr = event.getRightClicked().getMetadata("br_pedestal").get(0).asString();
                BossHall.BossPedestal bp = BossHall.getFromName(bossStr);
                event.getPlayer().teleport(BossHall.spawnLoc);
                assert bp != null;
                //bp.getBoss().spawn(BossHall.spawnLoc.clone().add(0,5,0),true);
                //NecronBoss.spawn(BossHall.spawnLoc.clone().add(0,5,0),true);
            } else {
                if (event.getPlayer().isSneaking()) {
                    SBPlayer p = new SBPlayer(event.getPlayer());
                    new MaddoxGUI(p).open();
                }
            }
        }
    }
}
