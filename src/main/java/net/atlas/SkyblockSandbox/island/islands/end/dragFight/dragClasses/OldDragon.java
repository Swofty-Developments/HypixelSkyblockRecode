package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class OldDragon extends AbstractDragon{


    public OldDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.OLD;
    }

    @Override
    public double getEnHealth() {
        return 15000000;
    }

}
