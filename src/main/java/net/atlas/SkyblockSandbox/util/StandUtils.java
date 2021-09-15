package net.atlas.SkyblockSandbox.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class StandUtils {

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {

        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, centerBlock.getY(), z);

                        if(!circleBlocks.contains(l)) circleBlocks.add(l);
                    }

                }
            }
        }

        return circleBlocks;
    }
}
