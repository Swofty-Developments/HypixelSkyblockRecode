package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EnderDragon;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class AbstractDragon extends EntityEnderDragon {

    public AbstractDragon(org.bukkit.World loc) {
        super(((CraftWorld)loc).getHandle());

        EnderDragon craftDragon = (EnderDragon) this.getBukkitEntity();

        //craftDragon.setMaxHealth(this.getEnHealth());
        //this.setHealth((float) this.getEnHealth());
        craftDragon.setMetadata(this.getDragonType().getMobName(),new FixedMetadataValue(SBX.getInstance(),this));
        StartFight.maxDragHealth = this.getEnHealth();
        StartFight.dragonHealth = this.getEnHealth();


    }

    public abstract DragonTypes getDragonType();

    public abstract double getEnHealth();


}
