package net.atlas.SkyblockSandbox.abilityCreator;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class AbilityUtil {

    public static int getAbilityAmount(ItemStack stack) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");

                return abils.c().size();
            }
        }
        return 0;
    }

    public static ItemStack setGenericAbilityString(ItemStack stack,String key,String value) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");

                abils.setString(key, value);

                data.set("Abilities", abils);
                tag.set("ExtraAttributes", data);

                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static String getGenericAbilityString(ItemStack stack, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                return abils.getString(key);
            }
        }
        return "";
    }

    public static ItemStack setAbilityData(ItemStack stack,int index, AbilityValue value, Object data) {
        stack = setAbilityString(stack,index, value.name(), String.valueOf(data));
        return stack;
    }

    public static String getAbilityData(ItemStack stack,int index, AbilityValue value) {
        return getAbilityString(stack,index, value.name());
    }

    public static ItemStack setAbilityString(ItemStack stack, int index, String key, String value) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);

                ability.setString(key, value);

                abils.set("Ability_" + index, ability);
                data.set("Abilities", abils);
                tag.set("ExtraAttributes", data);

                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static String getAbilityString(ItemStack stack,int index, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);
                return ability.getString(key);
            }
        }
        return "";
    }

    public static ItemStack removeAbility(ItemStack stack,int index) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);

                abils.remove("Ability_" + index);
                data.set("Abilities", abils);
                tag.set("ExtraAttributes", data);

                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static ArrayList<String> getAbilityDescription(ItemStack stack, int index) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);
                NBTTagCompound description = ability.getCompound("description");
                if (description == null) {
                    description = new NBTTagCompound();
                }
                ArrayList<String> desc = new ArrayList<>();
                for (String key : description.c()) {
                    desc.add(description.getString(key));
                }

                return desc;
            }
        }
        return null;
    }

    public static ItemStack setAbilityDescription(ItemStack stack, int aIndex, int index, String line) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + aIndex);
                NBTTagCompound description = ability.getCompound("description");
                if (description == null) {
                    description = new NBTTagCompound();
                }

                description.setString(String.valueOf(index), line);

                ability.set("description", description);
                abils.set("Ability_" + aIndex, ability);
                data.set("Abilities", abils);
                tag.set("ExtraAttributes", data);

                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

}
