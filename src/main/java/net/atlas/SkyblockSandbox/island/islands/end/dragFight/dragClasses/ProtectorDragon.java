package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class ProtectorDragon extends AbstractDragon{


    public ProtectorDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.PROTECTOR;
    }

    @Override
    public double getEnHealth() {
        return 9000000;
    }

}
