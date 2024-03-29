package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import me.clip.placeholderapi.PlaceholderAPI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.dragClasses.AbstractDragon;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.util.StandUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight.fightActive;
import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight.spawnLoc;


public class LootListener implements Listener {

    HashMap<Location, Material> storedBlocks = new HashMap<>();
    public static HashMap<String, ItemStack> lootDrops = new HashMap<>();
    public static HashMap<String, EntityItem> itemDropID = new HashMap<>();
    public static HashMap<String, EntityArmorStand> armorstandHoloID = new HashMap<>();
    public static Map<Player, Double> damage = new HashMap<>();
    public static Location lootLoc;
    static MongoCoins db;


    private static void strangeCircleStuff(ArrayList<Location> as) {
        HashMap<Location, Material> reset = new HashMap<Location, Material>();
        HashMap<Location, Byte> resetData = new HashMap<Location, Byte>();
        new BukkitRunnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {

                try {
                    for (Location armor : as) {
                        while (armor.clone().add(0,-1,0).getBlock().getType()==Material.AIR) {
                            armor.setY(armor.getY()-1);
                        }
                        Block b = armor.clone().add(0, -1, 0).getBlock();
                        if (b.getLocation().getY() >= 10) {
                            //distance formula lmao
                            double wtfisthis = Math.abs(Math.sqrt((spawnLoc.getX() - (b.getLocation().getX()) * (spawnLoc.getX() - (b.getLocation().getX()))) + (spawnLoc.getZ() - (b.getLocation().getZ())) * (spawnLoc.getZ() - (b.getLocation().getZ()))));
                            if (!(wtfisthis >= 70)) {
                                b.getLocation().setY(9);
                            }

                        }
                        reset.put(b.getLocation(), b.getType());
                        resetData.put(b.getLocation(), b.getData());
                        b.setType(Material.STAINED_CLAY);
                        b.setData((byte) 6);
                        as.remove(armor);
                        /*if (armor.isOnGround()) {
                            Block b = armor.getLocation().clone().add(0, -1, 0).getBlock();
                            if (b.getLocation().getY() >= 10) {
                                //distance formula lmao
                                double wtfisthis = Math.abs(Math.sqrt((spawnLoc.getX() - (b.getLocation().getX()) * (spawnLoc.getX() - (b.getLocation().getX()))) + (spawnLoc.getZ() - (b.getLocation().getZ())) * (spawnLoc.getZ() - (b.getLocation().getZ()))));
                                if (!(wtfisthis >= 70)) {
                                    b.getLocation().setY(9);
                                }

                            }
                            reset.put(b.getLocation(), b.getType());
                            resetData.put(b.getLocation(), b.getData());
                            b.setType(Material.STAINED_CLAY);
                            b.setData((byte) 6);
                            armor.remove();
                            as.remove(armor);
                        }*/
                    }
                } catch (ConcurrentModificationException ignored) {
                }
                if (as.isEmpty()) {
                    startBlockResetter(reset, resetData);
                    this.cancel();
                }
            }
        }.runTaskTimer(SBX.getInstance(), 0, 0);
    }

    protected static void startBlockResetter(HashMap<Location, Material> reset, HashMap<Location, Byte> resetData) {

        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), () -> {

            for (Location loc : reset.keySet()) {
                loc.getBlock().setType(reset.get(loc));
                loc.getBlock().setData(resetData.get(loc));
            }

        }, 20 * 30);
    }

    public static void dragonDownMessage(AbstractDragon dragon, Player killer, Location location) {
        damage = sortByValue(damage);
        String pattern = "###,###";
        DecimalFormat format = new DecimalFormat(pattern);
        for (Player p : Bukkit.getOnlinePlayers()) {


            List<Player> damagers = new ArrayList<>(damage.keySet());
            String damage1 = (format.format(damage.get(damagers.get(0))));
            String yourDamage = "";
            try {
                yourDamage = (format.format(damage.get(p)));
            } catch (IllegalArgumentException ignored) {
                yourDamage = "0";
            }

            p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
            p.sendMessage(ChatColor.GOLD + "              " + ChatColor.BOLD + ChatColor.stripColor(dragon.getName().toUpperCase()) + " DOWN!");
            p.sendMessage(ChatColor.GREEN + "                " + killer.getName() + ChatColor.GRAY + " dealt the final blow.");
            p.sendMessage(ChatColor.YELLOW + "          " + ChatColor.BOLD + "1st Damager" + ChatColor.GRAY + " - " + PlaceholderAPI.setPlaceholders(damagers.get(0), "%luckperms_prefix%") + damagers.get(0).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + damage1);
            if (damagers.size() < 3) {

                if (damagers.size() == 2) {
                    String damage2 = (format.format(damage.get(damagers.get(1))));
                    p.sendMessage(ChatColor.GOLD + "          " + ChatColor.BOLD + "2nd Damager" + ChatColor.GRAY + " - " + PlaceholderAPI.setPlaceholders(damagers.get(1), "%luckperms_prefix%") + damagers.get(1).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + damage2);
                } else {
                    p.sendMessage(ChatColor.GOLD + "          " + ChatColor.BOLD + "2nd Damager" + ChatColor.GRAY + " - N/A" + ChatColor.GRAY);
                }
                p.sendMessage(ChatColor.RED + "          " + ChatColor.BOLD + "3rd Damager" + ChatColor.GRAY + " - N/A");
            } else {
                String damage2 = (format.format(damage.get(damagers.get(1))));
                String damage3 = (format.format(damage.get(damagers.get(2))));
                p.sendMessage(ChatColor.GOLD + "          " + ChatColor.BOLD + "2nd Damager" + ChatColor.GRAY + " - " + PlaceholderAPI.setPlaceholders(damagers.get(1), "%luckperms_prefix%") + damagers.get(1).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + damage2);
                p.sendMessage(ChatColor.RED + "          " + ChatColor.BOLD + "3rd Damager" + ChatColor.GRAY + " - " + PlaceholderAPI.setPlaceholders(damagers.get(2), "%luckperms_prefix%") + damagers.get(2).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + damage3);
            }
            p.sendMessage(ChatColor.YELLOW + "          Your Damage: " + ChatColor.GREEN + yourDamage + ChatColor.GRAY + " (Position #" + (damagers.indexOf(p) + 1) + ")");
            p.sendMessage(ChatColor.YELLOW + "             Runecrafting Experience: " + ChatColor.LIGHT_PURPLE + "0");
            p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
        }
        HashMap<Player, Double> weightCopy = new HashMap<>(StartFight.weight);
        for (Player p : weightCopy.keySet()) {
            Double playerWeight = StartFight.weight.get(p);
            damage = sortByValue(damage);
            List<Player> damagers = new ArrayList<>(damage.keySet());
            int place = (damagers.indexOf(p) + 1);

            if (killer.getName().equals(p.getName())) {
                playerWeight += 50;
            }
            if (place >= 16) {
                playerWeight += 75;
            } else {
                if (place < 15 && place >= 11) {
                    playerWeight += 100;
                } else {
                    if (place < 10 && place >= 4) {
                        playerWeight += 125;
                    } else {
                        switch (place) {
                            case 1:
                                playerWeight += 300;
                            case 2:
                                playerWeight += 250;
                            case 3:
                                playerWeight += 200;
                        }
                    }
                }
            }
            StartFight.weight.remove(p);
            StartFight.weight.put(p, playerWeight);
            System.out.println(StartFight.weight.get(p));
            ArmorStand lootAs = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            lootAs.setVisible(false);
            Double finalPlayerWeight = playerWeight;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (lootAs.isOnGround()) {
                        lootLoc = lootAs.getLocation().clone().subtract(0, 1, 0);
                        p.sendMessage(String.valueOf(finalPlayerWeight));
                        calculateDrop(p, lootLoc, finalPlayerWeight);
                        damage.remove(p);
                        StartFight.weight.remove(p);
                        StartFight.aotdChance.remove(p);
                        StartFight.playerDMG.remove(p);
                        fightActive = false;
                        lootAs.remove();
                        this.cancel();
                    }
                }
            }.runTaskTimer(SBX.getInstance(), 0, 0);
        }
        /*ArrayList<ArmorStand> as = new ArrayList<>();
        for (Location loc : StandUtils.generateSphere(location, 6, false)) {
            ArmorStand uff = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
            uff.setVisible(false);
            uff.setMarker(false);
            uff.setMaxHealth(1000);
            uff.setHealth(1000);
            uff.setGravity(true);
            as.add(uff);
        }*/
        strangeCircleStuff(new ArrayList<>(StandUtils.generateSphere(location, 6, false)));
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    static void spawnLoot(Player player, Location location, ItemStack item) {
        //clientside loot
        player.sendMessage(String.valueOf(item.getItemMeta().getDisplayName()));
        EntityItem entityItem = new EntityItem(((CraftWorld) player.getWorld()).getHandle());
        entityItem.setItemStack(CraftItemStack.asNMSCopy(item));
        entityItem.setPosition(location.getX() + 0.5, location.getY() + 1, location.getZ() + 0.5);

        CraftPlayer entityPlayer = (CraftPlayer) player;
        PacketPlayOutSpawnEntity itemPacket = new PacketPlayOutSpawnEntity(entityItem, 2, 1);
        (entityPlayer.getHandle()).playerConnection.sendPacket(itemPacket);

        PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(entityItem.getId(), entityItem.getDataWatcher(), true);
        (entityPlayer.getHandle()).playerConnection.sendPacket(meta);


        //clientside stand
        EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) player.getWorld()).getHandle());
        entityArmorStand.setCustomName(item.getItemMeta().getDisplayName());
        entityArmorStand.setCustomNameVisible(true);
        entityArmorStand.setPosition(location.getX() + 0.5, location.getY() + 1, location.getZ() + 0.5);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setPosition(entityArmorStand.locX, entityArmorStand.locY - 1, entityArmorStand.locZ);


        PacketPlayOutSpawnEntityLiving standPacket = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
        (entityPlayer.getHandle()).playerConnection.sendPacket(standPacket);

        //detector stand
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setVisible(false);
        as.setGravity(true);
        Location asLoc = new Location(location.getWorld(), entityItem.locX, entityItem.locY, entityItem.locZ);
        as.teleport(asLoc.subtract(0, 1, 0));


        net.minecraft.server.v1_8_R3.ItemStack nmsItem1 = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag1 = (nmsItem1.hasTag()) ? nmsItem1.getTag() : new NBTTagCompound();
        NBTTagCompound data1 = tag1.getCompound("ExtraAttributes");
        String uuid = data1.getString("UUID");
        lootDrops.put(uuid, item);
        itemDropID.put(uuid, entityItem);
        armorstandHoloID.put(uuid, entityArmorStand);
        as.setMetadata(player.getName() + "_drag_loot", new FixedMetadataValue(SBX.getInstance(), uuid));
    }

    static void calculateDrop(Player p, Location spawnLoc, double playerWeight) {
        Location loc = spawnLoc.clone();

        ItemStack drop = null;

        String[] splitDrag = StartFight.activeDrag.getName().split(" ");
        String dragID = ChatColor.stripColor(splitDrag[0]);

        double random = Math.random() * 100;
        double petChance = Math.random() - (StartFight.aotdChance.get(p) / 1000000);
        double aotdChance = StartFight.aotdChance.get(p) + (Math.random() * 100);
        p.sendMessage(String.valueOf(aotdChance));
        try {
            if (playerWeight >= 450) {
                if (petChance < 0.0008) {
                    //if(petChance>0.0001) {
                    drop = DragonDrop.Universal.PET.getDrop(Rarity.EPIC);
                    double legChance = Math.random();
                    if (legChance <= 0.2) {
                        drop = DragonDrop.Universal.PET.getDrop(Rarity.LEGENDARY);
                    }
                }
            }
            if (playerWeight >= 450 && !StartFight.activeDrag.getBukkitEntity().hasMetadata(DragonTypes.SUPERIOR.getMobName()) && aotdChance > 140 && drop == null) {
                drop = DragonDrop.Universal.SWORD.getDrop();
                playerWeight -= 450;
            }
            if (drop == null && playerWeight >= 400) {
                if (random > 65) {
                    drop = DragonDrop.CHESTPLATE.getDrop(DragonTypes.valueOf(dragID.toUpperCase()));
                    playerWeight -= 400;
                }
            }
            if (drop == null && playerWeight >= 350) {
                if (random > 55) {
                    drop = DragonDrop.LEGGINGS.getDrop(DragonTypes.valueOf(dragID.toUpperCase()));
                    playerWeight -= 350;
                }
            }
            if (drop == null && playerWeight >= 325) {
                if (random > 40) {
                    drop = DragonDrop.HELMET.getDrop(DragonTypes.valueOf(dragID.toUpperCase()));
                    playerWeight -= 325;
                }
            }
            if (drop == null && playerWeight >= 300) {
                if (random > 30) {
                    drop = DragonDrop.BOOTS.getDrop(DragonTypes.valueOf(dragID.toUpperCase()));
                    playerWeight -= 300;
                }
            }
            //spawning drop
            if (drop != null) {
                drop = new SBItemStack(drop).refreshLore();
                spawnLoot(p, loc, drop);
            }
        } catch (Exception ex) {
            System.out.println("[SkyblockSandboxBase] Dragon loot could not be dropped at: ");
            ex.printStackTrace();
            p.sendMessage(ChatColor.RED + "Something went wrong while dropping loot! Please contact a staff member if this issue persists.");
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        for (Entity en : p.getNearbyEntities(0.2, 0.2, 0.2)) {
            if (en.hasMetadata(p.getName() + "_drag_loot")) {
                en.remove();
                p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 1);

                try {
                    String uuid = "";
                    for (MetadataValue value : en.getMetadata(p.getName() + "_drag_loot")) {
                        uuid = value.asString();
                    }
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " has obtained " + lootDrops.get(uuid).getItemMeta().getDisplayName() + ChatColor.YELLOW + "!");
                    }
                    p.getInventory().addItem(lootDrops.get(uuid));


                    //deleting clientside item
                    CraftPlayer entityPlayer = (CraftPlayer) p;
                    PacketPlayOutEntityDestroy itemPacket = new PacketPlayOutEntityDestroy(itemDropID.get(uuid).getId());
                    (entityPlayer.getHandle()).playerConnection.sendPacket(itemPacket);

                    //and clientside stand
                    PacketPlayOutEntityDestroy standPacket = new PacketPlayOutEntityDestroy(armorstandHoloID.get(uuid).getId());
                    (entityPlayer.getHandle()).playerConnection.sendPacket(standPacket);
                    lootDrops.remove(uuid);
                    armorstandHoloID.remove(uuid);
                } catch (NullPointerException ignored) {

                }

            }
        }
    }

}
