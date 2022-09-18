package net.atlas.SkyblockSandbox.island.islands.hub;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.builders.SBItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseArea {
    ShowcaseHandler handler;

    public Location loc1 = new Location(Bukkit.getWorlds().get(0), -28, 53, 45);
    public Location loc2 = new Location(Bukkit.getWorlds().get(0), -31, 53, 44);
    public Location loc3 = new Location(Bukkit.getWorlds().get(0), -33, 53, 41);
    public Location loc4 = new Location(Bukkit.getWorlds().get(0), -34, 53, 38);
    public Location loc5 = new Location(Bukkit.getWorlds().get(0), -33, 53, 35);
    public Location loc6 = new Location(Bukkit.getWorlds().get(0), -31, 53, 32);
    public Location loc7 = new Location(Bukkit.getWorlds().get(0), -28, 53, 31);
    public Location loc8 = new Location(Bukkit.getWorlds().get(0), -18, 53, 31);
    public Location loc9 = new Location(Bukkit.getWorlds().get(0), -15, 53, 32);
    public Location loc10 = new Location(Bukkit.getWorlds().get(0),-13, 53, 35);

    public List<Location> locList = new ArrayList<>();


    public ShowcaseArea () {
        handler = ShowcaseHandler.instance;
        locList.add(loc1);
        locList.add(loc2);
        locList.add(loc3);
        locList.add(loc4);
        locList.add(loc5);
        locList.add(loc6);
        locList.add(loc7);
        locList.add(loc8);
        locList.add(loc9);
        locList.add(loc10);
    }

    public void spawnArea(SBPlayer p) {
        for(Location loc:locList) {
            handler.addBlock(new ItemShowcaseBlock(SBItemBuilder.init()
                    .mat(Material.GOLD_BLOCK)
                    .name("Test").id("TEST")
                    .rarity(Rarity.LEGENDARY)
                    .type(ItemType.ITEM).build(),loc));
        }

        handler.spawnAllBlocks(p);


        new BukkitRunnable() {

            @Override
            public void run() {
                handler.updateBlocks(p);
            }
        }.runTaskTimer(SBX.getInstance(),200L,200L);
    }
}
