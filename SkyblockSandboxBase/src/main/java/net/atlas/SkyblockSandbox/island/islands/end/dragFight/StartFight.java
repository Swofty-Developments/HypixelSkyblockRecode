package net.atlas.SkyblockSandbox.island.islands.end.dragFight;


import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class StartFight {
    public static boolean fightActive = false;
    public static HashMap<Player, Double> playerDMG = new HashMap<>();
    public static Location spawnLoc = new Location(Bukkit.getWorlds().get(0), -671, 69, -277);
    public static HashMap<Location, Player> placedEyes = new HashMap<>();
    public static AbstractDragon activeDrag;
    public static HashMap<Player, Double> weight = new HashMap<>();
    public static HashMap<Player, Double> aotdChance = new HashMap<>();
    public static double maxDragHealth;
    public static double dragonHealth;
    public static ArmorStand as;

    public static void putEye(Location loc, Player p) {
        placedEyes.put(loc, p);
        if (placedEyes.size() == 8) {
            playerDMG.clear();
            for (Player pl : Bukkit.getOnlinePlayers()) {
                playerDMG.put(pl, 0D);
            }
            startDragonFight();
        }
    }

    public static void startDragonFight() {
        HashMap<Location, Player> placedEyesClone = new HashMap<>(placedEyes);
        for (Location loc : placedEyesClone.keySet()) {
            Player p = placedEyes.get(loc);
            placedEyes.remove(loc);
            if (aotdChance.containsKey(p)) {
                aotdChance.put(p, aotdChance.get(p) + 10D);
            } else {
                aotdChance.put(p, 10D);
            }
            if (weight.containsKey(p)) {
                if (weight.get(p) < 400) {
                    weight.put(p, weight.get(p) + 100);

                }
            } else {
                weight.put(p, 100D);
            }

        }
        System.out.println(weight);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 5, 1);
            playerDMG.put(p,0D);
        }
        fightActive = true;

        new BukkitRunnable() {
            AbstractDragon dragon = null;

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    new BukkitRunnable() {
                        int i = 0;

                        @Override
                        public void run() {
                            if (i == 3) {
                                this.cancel();
                            }
                            i++;
                            p.playSound(p.getLocation(), Sound.EXPLODE, 2, 1);
                        }
                    }.runTaskTimer(SBX.getInstance(), 0, 3);


                }


                int dragType = (int) (Math.random() * 100);
                for (SummoningAltaar value : SummoningAltaar.values()) {
                    value.getLoc().getBlock().setData((byte) 0);
                }
                if (dragType >= 96) {
                    dragon = (SuperiorDragon) DragonTypes.SUPERIOR.spawnEntity(spawnLoc);
                } else if (dragType >= 80) {
                    dragon = (StrongDragon) DragonTypes.STRONG.spawnEntity(spawnLoc);
                } else if (dragType >= 64) {
                    dragon = (WiseDragon) DragonTypes.WISE.spawnEntity(spawnLoc);
                } else if (dragType >= 48) {
                    dragon = (YoungDragon) DragonTypes.YOUNG.spawnEntity(spawnLoc);

                } else if (dragType >= 32) {
                    double random = Math.random() * 100;
                    if (random > 60) {
                        dragon = (UnstableDragon) DragonTypes.UNSTABLE.spawnEntity(spawnLoc);
                            /*new BukkitRunnable() {
                                @Override
                                public void run() {
                                    {
                                        if(!(activeDrag.isAlive())) {
                                            this.cancel();
                                        }
                                        PathFind.randomizeMovement(dragon);
                                    }
                                }
                            }.runTaskTimer(Items.getInstance(),50L,150L);*/
                    } else {
                        dragon = (UnstableDragon) DragonTypes.UNSTABLE.spawnEntity(spawnLoc);
                    }

                } else if (dragType >= 16) {
                    dragon = (ProtectorDragon) DragonTypes.PROTECTOR.spawnEntity(spawnLoc);
                } else if (dragType >= 0) {
                    dragon = (OldDragon) DragonTypes.OLD.spawnEntity(spawnLoc);

                }
                activeDrag = dragon;
                //PathFind.goToLocation(activeDrag, as);

            }

        }.runTaskLater(SBX.getInstance(), 20 * 10);


    }
}
