package net.atlas.SkyblockSandbox.slayer;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.entity.customEntity.NoTeleportEnderman;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public abstract class Slayer {

    public abstract EntityType getSlayerType();

    public abstract String getSlayerName();

    public abstract SlayerTier getMaxTier();

    public abstract Slayers getSlayerClass();

    public abstract HashMap<SlayerTier,Integer> getSlayerHealth();

    public abstract HashMap<MiniBossTier,Integer> getMinibossHealth();

    public abstract HashMap<SlayerTier,Integer> getDPS();



    public void spawnMiniboss(MiniBossTier tier,SBPlayer p, Location loc) {
        try {
            org.bukkit.entity.Entity enBase = loc.getWorld().spawnEntity(loc,getSlayerType());
            LivingEntity en = (LivingEntity) enBase;
            en.setMaxHealth(getMinibossHealth().get(tier));
            en.setHealth(getMinibossHealth().get(tier));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Entity spawnSlayer(SlayerTier tier,SBPlayer p,Location loc) {
        Entity returnEn = null;
        try {
            switch (getSlayerType()) {
                case ENDERMAN:
                    Enderman eman = NoTeleportEnderman.spawnAsSlayer(loc,p,tier);
                    eman.setMaxHealth(getSlayerHealth().get(tier));
                    eman.setHealth(getSlayerHealth().get(tier));
                    eman.setCustomName(getSlayerName());
                    eman.setTarget(p);
                    eman.setMetadata(getSlayerClass().toString(),new FixedMetadataValue(SBX.getInstance(),tier));
                    switch (tier) {
                        case ONE:
                            eman.setMetadata("hitshield",new FixedMetadataValue(SBX.getInstance(),10));
                            break;
                        case TWO:
                            eman.setMetadata("hitshield",new FixedMetadataValue(SBX.getInstance(),35));
                            break;
                        case THREE:
                            eman.setMetadata("hitshield",new FixedMetadataValue(SBX.getInstance(),70));
                            break;
                        case FOUR:
                            eman.setMetadata("hitshield",new FixedMetadataValue(SBX.getInstance(),100));
                            break;
                    }

                    returnEn = eman;
                    break;
                case ZOMBIE:
                case SPIDER:
                case WOLF:
                    org.bukkit.entity.Entity enBase = loc.getWorld().spawnEntity(loc,getSlayerType());
                    LivingEntity en = (LivingEntity) enBase;
                    en.setMaxHealth(getSlayerHealth().get(tier));
                    en.setHealth(getSlayerHealth().get(tier));
                    en.setCustomName(getSlayerName());
                    returnEn = en;
                    break;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnEn;
    }
}
