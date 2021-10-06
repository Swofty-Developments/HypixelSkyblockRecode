package net.atlas.SkyblockSandbox.island.islands;

import net.atlas.SkyblockSandbox.player.SBPlayer;

import java.util.HashMap;
import java.util.UUID;

public class FairySouls {

    private final static int initHealthBonus = 3;

    public static HashMap<UUID,Integer> cachedFairySouls = new HashMap<>();

    public static HashMap<SBPlayer.PlayerStat,Double> getPlayerRewards(SBPlayer p) {
        HashMap<SBPlayer.PlayerStat,Double> map = new HashMap<>();
        map.put(SBPlayer.PlayerStat.DEFENSE,0D);
        map.put(SBPlayer.PlayerStat.STRENGTH,0D);
        map.put(SBPlayer.PlayerStat.SPEED,0D);
        map.put(SBPlayer.PlayerStat.HEALTH,0D);
        if(cachedFairySouls.containsKey(p.getUniqueId())) {
            int soulAmt = cachedFairySouls.get(p.getUniqueId());
            if (soulAmt % 5 == 0) {
                int exchangedAmt = soulAmt / 5;
                for (int i = 1; i <= exchangedAmt; i++) {
                    double healthBonus = Double.valueOf(Math.ceil((i / 2D) + 2)).intValue();
                    if (soulAmt % 25 == 0) {
                        map.put(SBPlayer.PlayerStat.DEFENSE, 2D + map.get(SBPlayer.PlayerStat.DEFENSE));
                        map.put(SBPlayer.PlayerStat.STRENGTH, 2D + map.get(SBPlayer.PlayerStat.DEFENSE));
                        if (soulAmt % 50 == 0) {
                            map.put(SBPlayer.PlayerStat.SPEED, 1D + map.get(SBPlayer.PlayerStat.DEFENSE));
                        }
                    } else {
                        map.put(SBPlayer.PlayerStat.DEFENSE, 1D + map.get(SBPlayer.PlayerStat.DEFENSE));
                        map.put(SBPlayer.PlayerStat.STRENGTH, 1D + map.get(SBPlayer.PlayerStat.DEFENSE));
                    }
                    map.put(SBPlayer.PlayerStat.HEALTH, map.get(SBPlayer.PlayerStat.HEALTH) + healthBonus);
                }
            }
            return map;
        }
        return map;
    }
}
