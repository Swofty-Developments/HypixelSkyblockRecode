package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class YoungDragon extends AbstractDragon {


    public YoungDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.YOUNG;
    }

    @Override
    public double getEnHealth() {
        return 7500000;
    }


}
