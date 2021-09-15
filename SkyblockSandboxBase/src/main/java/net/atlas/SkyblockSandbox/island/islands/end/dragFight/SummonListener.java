package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import me.clip.placeholderapi.PlaceholderAPI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SummonListener {
    private static final HashMap<String, Location> eyes = new HashMap<>();
    public static void placeEye(SummoningAltaar loc,Player p) {
        if(loc.getLoc().getBlock().getType().equals(Material.ENDER_PORTAL_FRAME)) {
            Block block = loc.getLoc().getBlock();
            if(block.getData() != (byte) 4) {
                if(!StartFight.fightActive) {
                    net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(p.getItemInHand());
                    NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                    NBTTagCompound data = tag.getCompound("ExtraAttributes");
                    String uuid = data.getString("UUID");
                    eyes.put(uuid,loc.getLoc());
                    block.setData((byte) 4);
                    StartFight.putEye(loc.getLoc(),p);
                    p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT,1,1);
                    boolean userems = SBX.getInstance().getDragonData().getBoolean("data.use-rems." + p.getUniqueId());
                    boolean infiniteEyes = SBX.getInstance().getDragonData().getBoolean("data.infinite-eyes." + p.getUniqueId());
                    if(!infiniteEyes) {
                        if(userems) {
                            SBPlayer pl = new SBPlayer(p);
                            pl.setItemInHand(SkyblockItem.Default.SLEEPING_EYE);
                            net.minecraft.server.v1_8_R3.ItemStack nmsItem1 = CraftItemStack.asNMSCopy(p.getItemInHand());
                            NBTTagCompound tag1 = (nmsItem1.hasTag()) ? nmsItem1.getTag() : new NBTTagCompound();
                            NBTTagCompound data1 = tag1.getCompound("ExtraAttributes");
                            data1.setString("UUID",uuid);
                            tag1.set("ExtraAttributes", data1);
                            nmsItem1.setTag(tag1);
                            ItemStack i = CraftItemStack.asBukkitCopy(nmsItem1);
                            p.setItemInHand(i);
                        } else {
                            if(p.getItemInHand().getAmount()>1) {
                                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                            } else {
                                p.setItemInHand(new ItemStack(Material.AIR));
                            }
                        }
                    }
                    for(Player pl: Bukkit.getOnlinePlayers()) {
                        if(p.equals(pl)) {
                            if(StartFight.placedEyes.size()==0) {
                                pl.sendMessage(ChatColor.LIGHT_PURPLE + "You placed a Summoning Eye! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + "8" + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                            } else {
                                pl.sendMessage(ChatColor.LIGHT_PURPLE + "You placed a Summoning Eye! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + StartFight.placedEyes.size() + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                            }
                        } else {
                            if (StartFight.placedEyes.size() == 0) {
                                pl.sendMessage( PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%") + p.getName() + ChatColor.LIGHT_PURPLE + " placed a Summoning Eye! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + "8" + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                            } else {
                                pl.sendMessage( PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%") + p.getName() + ChatColor.LIGHT_PURPLE + " placed a Summoning Eye! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + StartFight.placedEyes.size() + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                            }
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.GREEN + "There is already a fight active!");
                    p.playSound(p.getLocation(),Sound.ENDERMAN_TELEPORT,1,0);
                }

            }
        }
    }

    public static void removeEye(SummoningAltaar loc, Player p) {
        if(loc.getLoc().getBlock().getType().equals(Material.ENDER_PORTAL_FRAME)) {
            Block block = loc.getLoc().getBlock();
            if(block.getData() == (byte) 4) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(p.getItemInHand());
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                String uuid = data.getString("UUID");
                if(eyes.containsKey(uuid)) {
                    if(block.getLocation().equals(eyes.get(uuid))) {
                        block.setData((byte) 0);
                        StartFight.placedEyes.remove(loc.getLoc());
                        p.sendMessage(ChatColor.DARK_PURPLE + "You removed a summoning eye!");
                        SBPlayer pl = new SBPlayer(p);
                        pl.setItemInHand(SkyblockItem.Default.SUMMONING_EYE);
                        eyes.remove(uuid);
                    }
                }

            }
        }
    }
}
