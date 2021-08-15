package net.atlas.SkyblockSandbox.entity.pathfinderGoal;

import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalSummonOwnerHurtTarget extends PathfinderGoalTarget {
    EntityCreature a;
    EntityLiving b;
    EntityLiving owner;
    private int c;

    public PathfinderGoalSummonOwnerHurtTarget(EntityCreature entitytameableanimal, EntityLiving owner) {
        super(entitytameableanimal, false);
        this.a = entitytameableanimal;
        this.owner = owner;
        this.a(1);
    }

    public boolean a() {
        EntityLiving entityliving = owner;
        this.b = entityliving.bf();

        int i = entityliving.bg();
        return i != this.c && a(this.b, true);
    }

    public void c() {
        System.out.println(b);
        this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true);
        System.out.println(this.e.getGoalTarget());
        EntityLiving entityliving = owner;
        if (entityliving != null) {
            this.c = entityliving.bg();
        }

        super.c();
    }

    @Override
    protected boolean a(EntityLiving var1, boolean var2) {
        if (!a(this.e, var1, var2, this.f)) {
            return false;
        } else return this.e.e(new BlockPosition(var1));
    }
}

