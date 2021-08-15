package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class WiseDragon extends AbstractDragon{


    public WiseDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.WISE;
    }

    @Override
    public double getEnHealth() {
        return 9000000;
    }


}
