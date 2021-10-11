package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.royawesome.jlibnoise.module.modifier.Abs;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.awt.*;
import java.lang.reflect.Field;

public class CustomEnderDragon extends AbstractDragon {

    public CustomEnderDragon(World loc) {
        super(loc);
    }

    @Override
    public DragonTypes getDragonType() {
        return null;
    }

    @Override
    public double getEnHealth() {
        return 0;
    }
}
