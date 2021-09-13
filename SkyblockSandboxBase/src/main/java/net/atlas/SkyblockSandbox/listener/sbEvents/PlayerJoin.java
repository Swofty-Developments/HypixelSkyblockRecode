package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.storage.MongoStorage;
import net.atlas.SkyblockSandbox.storage.StorageCache;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.SBX.cachedSkills;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.*;

public class PlayerJoin extends SkyblockListener<PlayerJoinEvent> {

    //public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> currStats = new HashMap<>();
    public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> maxStats = new HashMap<>();
    public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> bonusStats = new HashMap<>();

    @EventHandler
    public void callEvent(PlayerJoinEvent event) {

        SBPlayer p = new SBPlayer(event.getPlayer());
        System.out.println(p.getServer().getName());
        if(p.getServer().getServerName().equalsIgnoreCase("islands")) {
            if(p.hasIsland()) {
                Location teleLoc = p.getPlayerIsland().getCenter();
                while (teleLoc.getBlock().getType()!= Material.AIR) {
                    teleLoc.add(0,1,0);
                }
                p.teleport(teleLoc);
            } else {
                try {
                    Data.createIsland(p.getPlayer(), IslandId.randomIslandId());
                } catch (Exception ex) {
                    p.sendMessage(SUtil.colorize("&cFailed to create island. Please contact a server administrator if this issue persists."));
                }
            }
        }
        HashMap<SBPlayer.PlayerStat,Double> maxStat = new HashMap<>();
        HashMap<SBPlayer.PlayerStat,Double> empty = new HashMap<>();
        for (SBPlayer.PlayerStat s : SBPlayer.PlayerStat.values()) {
            double tempStat = NBTUtil.getAllStats(p).get(s);
            maxStat.put(s,tempStat);
            empty.put(s,0D);
        }
        maxStats.put(p.getUniqueId(), maxStat);
        bonusStats.put(p.getUniqueId(),empty);
        SBX.getInstance().coins.loadCoins(p.getPlayer());
        MongoStorage mongoStorage = SBX.storage;
        mongoStorage.setPlayerData(p.getUniqueId().toString());
        StorageCache storage = new StorageCache(p);
        for (int i = 1; i <= 9; i++) {
            storage.refresh(i);
        }
        DragonScoreboard scoreboard = new DragonScoreboard(SBX.getInstance());
        scoreboard.setScoreboard(p.getPlayer());
        for(SBPlayer.PlayerStat s: SBPlayer.PlayerStat.values()) {
            p.setStat(s,p.getMaxStat(s));
        }
        p.updateStats();

        if(p.getMaxStat(HEALTH)>100) {
            double newHealth;
            double oldrng = (p.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
            if (oldrng == 0)
                newHealth = 0;
            else {
                double newRng = (40 - 0);
                newHealth = Math.floor(((p.getStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);
            }
            p.setMaxHealth(newHealth);
            p.setHealth(newHealth);
        } else {
            p.setMaxHealth(20);
        }
        for(SkillType t:SkillType.values()) {
            p.addSkillXP(t,0);
            p.setSkillLvl(t,0);
            Object lvl = SBX.getMongoStats().getData(p.getUniqueId(),t.getName() + "_lvl");
            if(lvl instanceof Double) {
                p.setSkillLvl(t, ((Double) lvl).intValue());
            } else {
                p.setSkillLvl(t, (Integer)lvl);
            }

            HashMap<SkillType,Double> temp = new HashMap<>(cachedSkills.get(p.getUniqueId()));
            temp.put(t, (Double) SBX.getMongoStats().getData(p.getUniqueId(),t.getName() + "_xp"));
            cachedSkills.put(p.getUniqueId(),temp);
        }

    }
}
