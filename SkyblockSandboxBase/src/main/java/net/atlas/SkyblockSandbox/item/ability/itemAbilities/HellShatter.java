package net.atlas.SkyblockSandbox.item.ability.itemAbilities;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.entity.customEntity.AnimBlock;
import net.atlas.SkyblockSandbox.entity.customEntity.FissureZombie;
import net.atlas.SkyblockSandbox.entity.customEntity.PlaceHolderSlime;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityWolf;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.*;

public class HellShatter extends Ability {
    HashMap<UUID,Integer> mobCount = new HashMap<>();
    @Override
    public String getAbilityName() {
        return "Hell Shatter";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public double getManaCost() {
        return 66;
    }

    @Override
    public List<String> getAbilDescription() {
        return SUtil.colorize("&7Call undead &4minions &7from the depths", "&7of hell to aid you in battle!", "&7For every &b166 mana &7you gain", "&7one gauranteed &4minion.","","&8Current Minion Range: &33-&37");
    }

    @Override
    public void leftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void rightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {
        if(!mobCount.containsKey(p.getUniqueId())) {
            mobCount.put(p.getUniqueId(), 0);
        }
        SBPlayer pl = new SBPlayer(p);
        SBItemStack sbitem = new SBItemStack(item);
        Location blockLoc = pl.getLocation();
        while (blockLoc.getBlock().getType()==Material.AIR) {
            blockLoc.setY(blockLoc.getY()-1);
            if(blockLoc.getY()<=0) {
                p.sendMessage(SUtil.colorize("&cYou must be above a solid block!"));
                return;
            }
        }
        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.6f);
        cylinder(blockLoc.clone().subtract(0,1,0),Material.OBSIDIAN,5,p);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                if(i>=4) {
                    mobCount.put(p.getUniqueId(),0);
                    this.cancel();
                }
                cylinder(blockLoc.clone().subtract(0,1,0),Material.REDSTONE_BLOCK,i,p);
                p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 0.5f, 1);
            }
        }.runTaskTimer(SBX.getInstance(),4L,2L);

    }

    public void cylinder(Location loc, Material mat, int r,Player p) {
        SBPlayer pl = new SBPlayer(p);
        List<Slime> blist = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    FallingBlock bl = AnimBlock.spawn(new Location(w, x, cy, z),mat);
                    Slime empty = PlaceHolderSlime.spawn(new Location(w,x,cy,z));
                    empty.setPassenger(bl);
                    /*ArmorStand as = (ArmorStand) w.spawnEntity(new Location(w, x, cy, z), EntityType.ARMOR_STAND);
                    as.setVisible(false);
                    as.setMarker(true);
                    as.setGravity(false);
                    as.setHelmet(new ItemStack(Material.OBSIDIAN));*/
                    //asList.add(as);
                    blist.add(empty);
                    Random random = new Random();
                    if(random.nextDouble()<0.03) {
                        if(mobCount.get(p.getUniqueId())>=6) {
                            return;
                        }
                        int finalX = x;
                        int finalZ = z;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                mobCount.put(p.getUniqueId(),mobCount.get(p.getUniqueId())+1);
                                switch (random.nextInt(2)+1) {
                                    case 1:
                                        Zombie z = FissureZombie.spawnSummon(new Location(w,finalX,cy,finalZ).add(0,2,0),pl.getMaxStat(SBPlayer.PlayerStat.HEALTH)*0.75,pl);
                                        break;
                                    case 2:
                                        w.spawnEntity(new Location(w, finalX,cy, finalZ).add(0,2,0),EntityType.SKELETON);
                                        break;
                                    case 3:
                                        w.spawnEntity(new Location(w, finalX,cy, finalZ).add(0,2,0),EntityType.MAGMA_CUBE);
                                        break;
                                }
                            }
                        }.runTaskLater(SBX.getInstance(),12L);

                    }
                }
            }
        }
        new BukkitRunnable() {
            boolean goDown;
            int i = 0;
            @Override
            public void run() {
                i++;
                for(Slime as:new ArrayList<>(blist)) {
                    if(!goDown) {
                        as.setVelocity(new Vector(0,0,0));
                        as.teleport(as.getLocation().add(0,0.1,0));
                        ((CraftEntity)as).getHandle().setLocation(as.getLocation().getX(), as.getLocation().getY()+0.1, as.getLocation().getZ(), as.getLocation().getYaw(), as.getLocation().getPitch());
                        if(i>10) {
                            goDown = true;
                        }
                    } else {
                        ((CraftEntity)as).getHandle().setLocation(as.getLocation().getX(), as.getLocation().getY()-0.1, as.getLocation().getZ(), as.getLocation().getYaw(), as.getLocation().getPitch());
                    }
                }
                if(goDown) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Slime as:new ArrayList<>(blist)) {
                                as.getPassenger().remove();
                                as.remove();
                                blist.remove(as);
                            }
                        }
                    }.runTaskLater(SBX.getInstance(),12L);
                }
                if(blist.size()==0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(SBX.getInstance(),0L,1L);
    }

    @Override
    public void rightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftLeftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void shiftLeftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftRightClickAirAction(Player p, PlayerInteractEvent event, ItemStack item) {

    }

    @Override
    public void shiftRightClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void hitEntityAction(Player p, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item) {

    }

    @Override
    public void breakBlockAction(Player p, BlockBreakEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void clickedInInventoryAction(Player p, InventoryClickEvent event) {

    }

    @Override
    public void shiftEvent(Player p, PlayerToggleSneakEvent e) {

    }

    @Override
    public void rightClickEntityEvent(Player p, PlayerInteractAtEntityEvent e) {

    }

    @Override
    public void playerFishAction(Player p, PlayerFishEvent event, ItemStack item) {

    }

    @Override
    public void playerShootAction(Player p, EntityShootBowEvent event, ItemStack item) {

    }
}
