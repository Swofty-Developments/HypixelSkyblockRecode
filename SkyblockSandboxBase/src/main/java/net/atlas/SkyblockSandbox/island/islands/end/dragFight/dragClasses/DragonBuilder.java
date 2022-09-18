package net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.DragonTypes;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.PathFind;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.royawesome.jlibnoise.module.modifier.Abs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight.spawnLoc;

public class DragonBuilder {

    private double health = 200;
    private double moveSpeed = 0.75;
    private String name;
    private DragonTypes type;

    public static DragonBuilder init() {
        return new DragonBuilder();
    }

    public DragonBuilder health(double health) {
        this.health = health;
        return this;
    }

    public DragonBuilder moveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
        return this;
    }

    public DragonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DragonBuilder dragonType(DragonTypes type) {
        this.type = type;
        return this;
    }

    public AbstractDragon build(Location loc) {
        CustomEnderDragon dragon = new CustomEnderDragon(loc.getWorld());
        dragon.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        dragon.setCustomName(SUtil.colorize("&c&l" + name));


        StartFight.maxDragHealth = this.health;
        StartFight.dragonHealth = this.health;
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(dragon);

        dragon.getBukkitEntity().setMetadata(type.getMobName(),new FixedMetadataValue(SBX.getInstance(),this));

        ArmorStand as;
        as = (ArmorStand) loc.getWorld().spawnEntity(loc.subtract(0, 0, 0), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setGravity(false);
        as.setCustomName("follower");
        as.setCustomNameVisible(false);


        PathFind.getDragDirection(dragon, as,moveSpeed);

        return dragon;
    }


}
