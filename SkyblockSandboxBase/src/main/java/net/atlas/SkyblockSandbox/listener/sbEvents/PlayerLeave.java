package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.AuctionHouse.guis.AuctionCreatorGUI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.island.islands.FairySouls;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import org.bson.Document;
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
        SBX.getInstance().coins.cacheCoins(event.getPlayer());
        Document doc = new Document();
        SBPlayer p = new SBPlayer(event.getPlayer());
        for(SkillType type:cachedSkills.get(event.getPlayer().getUniqueId()).keySet()) {
            double amt = p.getCurrentSkillExp(type);
            //playerData.setData(event.getPlayer().getUniqueId(),type.getName() + "_xp",amt);
            doc.put(type.getName() + "_xp",amt);
        }
        for(SkillType type:cachedSkillLvls.get(event.getPlayer().getUniqueId()).keySet()) {
            int amt = p.getSkillLvl(type);
            //playerData.setData(event.getPlayer().getUniqueId(), type.getName() + "_lvl",amt);
            doc.put(type.getName() + "_lvl",amt);
        }
        playerData.setData(event.getPlayer().getUniqueId(),"Skills",doc);
        playerData.setData(event.getPlayer().getUniqueId(),"fairy-souls", FairySouls.cachedFairySouls.get(event.getPlayer().getUniqueId()));
        cachedSkillLvls.remove(event.getPlayer().getUniqueId());
        cachedSkills.remove(event.getPlayer().getUniqueId());
        StorageCache storage = new StorageCache(new SBPlayer(event.getPlayer()));
        for (int i = 1; i <= 9; i++) {
            storage.toMongoFromCache(i);
        }
    }
}
