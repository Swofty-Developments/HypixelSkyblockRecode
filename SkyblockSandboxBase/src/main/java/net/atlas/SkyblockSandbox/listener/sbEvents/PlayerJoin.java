package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.*;

public class PlayerJoin extends SkyblockListener<PlayerJoinEvent> {

    //public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> currStats = new HashMap<>();
    public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> maxStats = new HashMap<>();
    public static HashMap<UUID,HashMap<SBPlayer.PlayerStat, Double>> bonusStats = new HashMap<>();

    @EventHandler
    public void callEvent(PlayerJoinEvent event) {

        SBPlayer p = new SBPlayer(event.getPlayer());
        HashMap<SBPlayer.PlayerStat,Double> maxStat = new HashMap<>();
        HashMap<SBPlayer.PlayerStat,Double> empty = new HashMap<>();
        for (SBPlayer.PlayerStat s : SBPlayer.PlayerStat.values()) {
            double tempStat = NBTUtil.getAllStats(p).get(s);
            maxStat.put(s,s.getBase() + tempStat);
            empty.put(s,0D);
        }
        maxStats.put(p.getUniqueId(), maxStat);
        //currStats.put(p.getUniqueId(), maxStat);
        bonusStats.put(p.getUniqueId(),empty);
        SBX.getInstance().coins.loadCoins(p.getPlayer());
        DragonScoreboard scoreboard = new DragonScoreboard(SBX.getInstance());
        scoreboard.setScoreboard(p.getPlayer());

        //p.setStat(HEALTH,p.getMaxStat(HEALTH));
        //p.setStat(INTELLIGENCE, p.getMaxStat(INTELLIGENCE));
        for(SBPlayer.PlayerStat s: SBPlayer.PlayerStat.values()) {
            p.setStat(s,p.getMaxStat(s));
        }
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
        }
        for(SkillType t:SkillType.values()) {
            p.addSkillXP(t,0);
        }

    }
}
