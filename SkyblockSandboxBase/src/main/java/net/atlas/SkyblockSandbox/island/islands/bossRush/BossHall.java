package net.atlas.SkyblockSandbox.island.islands.bossRush;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class BossHall {
    public static final Location spawnLoc = new Location(Bukkit.getWorlds().get(0),129, 68, -52);
    public static List<BossPedestal> bossList = new ArrayList<>();

    public void addBoss(Location l, DungeonBoss db) {
        BossPedestal bp = new BossPedestal(l);
        bp.setBoss(db);
        bossList.add(bp);
    }

    private boolean removeBoss(DungeonBoss db) {
        return bossList.removeIf(bp -> db == bp.boss);
    }

    private boolean containsBoss(DungeonBoss db) {
        for (BossPedestal bp : bossList) {
            if (bp.boss == db) {
                return true;
            }
        }
        return false;
    }

    public BossPedestal getBossPedestal(DungeonBoss db) {
        for (BossPedestal bp : bossList) {
            if (bp.boss == db) {
                return bp;
            }
        }
        return null;
    }

    public static BossPedestal getFromName(String name) {
        for (BossPedestal bp : bossList) {
            if (bp.getBoss().getName().equals(name)) {
                return bp;
            }
        }
        return null;
    }

    public static class BossPedestal {
        private final Location pdLoc;
        private DungeonBoss boss;

        public BossPedestal(Location loc) {
            pdLoc = loc;
        }

        public void setBoss(DungeonBoss db) {
            boss = db;
        }

        public void showPedestal() {
            Entity en = boss.spawn(pdLoc, false);
            en.setMetadata("br_pedestal", new FixedMetadataValue(SBX.getInstance(), boss.getName()));
        }

        public DungeonBoss getBoss() {
            return boss;
        }
    }
}
