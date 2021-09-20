package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.customMining.MiningBlock;
import net.atlas.SkyblockSandbox.customMining.MiningBlocks;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

import static net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler.dist;

public class BlockBreak extends SkyblockListener<BlockBreakEvent> {

    public static HashMap<Location, Material> brokenBlocks = new HashMap<>();
    public static HashMap<Location,Byte> blockData = new HashMap<>();

    @EventHandler
    public void callEvent(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            if (event.getPlayer().getItemInHand().getType().name().contains("SWORD")) {
                event.setCancelled(true);
                return;
            }
            event.getBlock().breakNaturally();
            event.setCancelled(true);
        } else {
            SBPlayer pl = new SBPlayer(event.getPlayer());
            Location loc = event.getBlock().getLocation();
            Block block = event.getBlock();
            byte data = block.getData();
            Material mat = block.getType();
            event.setCancelled(true);
            if (pl.getItemInHand().getType().name().contains("SWORD")) {
                event.setCancelled(true);
                return;
            }
            if (pl.hasIsland()) {
                if (pl.getWorld() == pl.getPlayerIsland().getCenter().getWorld()) {
                    if (pl.getLocation().distance(pl.getPlayerIsland().getCenter()) > dist()) {
                        event.setCancelled(true);
                    } else {
                        event.setCancelled(event.getBlock().getType().equals(Material.BEDROCK));
                    }
                    return;
                }
            }

            if (event.getBlock().getType().name().contains("LOG")) {
                SkillEXPGainEvent expGainEvent = new SkillEXPGainEvent(pl, SkillType.FORAGING, 8.0D);
                Bukkit.getPluginManager().callEvent(expGainEvent);
                event.setCancelled(false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        loc.getBlock().setType(mat);
                        loc.getBlock().setData(data);
                    }
                }.runTaskLater(SBX.getInstance(), 20 * 10);
            } else {
                if(MiningBlocks.isMineable(event.getBlock())) {
                    MiningBlock mb = MiningBlocks.toMiningBlock(event.getBlock());
                    brokenBlocks.put(event.getBlock().getLocation(), event.getBlock().getType());
                    blockData.put(event.getBlock().getLocation(),event.getBlock().getData());
                    pl.playEffect(pl.getLocation(), Effect.STEP_SOUND, event.getBlock().getType().getId());
                    event.getBlock().setType(Material.BEDROCK);
                    SkillEXPGainEvent expGainEvent = new SkillEXPGainEvent(pl, mb.getSkillType(), mb.getXpAmt());
                    Bukkit.getPluginManager().callEvent(expGainEvent);

                    new BukkitRunnable() {
                        final Block b = event.getBlock();

                        @Override
                        public void run() {
                            if (brokenBlocks.get(b.getLocation()) != null) {
                                b.setType(brokenBlocks.get(b.getLocation()));
                                b.setData(blockData.get(b.getLocation()));
                                brokenBlocks.remove(b.getLocation());
                                blockData.remove(b.getLocation());
                            }
                        }
                    }.runTaskLater(SBX.getInstance(), 40L);
                }
            }
        }

    }
}
