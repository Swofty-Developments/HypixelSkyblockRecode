package net.atlas.SkyblockSandbox.entity.customEntity;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class AnimBlock extends EntityFallingBlock {
    private IBlockData block;
    public int ticksLived;
    public boolean dropItem = true;
    private boolean e;
    public boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;

    public AnimBlock(World world) {
        super(world);
    }

    public AnimBlock(World world, double d0, double d1, double d2, IBlockData iblockdata) {
        super(world);
        this.block = iblockdata;
        this.k = true;
        this.setSize(0.98F, 0.98F);
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    @Override
    protected boolean s_() {
        return false;
    }

    @Override
    protected void h() {
    }

    @Override
    public boolean ad() {
        return !this.dead;
    }

    @Override
    public void t_() {
        Block block = this.block.getBlock();
        if (block.getMaterial() == Material.AIR) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            BlockPosition blockposition;
            if (this.ticksLived++ == 0) {
                blockposition = new BlockPosition(this);
                if (this.world.getType(blockposition).getBlock() == block && !CraftEventFactory.callEntityChangeBlockEvent(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), Blocks.AIR, 0).isCancelled()) {
                    this.world.setAir(blockposition);
                    this.world.spigotConfig.antiXrayInstance.updateNearbyBlocks(this.world, blockposition);
                } else if (!this.world.isClientSide) {
                    this.die();
                    return;
                }
            }

           // this.motY -= 0.03999999910593033D;
            //this.move(this.motX, this.motY, this.motZ);
            //this.motX *= 0.9800000190734863D;
            //this.motY *= 0.9800000190734863D;
            //this.motZ *= 0.9800000190734863D;
            if (!this.world.isClientSide) {
                blockposition = new BlockPosition(this);
            }
        }

    }

    public void e(float f, float f1) {
        Block block = this.block.getBlock();
        if (this.hurtEntities) {
            int i = MathHelper.f(f - 1.0F);
            if (i > 0) {
                ArrayList arraylist = Lists.newArrayList(this.world.getEntities(this, this.getBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); CraftEventFactory.entityDamage = null) {
                    Entity entity = (Entity) iterator.next();
                    CraftEventFactory.entityDamage = this;
                    entity.damageEntity(damagesource, (float) Math.min(MathHelper.d((float) i * this.fallHurtAmount), this.fallHurtMax));
                }

                if (flag && (double) this.random.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
                    int j = (Integer) this.block.get(BlockAnvil.DAMAGE);
                    ++j;
                    if (j > 2) {
                        this.e = true;
                    } else {
                        this.block = this.block.set(BlockAnvil.DAMAGE, j);
                    }
                }
            }
        }

    }

    protected void b(NBTTagCompound nbttagcompound) {
        Block block = this.block != null ? this.block.getBlock() : Blocks.AIR;
        MinecraftKey minecraftkey = (MinecraftKey) Block.REGISTRY.c(block);
        nbttagcompound.setString("Block", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.setByte("Data", (byte) block.toLegacyData(this.block));
        nbttagcompound.setByte("Time", (byte) this.ticksLived);
        nbttagcompound.setBoolean("DropItem", this.dropItem);
        nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nbttagcompound.setInt("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            nbttagcompound.set("TileEntityData", this.tileEntityData);
        }

    }

    protected void a(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getByte("Data") & 255;
        if (nbttagcompound.hasKeyOfType("Block", 8)) {
            this.block = Block.getByName(nbttagcompound.getString("Block")).fromLegacyData(i);
        } else if (nbttagcompound.hasKeyOfType("TileID", 99)) {
            this.block = Block.getById(nbttagcompound.getInt("TileID")).fromLegacyData(i);
        } else {
            this.block = Block.getById(nbttagcompound.getByte("Tile") & 255).fromLegacyData(i);
        }

        this.ticksLived = nbttagcompound.getByte("Time") & 255;
        Block block = this.block.getBlock();
        if (nbttagcompound.hasKeyOfType("HurtEntities", 99)) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nbttagcompound.getInt("FallHurtMax");
        } else if (block == Blocks.ANVIL) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.hasKeyOfType("DropItem", 99)) {
            this.dropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (nbttagcompound.hasKeyOfType("TileEntityData", 10)) {
            this.tileEntityData = nbttagcompound.getCompound("TileEntityData");
        }

        if (block == null || block.getMaterial() == Material.AIR) {
            this.block = Blocks.SAND.getBlockData();
        }

    }

    public void a(boolean flag) {
        this.hurtEntities = flag;
    }

    public void appendEntityCrashDetails(CrashReportSystemDetails crashreportsystemdetails) {
        super.appendEntityCrashDetails(crashreportsystemdetails);
        if (this.block != null) {
            Block block = this.block.getBlock();
            crashreportsystemdetails.a("Immitating block ID", Block.getId(block));
            crashreportsystemdetails.a("Immitating block data", block.toLegacyData(this.block));
        }

    }

    public IBlockData getBlock() {
        return this.block;
    }

    public static FallingBlock spawn(Location loc, org.bukkit.Material mat) {
        double x = (double) loc.getBlockX() + 0.5D;
        double y = (double) loc.getBlockY() + 0.5D;
        double z = (double) loc.getBlockZ() + 0.5D;
        AnimBlock entity = new AnimBlock(((CraftWorld) loc.getWorld()).getHandle(), x, y, z, net.minecraft.server.v1_8_R3.Block.getById(mat.getId()).fromLegacyData(0));
        entity.ticksLived = 1;
        ((CraftWorld) loc.getWorld()).addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        FallingBlock block = (FallingBlock) entity.getBukkitEntity();
        block.setDropItem(false);
        return block;
    }
}

