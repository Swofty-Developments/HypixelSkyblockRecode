package net.atlas.SkyblockSandbox.slayer.slayerBosses;

import net.atlas.SkyblockSandbox.slayer.MiniBossTier;
import net.atlas.SkyblockSandbox.slayer.Slayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class EndSlayer extends Slayer {
    @Override
    public EntityType getSlayerType() {
        return EntityType.ENDERMAN;
    }

    @Override
    public String getSlayerName() {
        return SUtil.colorize("&5&lVoidgloom Seraph");
    }

    @Override
    public SlayerTier getMaxTier() {
        return SlayerTier.FOUR;
    }

    @Override
    public Slayers getSlayerClass() {
        return Slayers.ENDERMAN;
    }

    @Override
    public HashMap<SlayerTier, Integer> getSlayerHealth() {
        HashMap<SlayerTier,Integer> map = new HashMap<>();
        map.put(SlayerTier.ONE,300000);
        map.put(SlayerTier.TWO,15000000);
        map.put(SlayerTier.THREE,66666666);
        map.put(SlayerTier.FOUR,300000000);
        return map;
    }

    @Override
    public HashMap<MiniBossTier, Integer> getMinibossHealth() {
        HashMap<MiniBossTier,Integer> map = new HashMap<>();
        map.put(MiniBossTier.ONE,5000000);
        map.put(MiniBossTier.TWO,20000000);
        map.put(MiniBossTier.THREE,50000000);
        return map;
    }

    @Override
    public HashMap<SlayerTier, Integer> getDPS() {
        HashMap<SlayerTier,Integer> map = new HashMap<>();
        map.put(SlayerTier.ONE,1200);
        map.put(SlayerTier.TWO,5000);
        map.put(SlayerTier.THREE,12000);
        map.put(SlayerTier.FOUR,21000);
        return map;
    }
}
