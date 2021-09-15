package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.AuctionHouse.guis.AuctionCreatorGUI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.atlas.SkyblockSandbox.SBX.cachedSkillLvls;
import static net.atlas.SkyblockSandbox.SBX.cachedSkills;

public class PlayerLeave extends SkyblockListener<PlayerQuitEvent> {

    @EventHandler
    public void callEvent(PlayerQuitEvent event) {
        MongoCoins playerData = SBX.getMongoStats();
        if (AuctionCreatorGUI.PlayerItems.containsKey(event.getPlayer().getUniqueId())) {
            playerData.setData(event.getPlayer().getUniqueId(), "AuctionItem", AuctionCreatorGUI.PlayerItems.get(event.getPlayer().getUniqueId()));
        }
        for(SkillType type:cachedSkills.get(event.getPlayer().getUniqueId()).keySet()) {
            double amt = cachedSkills.get(event.getPlayer().getUniqueId()).get(type);
            playerData.setData(event.getPlayer().getUniqueId(),type.getName() + "_xp",amt);
        }
        for(SkillType type:cachedSkillLvls.get(event.getPlayer().getUniqueId()).keySet()) {
            int amt = cachedSkillLvls.get(event.getPlayer().getUniqueId()).get(type);
            playerData.setData(event.getPlayer().getUniqueId(), type.getName() + "_lvl",amt);
        }
        cachedSkillLvls.remove(event.getPlayer().getUniqueId());
        cachedSkills.remove(event.getPlayer().getUniqueId());
    }
}
