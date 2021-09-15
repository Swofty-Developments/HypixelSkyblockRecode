package net.atlas.SkyblockSandbox.entity.pathfinderGoal;

import net.minecraft.server.v1_8_R3.*;

public class PathfinderGoalFastMeleeAttack extends PathfinderGoal {
    World a;
    protected EntityCreature b;
    int c;
    double d;
    boolean e;
    PathEntity f;
    Class<? extends Entity> g;
    private int h;
    private double i;
    private double j;
    private double k;

    public PathfinderGoalFastMeleeAttack(EntityCreature var1, Class<? extends Entity> var2, double var3, boolean var5) {
        this(var1, var3, var5);
        this.g = var2;
    }

    public PathfinderGoalFastMeleeAttack(EntityCreature var1, double var2, boolean var4) {
        this.b = var1;
        this.a = var1.world;
        this.d = var2;
        this.e = var4;
        this.a(3);
    }

    public boolean a() {
        EntityLiving var1 = this.b.getGoalTarget();
        if (var1 == null) {
            return false;
        } else if (!var1.isAlive()) {
            return false;
        } else if (this.g != null && !this.g.isAssignableFrom(var1.getClass())) {
            return false;
        } else {
            this.f = this.b.getNavigation().a(var1);
            return this.f != null;
        }
    }

    public boolean b() {
        EntityLiving var1 = this.b.getGoalTarget();
        if (var1 == null) {
            return false;
        } else if (!var1.isAlive()) {
            return false;
        } else if (!this.e) {
            return !this.b.getNavigation().m();
        } else {
            return this.b.e(new BlockPosition(var1));
        }
    }

    public void c() {
        this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void d() {
        this.b.getNavigation().n();
    }

    @Override
    public void e() {
        EntityLiving var1 = this.b.getGoalTarget();
        this.b.getControllerLook().a(var1, 30.0F, 30.0F);
        double var2 = this.b.e(var1.locX, var1.getBoundingBox().b, var1.locZ);
        double var4 = this.a(var1);
        --this.h;
        if ((this.e || this.b.getEntitySenses().a(var1)) && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || var1.e(this.i, this.j, this.k) >= 1.0D || this.b.bc().nextFloat() < 0.05F)) {
            this.i = var1.locX;
            this.j = var1.getBoundingBox().b;
            this.k = var1.locZ;
            this.h = 4 + this.b.bc().nextInt(7);
            if (var2 > 1024.0D) {
                this.h += 10;
            } else if (var2 > 256.0D) {
                this.h += 5;
            }

            if (!this.b.getNavigation().a(var1, this.d)) {
                this.h += 15;
            }
        }

        this.c = Math.max(this.c - 1, 0);
        if (var2 <= var4 && this.c <= 0) {
            this.c = 1;
            if (this.b.bA() != null) {
                this.b.bw();
            }

            this.b.r(var1);
        }

    }

    protected double a(EntityLiving var1) {
        return (double) (this.b.width * 2.0F * this.b.width * 2.0F + var1.width);
    }


}
