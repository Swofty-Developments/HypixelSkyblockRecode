package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import org.bukkit.World;

public class VoidgloomDragon extends AbstractDragon{
    public VoidgloomDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return DragonTypes.VOIDGLOOM;
    }

    @Override
    public double getEnHealth() {
        return 77777777;
    }
}
