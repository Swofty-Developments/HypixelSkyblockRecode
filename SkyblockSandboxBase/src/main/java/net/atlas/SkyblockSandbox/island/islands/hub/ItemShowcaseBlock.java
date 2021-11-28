package net.atlas.SkyblockSandbox.island.islands.hub;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class ItemShowcaseBlock {

    private final Location displayLoc;
    private ArmorStand glassStand;
    private EntityItem item;
    private SBItemStack sbItem;
    private int cachedVotes;

    public ItemShowcaseBlock(SBItemStack item,Location loc) {
        this.displayLoc = loc;
        this.sbItem = item;
    }

    public SBItemStack getSbItem() {
        return sbItem;
    }

    public void upvote() {
        cachedVotes++;
    }

    public void downvote() {
        cachedVotes--;
    }

    public int getVotes() {
        return cachedVotes;
    }

    public void updateItem(SBPlayer p, SBItemStack item) {
        despawnItem(p);
        spawnItem(p,item);
    }

    public void despawn(SBPlayer p) {
        this.glassStand.remove();
        despawnItem(p);
    }

    public void despawnItem(SBPlayer p) {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(this.item.getId());
        ((CraftPlayer)p.getPlayer()).getHandle().playerConnection.sendPacket(destroy);
    }

    public void spawnItem(SBPlayer p,SBItemStack item) {
        this.sbItem = item;
        this.item = new EntityItem(((CraftWorld) displayLoc.getWorld()).getHandle());
        this.item.setItemStack(CraftItemStack.asNMSCopy(this.sbItem.asBukkitItem()));
        this.item.setPosition(displayLoc.getX() + 0.5, displayLoc.getY() + 1, displayLoc.getZ() + 0.5);

        CraftPlayer entityPlayer = (CraftPlayer) p.getPlayer();
        PacketPlayOutSpawnEntity itemPacket = new PacketPlayOutSpawnEntity(this.item, 2, 1);
        (entityPlayer.getHandle()).playerConnection.sendPacket(itemPacket);

        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(this.item.getId(), this.item.getDataWatcher(), true);
        (entityPlayer.getHandle()).playerConnection.sendPacket(meta);
    }


    public void spawn(SBPlayer p) {
        Player player = p.getPlayer();

        //glass display component
        this.glassStand = (ArmorStand) displayLoc.getWorld().spawnEntity(displayLoc.clone().add(0.5,-0.25,0.5), EntityType.ARMOR_STAND);
        this.glassStand.setVisible(false);
        this.glassStand.setGravity(false);
        this.glassStand.setHelmet(new ItemStack(Material.GLASS,1));
        this.glassStand.setMetadata("showcase",new FixedMetadataValue(SBX.getInstance(), ShowcaseHandler.instance.getIndexOf(this)));

        //setting NMS item of the itemstack for display
        this.item = new EntityItem(((CraftWorld) displayLoc.getWorld()).getHandle());
        this.item.setItemStack(CraftItemStack.asNMSCopy(this.sbItem.asBukkitItem()));
        this.item.setPosition(displayLoc.getX() + 0.5, displayLoc.getY() + 2, displayLoc.getZ() + 0.5);

        CraftPlayer entityPlayer = (CraftPlayer) player;
        PacketPlayOutSpawnEntity itemPacket = new PacketPlayOutSpawnEntity(this.item, 2, 1);
        (entityPlayer.getHandle()).playerConnection.sendPacket(itemPacket);

        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(this.item.getId(), this.item.getDataWatcher(), true);
        (entityPlayer.getHandle()).playerConnection.sendPacket(meta);

    }

}
