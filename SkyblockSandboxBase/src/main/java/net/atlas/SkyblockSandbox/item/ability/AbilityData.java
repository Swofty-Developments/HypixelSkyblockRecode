package net.atlas.SkyblockSandbox.item.ability;

import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.Particles;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AbilityData {

    public static ItemStack setData(EnumAbilityData dataType, ItemStack item, Object data, int index) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        abilitySlot.setString(dataType.getA(), data.toString());
        ability.set("Ability_" + index, abilitySlot);
        abilitySlot.setString("FunctionCount", "0");
        abilitySlot.setString("id", UUID.randomUUID().toString());

        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return NBTUtil.setInteger(CraftItemStack.asBukkitCopy(nmsItem), 1, "hasAbility");
    }

    public static boolean hasAbility(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());

        return attributes.hasKey("hasAbility");
    }

    public static Object retrieveData(EnumAbilityData dataType, ItemStack item, int index) {
        if(index > 5) {
            throw new NullPointerException("Ability index can't be higher than 5!");
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());

        return abilitySlot.getString(dataType.getA());
    }

    public static boolean hasAbility(ItemStack item, int index) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());

        return abilitySlot.hasKey("id");
    }

    public static ItemStack removeAbility(ItemStack item, int index) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        ability.remove("Ability_" + index);

        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static ItemStack setFunctionData(ItemStack item, int index, EnumFunctionsData type, int count, Object data) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 5)
            throw new NullPointerException("Function count can't be higher than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        NBTTagCompound functionSlot = (function.getCompound("Function_" + count) != null ? function.getCompound("Function_" + count) : new NBTTagCompound());

        functionSlot.setString(type.getA(), data.toString());
        function.set("Function_" + count, functionSlot);
        functionSlot.setString("id", UUID.randomUUID().toString());
        abilitySlot.setString("id", UUID.randomUUID().toString());

        abilitySlot.set("Functions", function);
        ability.set("Ability_" + index, abilitySlot);
        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return NBTUtil.setInteger(CraftItemStack.asBukkitCopy(nmsItem), 1, "hasAbility");
    }
    public static ItemStack setFunctionData(ItemStack item, int index, String type, int count, Object data) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 5)
            throw new NullPointerException("Function count can't be higher than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        NBTTagCompound functionSlot = (function.getCompound("Function_" + count) != null ? function.getCompound("Function_" + count) : new NBTTagCompound());

        functionSlot.setString(type, data.toString());
        function.set("Function_" + count, functionSlot);
        functionSlot.setString("id", UUID.randomUUID().toString());
        abilitySlot.setString("id", UUID.randomUUID().toString());

        abilitySlot.set("Functions", function);
        ability.set("Ability_" + index, abilitySlot);
        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return NBTUtil.setInteger(CraftItemStack.asBukkitCopy(nmsItem), 1, "hasAbility");
    }

    public static Object retrieveFunctionData(EnumFunctionsData dataType, ItemStack item, int index, int count) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 5)
            throw new NullPointerException("Ability Function can't be more than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        NBTTagCompound functionSlot = (function.getCompound("Function_" + count) != null ? function.getCompound("Function_" + count) : new NBTTagCompound());

        return "" + functionSlot.getString(dataType.getA());
    }
    public static Object retrieveFunctionData(String type, ItemStack item, int index, int count) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 3)
            throw new NullPointerException("Ability Function can't be more than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        NBTTagCompound functionSlot = (function.getCompound("Function_" + count) != null ? function.getCompound("Function_" + count) : new NBTTagCompound());

        return "" + functionSlot.getString(type);
    }
    public static Set<String> listFunctionData(ItemStack item, int index, int count) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 3)
            throw new NullPointerException("Ability Function can't be more than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound functionSlot = (abilitySlot.getCompound("Function_" + count) != null ? abilitySlot.getCompound("Function_" + count) : new NBTTagCompound());
        return functionSlot.c();
    }

    public static boolean hasFunctionData(ItemStack item, int index, int count, EnumFunctionsData type) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 5)
            throw new NullPointerException("Function count can't be higher than 5!");
        if(retrieveFunctionData(type,item,index,count).equals("")){
            return false;
        } else {
            return true;
        }
    }
    public static boolean hasFunctionData(ItemStack item, int index, int count, String type){
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(count > 5)
            throw new NullPointerException("Function count can't be higher than 5!");

        if(retrieveFunctionData(AFromString(type),item,index,count).equals("")){
            return false;
        } else {
            return true;
        }
    }
    public static boolean hasFunction(ItemStack item, int index) {
        if (index > 5) {
            throw new NullPointerException("Ability index can't be higher than 5!");
        } else {
            for (int i = 1; i <= 5; i++) {
                if(AbilityData.stringFromFunction(item, EnumFunctionsData.ID, index, i, null) == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack removeFunction(ItemStack item, int index, int count, Player player) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        abilitySlot.remove("Function_" + count);

        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }
    public static ItemStack removeFunction(ItemStack item, int index, int count) {
        if(index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        function.remove("Function_" + count);

        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    public static List<String> AFromB(int B) {
        ArrayList<String> array = new ArrayList<>();
        for(EnumFunctionsData value : EnumFunctionsData.values()){
            if(B == value.getB()){
                array.add(value.getA());
            }
        }
        return array;
    }
    public static EnumFunctionsData AFromString(String A){
        for(EnumFunctionsData value : EnumFunctionsData.values()){
            if(A.equals(value.getA())){
                return value;
            }
        }
        return null;
    }

    public static int getAbilityAmount(ItemStack item) {
        if(item!=null) {
            if(item.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
                NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
                return ability.c().size();
            }
        }
        return 0;
    }

    public static List<ItemStack> ListOfParticles(){
        List<ItemStack> array = new ArrayList<>();
        for(Particles value : Particles.values()) {
            if(value.equals(Particles.BLOCK_CRACK)||value.equals(Particles.ITEM_CRACK)||value.equals(Particles.BLOCK_DUST)) {

            } else {
                array.add(StackUtils.makeColorfulItem(value.getB(),"&7Particle: &a" + value.name(),1,0,"\n&eClick to set!"));
            }
        }
        return array;
    }
    public static Particles ValueFromName(String name) {
        Particles finally2 = null;
        for(Particles value : Particles.values()) {
            if(value.name().equals(name)) {
                finally2 = value;
            }
        }
        return finally2;
    }
    public static Float floatFromFunction(ItemStack item, EnumFunctionsData functionType, int index, int count, float defaultValue) {
        if (NumUtils.isFloat(AbilityData.retrieveFunctionData(functionType, item, index, count).toString())) {
            return Float.parseFloat(AbilityData.retrieveFunctionData(functionType, item, index, count).toString());
        }
        return defaultValue;
    }
    public static int intFromFunction(ItemStack item, EnumFunctionsData functionType, int index, int count, int defaultValue) {
        if(AbilityData.retrieveFunctionData(functionType, item, index, count).toString().equals("")){
            return defaultValue;
        }
        if (NumUtils.isInt(AbilityData.retrieveFunctionData(functionType, item, index, count).toString())) {
            return Integer.parseInt(AbilityData.retrieveFunctionData(functionType, item, index, count).toString());
        }
        return defaultValue;
    }
    public static boolean booleanFromFunction(ItemStack item, EnumFunctionsData functionType, int index, int count, boolean defaultValue) {
        if(AbilityData.retrieveFunctionData(functionType, item, index, count).toString().equals("")){
            return defaultValue;
        }
        if(AbilityData.retrieveFunctionData(functionType, item, index, count).toString().equals("True")){
            return true;
        } else if (AbilityData.retrieveFunctionData(functionType, item, index, count).toString().equals("False")){
            return false;
        }
        return defaultValue;
    }
    public static String stringFromFunction(ItemStack item, EnumFunctionsData functionType, int index, int count, String defaultValue) {
        if(AbilityData.retrieveFunctionData(functionType, item, index, count).toString().equals("")){
            return defaultValue;
        }
        return AbilityData.retrieveFunctionData(functionType, item, index, count).toString();
    }
}
