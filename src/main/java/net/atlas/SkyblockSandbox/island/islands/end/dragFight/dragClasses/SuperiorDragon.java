package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class SuperiorDragon extends AbstractDragon {


    public SuperiorDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.SUPERIOR;
    }

    @Override
    public double getEnHealth() {
        return 12000000;
    }

}
