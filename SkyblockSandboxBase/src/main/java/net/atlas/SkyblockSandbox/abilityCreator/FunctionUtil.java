package net.atlas.SkyblockSandbox.abilityCreator;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class FunctionUtil {

    public static int getFunctionAmount(ItemStack stack,int abilIndex) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + abilIndex);

                //can't use .c().size(); due to other attributes being stored here.

                int amt = 0;
                for(int i=0;i<3;i++) {
                    if(!ability.getCompound("Function_" + i).toString().equals("{}")) {
                        amt++;
                    }
                }
                return amt;
            }
        }
        return 0;
    }

    public static ItemStack setFunctionData(ItemStack stack,int index,int functionIndex, Enum<? extends Function.dataValues> value, Object data) {
        stack = setFunctionString(stack,index,functionIndex, value.name(), String.valueOf(data));
        return stack;
    }

    public static String getFunctionData(ItemStack stack, int index, int functionIndex, Enum<? extends Function.dataValues> value) {
        return getFunctionString(stack,index,functionIndex, value.name());
    }

    public static ItemStack setFunctionString(ItemStack stack, int index, int functionIndex, String key, String value) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);
                NBTTagCompound function = ability.getCompound("Function_" + functionIndex);

                function.setString(key, value);

                ability.set("Function_" + functionIndex,function);
                abils.set("Ability_" + index, ability);
                data.set("Abilities", abils);
                tag.set("ExtraAttributes", data);

                nmsItem.setTag(tag);

                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public static String getFunctionString(ItemStack stack, int index, int functionIndex, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound abils = data.getCompound("Abilities");
                NBTTagCompound ability = abils.getCompound("Ability_" + index);
                NBTTagCompound function = ability.getCompound("Function_" + functionIndex);
                return function.getString(key);
            }
        }
        return "";
    }

}
