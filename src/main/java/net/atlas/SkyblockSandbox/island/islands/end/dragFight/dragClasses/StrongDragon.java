package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class StrongDragon extends AbstractDragon {


    public StrongDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.STRONG;
    }

    @Override
    public double getEnHealth() {
        return 9000000;
    }

}
