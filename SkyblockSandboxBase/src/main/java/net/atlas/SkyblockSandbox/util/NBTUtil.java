package net.atlas.SkyblockSandbox.util;

import net.atlas.SkyblockSandbox.island.islands.FairySouls;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class NBTUtil {
    public static HashMap<SBPlayer.PlayerStat, Double> getAllStats(SBPlayer p) {
        HashMap<SBPlayer.PlayerStat, Double> statMap = new HashMap<>();
        for (SBPlayer.PlayerStat s : SBPlayer.PlayerStat.values()) {
            double tempStat = s.getBase();
            for (ItemStack i : p.getInventory().getArmorContents()) {
                if (i != null && i.hasItemMeta()) {
                    SBItemBuilder item = new SBItemBuilder(i);
                    if (getString(i, "non-legacy").equals("true")) {
                        if(item.stats.get(s) != null) {
                            tempStat += item.stats.get(s);
                        }
                    } else {
                        tempStat += new SBItemStack(i).getStat(s);
                    }
                }
            }
            if (!(p.getItemInHand().getType().equals(Material.AIR) || p.getItemInHand() == null)) {
                SBItemBuilder item = new SBItemBuilder(p.getItemInHand());
                if (getString(p.getItemInHand(), "non-legacy").equals("true")) {
                    if (item.stats.get(s) != null) {
                        tempStat += item.stats.get(s);
                    }
                } else {
                    tempStat += new SBItemStack(p.getItemInHand()).getStat(s);
                }
                HashMap<SBPlayer.PlayerStat, Double> fairyMap = FairySouls.getPlayerRewards(p);
                if (fairyMap.containsKey(s)) {
                    tempStat += fairyMap.get(s);
                }
            }
            statMap.put(s, tempStat);
        }
        return statMap;
    }


    public static String getString(ItemStack item, String key) {
        if (item != null) {
            if (item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                return data.getString(key);
            }
        }
        return "";
    }

    public static ItemStack removeTag(ItemStack item, String key) {
        if (item != null) {
            if (item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                tag.remove(key);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return item;
    }


    public static ItemStack setGenericString(ItemStack item, String key, String value) {
        if (item != null) {
            if (item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                tag.setString(key, value);
                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return item;
    }

    public static String getGenericString(ItemStack item, String key) {
        if (item != null) {
            if (item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

                return tag.getString(key);
            }
        }
        return "";
    }

    public static List<String> getPetPerkDescription(ItemStack host, int index) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound perk = data.getCompound("pet-perks");
            NBTTagCompound perkData = perk.getCompound("perk_" + index);
            if(perkData!=null) {
                NBTTagCompound description = perkData.getCompound("description");
                Set<String> desc = description.c();
                ArrayList<String> descList = new ArrayList<>();
                for (int i = 0; i < desc.size(); i++) {
                    descList.add(description.getString(String.valueOf(i)));
                }
                return descList;
            }
        }
        return null;
    }

    public static String getPerkName(ItemStack host, int index) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound perk = data.getCompound("pet-perks");
            NBTTagCompound perkData = perk.getCompound("perk_" + index);
            if(perkData!=null) {
                String name = perkData.getString("name");
                return name;
            }
        }
        return null;
    }

    public static int getPerkAmt(ItemStack host) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound perk = data.getCompound("pet-perks");
            return perk.c().size();

        }
        return 0;
    }

    public static Integer getInteger(ItemStack item, String key) {
        if (item != null) {
            if (item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                return data.getInt(key);
            }
        }
        return 0;
    }

    public static ItemStack setInteger(ItemStack stack, int v, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                data.setInt(key, v);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);

            }
        }
        return null;
    }

    public static ItemStack setStat(ItemStack stack, double value, SBPlayer.PlayerStat stat) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();
                NBTTagCompound stats = data.getCompound("Stats");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }

                stats.setDouble(stat.name(), value);
                data.set("Stats",stats);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static Double getStat(ItemStack stack, SBPlayer.PlayerStat key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound stats = data.getCompound("Stats");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }

                return stats.get(key.name()) != null ? stats.getDouble(key.name()) : null;
            }
        }
        return null;
    }

    public static ItemStack setDescription(ItemStack stack, int index, String line) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();
                NBTTagCompound stats = data.getCompound("Description");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }

                stats.setString(String.valueOf(index), line);
                data.set("Description",stats);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static String getDescription(ItemStack stack, int index) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound stats = data.getCompound("Description");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }

                return stats.get(String.valueOf(index)) != null ? stats.getString(String.valueOf(index)) : null;
            }
        }
        return null;
    }

    public static List<String> getDescription(ItemStack stack) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound stats = data.getCompound("Description");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }
                List<String> list = new ArrayList<>();
                for(String key : stats.c()) {
                    list.add(stats.getString(key));
                }
                return list;
            }
        }
        return null;
    }

    public static ItemStack setEnchant(ItemStack stack, Enchantment enchant, int value) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();
                NBTTagCompound stats = data.getCompound("Enchants");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }

                stats.setInt(enchant.name(), value);
                data.set("Enchants",stats);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static HashMap<Enchantment, Integer> getEnchants(ItemStack stack) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound stats = data.getCompound("Enchants");
                if (stats == null) {
                    stats = new NBTTagCompound();
                }
                HashMap<Enchantment, Integer> list = new HashMap<>();
                for(String key : stats.c()) {
                    list.put(Enchantment.valueOf(key), stats.getInt(key));
                }
                return list;
            }
        }
        return null;
    }

    public static ItemStack setString(ItemStack stack, String msg, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                data.setString(key, msg);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static ItemStack setSignature(ItemStack stack, String msg, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound signatures = data.getCompound("Signatures");
                if (signatures == null) {
                    signatures = new NBTTagCompound();
                }

                signatures.setString(key, msg);
                data.set("Signatures",signatures);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static String getSignature(ItemStack stack,String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound signatures = data.getCompound("Signatures");
                if (signatures == null) {
                    signatures = new NBTTagCompound();
                }

                return signatures.getString(key);
            }
        }
        return "";
    }
    public static List<String> getAllSignatures(ItemStack stack) {
        List<String> signaturesList = new ArrayList<>();
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound signatures = data.getCompound("Signatures");
                if (signatures == null) {
                    signatures = new NBTTagCompound();
                }
                signaturesList = new ArrayList<>(signatures.c());

            }
        }
        return signaturesList;
    }


    public static ItemStack setBytes(ItemStack stack, byte[] bytes, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                data.setByteArray(key, bytes);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static byte[] getBytes(ItemStack stack,String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                return data.getByteArray(key);
            }
        }
        return null;
    }
}
