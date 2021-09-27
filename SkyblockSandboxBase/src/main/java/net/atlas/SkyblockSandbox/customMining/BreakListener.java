package net.atlas.SkyblockSandbox.customMining;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class BreakListener implements Listener {
    private BukkitScheduler scheduler;
    public static HashMap<UUID, MineTask> isClicking = new HashMap<>();

    public BreakListener() {
        this.scheduler = SBX.getInstance().getServer().getScheduler();
    }

    @EventHandler
    public void blockBreak(BlockDamageEvent e) {
        //e.setCancelled(true);
        Player p = e.getPlayer();
        if (p.getItemInHand() != null && !p.getItemInHand().getType().equals(Material.AIR)) {
            String ID = NBTUtil.getString(p.getItemInHand(),"item-type");
            if(ID!=null) {
                if(ID.toUpperCase().contains("PICKAXE") || ID.toUpperCase().contains("DRILL") || p.getItemInHand().getType().name().contains("PICKAXE")) {
                    Block b = e.getBlock();
                    MineTask previous = isClicking.get(p.getUniqueId());
                    if(previous==null || !previous.getBlock().getLocation().equals(b.getLocation())) {
                        if(previous !=null) {
                            previous.cancel();
                        }

                        SBPlayer pl = new SBPlayer(p);
                        MiningBlock mb = MiningBlocks.toMiningBlock(b);

                        MineTask task = new MineTask(pl,b,mb);
                        int taskID = scheduler.scheduleSyncRepeatingTask(SBX.getInstance(),task,0L,0L);
                        task.setTaskID(taskID);
                        isClicking.put(p.getUniqueId(),task);
                    }
                }
            }
        }
    }

    private class MineTask implements Runnable {
        private int counter = 0;
        private int taskID;
        private final Player player;
        private final Block block;
        private double breakTime;
        private MiningBlock mb;
        int id = new Random().nextInt(2000);

        public MineTask(SBPlayer player, Block block, MiningBlock mb)
        {
            this.player = player;
            this.block = block;
            this.mb = mb;

            double miningTime = -1;
            if (mb.getBlockHP() != 0) {
                miningTime = ((mb.getBlockHP()*30)/player.getMaxStat(SBPlayer.PlayerStat.MINING_SPEED));
            }
            int softCap = (int) ((mb.getBlockHP()*20)/2.9999);
            if(softCap<player.getMaxStat(SBPlayer.PlayerStat.MINING_SPEED) && miningTime>1) {
                miningTime = 4;
            }
            if(miningTime<1&&miningTime!=-1) {
                miningTime = 1;
            }
            this.breakTime = miningTime;
            //player.sendMessage(String.valueOf(breakTime));
        }

        public void setTaskID(int taskID)
        {
            this.taskID = taskID;
        }

        public Block getBlock()
        {
            return block;
        }

        @Override
        public void run()
        {
            Block newTarget = player.getTargetBlock((Set<Material>) null, 5);
            // If the player isn't looking at the block cancel
            if (newTarget == null || !block.getLocation().equals(newTarget.getLocation()) || breakTime==-1)
            {
                sendBlockBreak(-1,id);
                cancel();
                return;
            }

            // Damage is a value 0 to 9 inclusive representing the 10 different damage textures that can be applied to a block
            int damage = (int)((counter / (float)breakTime) * 10);

            // Send the damage animation state once for each increment
            if (damage != (counter == 0 ? -1 : (int)((counter - 1) / (float)breakTime * 10)))
            {
                sendBlockBreak(damage,id);
            }
            counter++;
            if (counter >= breakTime)
            {
                sendBlockBreak(-1,id);
                Bukkit.getServer().getPluginManager().callEvent(new BlockBreakEvent(block,player));
                scheduler.cancelTask(taskID);
            }
        }
        private void sendBlockBreak(int damage,int id) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, new net.minecraft.server.v1_8_R3.BlockPosition(block.getX(), block.getY(), block.getZ()), damage);
            for(Entity b:block.getLocation().getWorld().getNearbyEntities(block.getLocation(),20,20,20)) {
                if(b instanceof Player) {
                    int dimension = ((CraftWorld) b.getWorld()).getHandle().dimension;
                   ((CraftServer) b.getServer()).getHandle().sendPacketNearby(block.getX(),block.getY(),block.getZ(),120,dimension, packet);
                }
            }
        }

        public void cancel()
        {
            isClicking.remove(player.getUniqueId());
            scheduler.cancelTask(taskID);
            sendBlockBreak(-1,id);
        }
    }

    public static void setClickCanceled(Player p) {
        MineTask prev = isClicking.get(p.getUniqueId());
        if(prev!=null) {
            prev.cancel();
        }
    }
}
