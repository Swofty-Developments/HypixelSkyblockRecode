package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class UnstableDragon extends AbstractDragon {


    public UnstableDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.UNSTABLE;
    }

    @Override
    public double getEnHealth() {
        return 9000000;
    }

}
