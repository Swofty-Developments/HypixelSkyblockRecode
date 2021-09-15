package net.atlas.SkyblockSandbox.entity.pathfinderGoal;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTameableAnimal;
import net.minecraft.server.v1_8_R3.PathfinderGoalTarget;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalSummonOwnerHurtByTarget extends PathfinderGoalTarget {
    EntityCreature a;
    EntityLiving b;
    EntityLiving owner;
    private int c;

    public PathfinderGoalSummonOwnerHurtByTarget(EntityCreature entitytameableanimal, EntityLiving owner) {
        super(entitytameableanimal, false);
        this.a = entitytameableanimal;
        this.owner = owner;
        this.a(1);
    }

    public boolean a() {
        EntityLiving entityliving = owner;
        if (entityliving == null) {
            return false;
        } else {
            this.b = entityliving.getLastDamager();
            int i = entityliving.be();
            return i != this.c && this.a(this.b, true);
        }

    }

    public void c() {
        this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
        EntityLiving entityliving = owner;
        if (entityliving != null) {
            this.c = entityliving.be();
        }

        super.c();
    }
}
