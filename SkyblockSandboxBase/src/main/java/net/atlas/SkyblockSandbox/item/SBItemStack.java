package net.atlas.SkyblockSandbox.item;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.NumberTruncation.RomanNumber;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.*;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.RaritiesGUI.masterStarKey;
import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.RaritiesGUI.starKey;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.GEAR_SCORE;
import static net.atlas.SkyblockSandbox.player.pets.PetBuilder.petXP;

public class SBItemStack extends ItemStack {
    public static String[] statsformat = {"Gear_Score", "Damage", "Strength", "Crit_Chance", "Crit_Damage", "Attack_Speed", "blank", "Health", "Defense", "Intelligence", "Speed", "Ferocity", "Mining_Speed", "Mining_Fortune", "Pristine", "blank"};

    private ItemStack stack;
    private String itemID;

    public SBItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public SBItemStack(String name, String id, Material mat, Rarity rarity, ItemType type, int durability, boolean stackable, boolean reforgeable, HashMap<PlayerStat, Double> stats) {
        stack = new ItemStack(mat, 1);
        itemID = id;
        setName(stack, name);
        setString(stack, id.toUpperCase(), "ID");
        setString(stack, rarity.name(), "RARITY");
        setString(stack, "true", "non-legacy");
        if (type != null) {
            setString(stack, type.getValue(), "item-type");
        } else {
            setString(stack, ItemType.ITEM.getValue(), "item-type");
        }
        setDurability(stack, durability);
        for (PlayerStat stat : stats.keySet()) {
            stack = setStat(stack, stat, stats.get(stat));
        }
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
        stack = refreshLore();
    }
    public SBItemStack(String name, String id, Material mat, Rarity rarity, ItemType type, int durability, boolean stackable, boolean reforgeable) {
        stack = new ItemStack(mat, 1);
        itemID = id;
        setName(stack, name);
        setString(stack, id.toUpperCase(), "ID");
        setString(stack, rarity.name(), "RARITY");
        setString(stack, "true", "non-legacy");
        if (type != null) {
            setString(stack, type.getValue(), "item-type");
        } else {
            setString(stack, ItemType.ITEM.getValue(), "item-type");
        }
        setDurability(stack, durability);
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
        stack = refreshLore();
    }

    public SBItemStack(String name, String id, Material mat, Rarity rarity, ItemType type, String url, int durability, boolean stackable, boolean reforgeable, HashMap<PlayerStat, Double> stats) {
        stack = new ItemStack(mat, 1);
        itemID = id;
        setString(stack, id.toUpperCase(), "ID");
        setString(stack, rarity.name(), "RARITY");
        setString(stack, type.getValue(), "item-type");
        setName(stack, name);
        setDurability(stack, durability);

        setString(stack, "true", "non-legacy");
        for (PlayerStat stat : stats.keySet()) {
            stack = setStat(stack, stat, stats.get(stat));
        }
        if(url.contains(".")) {
            stack = applyUrl(stack, url);
        } else {
            stack = applyTexture(stack , url);
        }
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
    }

    public SBItemStack(ItemStack item, String itemID) {
        this.stack = item;
        itemID = itemID.toUpperCase().replace(' ', '_');
        this.stack = setString(stack, itemID, "ID");
    }

    public SBItemStack(String name, Material mat, int amount) {
        stack = new ItemStack(mat, amount);
        stack = setName(stack, name);
    }

    public SBItemStack(String name, Material mat, int amount, byte dmg) {
        stack = new ItemStack(mat, amount, dmg);
        stack = setName(stack, name);
    }

    public ItemStack setAbility(Ability ability, int index) {
        stack = setAbilityData(index,AbilityValue.NAME,ability.getAbilityName());
        stack = setAbilityData(index,AbilityValue.MANA_COST,ability.getManaCost());
        stack = setAbilityData(index,AbilityValue.CLICK_TYPE,ability.getAbilityType());
        for(String s:ability.getAbilDescription()) {
            stack = addAbilityDescriptionLine(stack,s,index);
        }
        return stack;
    }

    public ItemStack setAbilityData(int index,AbilityValue value,Object data) {
        return AbilityUtil.setAbilityData(stack,index,value,data);
    }

    public String getAbilityData(int index,AbilityValue value) {
        return AbilityUtil.getAbilityData(stack,index,value);
    }

    public ItemStack refreshName() {

        StringBuilder petLvl = new StringBuilder();
        if (Boolean.parseBoolean(NBTUtil.getString(stack, "is-pet"))) {
            petLvl.append(SUtil.colorize("&7[Lvl"));
            petLvl.append(NBTUtil.getInteger(stack, "pet-level"));
            petLvl.append("] ");
        }

        String s = NBTUtil.getString(stack, "RARITY");
        Rarity rarity;
        if (!s.equals("")) {
            rarity = Enums.getIfPresent(Rarity.class, s.toUpperCase().replace(' ', '_')).orNull();
            if (rarity == null) {
                s = NBTUtil.getString(stack, "rarity");
                if (!s.equals("")) {
                    rarity = Enums.getIfPresent(Rarity.class, s.toUpperCase().replace(' ', '_')).orNull();
                    if (rarity == null) {
                        rarity = Rarity.COMMON;
                    }
                } else {
                    rarity = Rarity.COMMON;
                }
            }
        } else {
            rarity = null;
        }

        int starAmt = NBTUtil.getInteger(stack, starKey);
        int masterStarAmt = NBTUtil.getInteger(stack, masterStarKey);
        if (masterStarAmt > starAmt) {
            masterStarAmt = starAmt;
        }
        ItemMeta meta = stack.getItemMeta();
        if (starAmt == 0 && masterStarAmt == 0) {
            if (ChatColor.stripColor(meta.getDisplayName()) == null || ChatColor.stripColor(meta.getDisplayName()).isEmpty()) {
                stack = NBTUtil.setString(stack, "null", "item-name");
            } else {
                if (NBTUtil.getString(stack, "item-name").isEmpty() || NBTUtil.getString(stack, "item-name") == null) {
                    stack = NBTUtil.setString(stack, meta.getDisplayName(), "item-name");
                }
            }

        }
        StringBuilder builder = new StringBuilder();
        for (int b = 0; b < starAmt; b++) {
            if (masterStarAmt >= b + 1) {
                builder.append("§c✪");
            } else {
                builder.append("§6✪");
            }
        }
        if (starAmt != 0 || masterStarAmt != 0) {
            builder.append(" ");
        }

        meta = stack.getItemMeta();
        if (rarity != null) {
            meta.setDisplayName(builder + petLvl.toString() + rarity.getColor() + SUtil.colorize(NBTUtil.getString(stack, "item-name")));
        } else {
            meta.setDisplayName(builder + petLvl.toString() + SUtil.colorize(NBTUtil.getString(stack, "item-name")));
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack refreshLore() {
        if (stack != null) {
            if (!NBTUtil.getString(stack, "non-legacy").equals("true")) {
                return stack;
            }
            if (stack.hasItemMeta()) {
                ItemMeta meta = stack.getItemMeta();

                List<String> oldLore = meta.getLore();
                List<String> newLore = new ArrayList<>();
                List<String> enchantsLore = new ArrayList<>();
                List<String> ultEnchantsLore = new ArrayList<>();

                int i = 0;
                if (!NBTUtil.getString(stack, "pet-type").equals("")) {
                    newLore.add(SUtil.colorize("&8" + NBTUtil.getString(stack, "pet-type")));
                    newLore.add("");
                }
                String prevS = "";
                for (String s : statsformat) {
                    i++;
                    if (s.equals("blank")) {
                        if (!prevS.equals("")) {
                            newLore.add("");
                        }
                    } else {
                        int a = getInteger(stack, s.toUpperCase());
                        if (a != 0) {
                            prevS = s;
                            PlayerStat stat = Enums.getIfPresent(PlayerStat.class, s.toUpperCase()).orNull();
                            s = s.replace('_', ' ');
                            if (stat != null) {
                                if (stat.equals(GEAR_SCORE)) {
                                    newLore.add(ChatColor.GRAY + s + ":" + stat.getColor() + " " + a);
                                } else {
                                    newLore.add(ChatColor.GRAY + s + ":" + stat.getColor() + " +" + a);
                                }
                            }


                        }
                    }
                }

                if (hasAnyEnchant()) {
                    HashMap<Enchantment, Integer> enchants = getItemEnchants();
                    if (!enchants.isEmpty()) {
                        for (Enchantment e : enchants.keySet()) {
                            if (e.isUltimate()) {
                                ultEnchantsLore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + e.getName() + " " + RomanNumber.toRoman(enchants.get(e)));
                            } else {
                                enchantsLore.add(ChatColor.BLUE + e.getName() + " " + RomanNumber.toRoman(enchants.get(e)));
                            }
                        }
                        if (enchants.size() > 4) {
                            enchantsLore.sort(Collator.getInstance());
                            ultEnchantsLore.addAll(enchantsLore);
                            String enchantString = String.join(",", ultEnchantsLore);

                            List<String> strings = nthOccurence(enchantString, 2, ',');

                            for (String s : strings) {
                                strings.set(strings.indexOf(s), ChatColor.BLUE + s);
                            }
                            newLore.addAll(strings);
                        } else {
                            ultEnchantsLore.addAll(enchantsLore);
                            newLore.addAll(ultEnchantsLore);
                        }
                        newLore.add("");
                    }
                }


                List<String> description = getDescription(stack);
                LinkedHashMap<Integer, String> descriptionMap = getDescriptionWithLine(stack);
                Map<Integer, String> map = new TreeMap<>(descriptionMap);
                if (!descriptionMap.isEmpty()) {
                    int prevnum = -1;
                    for (Integer bb : map.keySet()) {
                        int iter = newLore.size();
                        if (prevnum != -1 && prevnum != bb - 1) {
                            for (int ii = prevnum; ii < bb - 1; ii++) {
                                newLore.add("can-overwrite");
                            }
                        }
                        newLore.add(descriptionMap.get(bb));
                        prevnum = bb;
                    }
                    for (String s : newLore) {
                        if (s.equals("can-overwrite")) {
                            newLore.set(newLore.indexOf(s), "");
                        }
                    }
                }
                if (!description.isEmpty()) {
                    newLore.addAll(description);
                    newLore.add("");
                }

                HashMap<String, String> abilityNames = new HashMap<>();
                List<String> abilityType = new ArrayList<>();

                if (hasAbility()) {

                    for (int b = 1; b < getAbilAmount() + 1; b++) {
                        abilityNames.put(AbilityUtil.getAbilityData(stack,b, AbilityValue.NAME), AbilityUtil.getAbilityData(stack,b, AbilityValue.MANA_COST));
                        abilityType.add(AbilityUtil.getAbilityData(stack,b, AbilityValue.CLICK_TYPE));
                    }
                    int j = 1;
                    for (String s : abilityNames.keySet()) {
                        newLore.add(ChatColor.GOLD + "Item Ability: " + s + " " + ChatColor.YELLOW + ChatColor.BOLD + abilityType.get(j - 1).replace('_', ' '));
                        newLore.addAll(getAbilityDescription(stack, j));
                        DecimalFormat format = new DecimalFormat("#");
                        Integer d = 0;
                        if (!abilityNames.get(s).isEmpty()) {
                            d = new Double(abilityNames.get(s)).intValue();
                        }
                        String manacost = format.format(d);
                        if (!manacost.equals("0.0")) {
                            if (!manacost.equals("")) {
                                if (!manacost.equals("0")) {
                                    newLore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + manacost);
                                }
                            }
                        }
                        newLore.add("");
                        j++;
                    }
                }


                //if pet set XP bar
                StringBuilder petLvl = new StringBuilder();
                if (Boolean.parseBoolean(NBTUtil.getString(stack, "is-pet"))) {
                    petLvl.append(SUtil.colorize("&7[Lvl"));
                    petLvl.append(NBTUtil.getInteger(stack, "pet-level"));
                    petLvl.append("] ");
                    for (int b = 0; b < 3; b++) {
                        List<String> perkDescript = NBTUtil.getPetPerkDescription(stack, b + 1);
                        String perkName = NBTUtil.getPerkName(stack, b + 1);
                        newLore.add(SUtil.colorize("&6" + ChatColor.stripColor(perkName)));
                        if (!perkDescript.isEmpty()) {
                            newLore.addAll(perkDescript);
                            newLore.add("");
                        }
                    }

                    if (Boolean.parseBoolean(NBTUtil.getString(stack, "is-equipped"))) {

                        int totalXp = petXP[NBTUtil.getInteger(stack, "pet-level")];
                        if (totalXp != 0) {
                            if (NBTUtil.getInteger(stack, "pet-level") < 100) {
                                int percent = NBTUtil.getInteger(stack, "pet-xp") * 100 / totalXp;
                                double c = Math.round(percent * 10.0) / 10.0;
                                newLore.add(SUtil.colorize("&7Progress to level " + (NBTUtil.getInteger(stack, "pet-level") + 1) + ": &e" + c + "%"));
                                newLore.add(SUtil.colorize(getProgressBar(NBTUtil.getInteger(stack, "pet-xp"), totalXp, 20, '-', ChatColor.DARK_GREEN, ChatColor.WHITE) + " &e" + NBTUtil.getInteger(stack, "pet-xp") + "&6/&e" + NumberSuffix.format(totalXp)));
                            } else {
                                newLore.add(SUtil.colorize("&b&lMAX LEVEL"));
                            }
                            newLore.add("");
                        }
                    } else {
                        newLore.addAll(SUtil.colorize("&eRight-click to add this pet to", "&eYour pet menu!"));
                        newLore.add("");
                    }
                }


                String s = NBTUtil.getString(stack, "RARITY");
                Rarity rarity;
                if (!s.equals("")) {
                    rarity = Enums.getIfPresent(Rarity.class, s.toUpperCase().replace(' ', '_')).orNull();
                    if (rarity == null) {
                        s = NBTUtil.getString(stack, "rarity");
                        if (!s.equals("")) {
                            rarity = Enums.getIfPresent(Rarity.class, s.toUpperCase().replace(' ', '_')).orNull();
                            if (rarity == null) {
                                rarity = Rarity.COMMON;
                            }
                        } else {
                            rarity = Rarity.COMMON;
                        }
                    }
                } else {
                    rarity = null;
                }
                if (getString(stack, "reforgable").equals("true")) {
                    newLore.add(ChatColor.DARK_GRAY + "This item can be reforged!");
                }
                if (Boolean.parseBoolean(NBTUtil.getString(stack, "is-equipped"))) {
                    rarity = null;
                    if (!Boolean.parseBoolean(NBTUtil.getString(stack, "is-active"))) {
                        newLore.add(SUtil.colorize("&eClick to summon."));
                    } else {
                        newLore.add(SUtil.colorize("&cClick to despawn."));
                    }
                }
                if (rarity != null) {
                    ItemType type = Enums.getIfPresent(ItemType.class, NBTUtil.getString(stack, "item-type")).or(ItemType.ITEM);
                    if (getInteger(stack, "rarity_upgrades") >= 1) {
                        String recombsymbol = rarity.getColor() + "" + ChatColor.MAGIC + "L" + ChatColor.stripColor("") + rarity.getColor() + "" + ChatColor.BOLD;
                        newLore.add(recombsymbol + " " + rarity.name() + " " + type.getValue() + " " + recombsymbol);
                    } else {
                        newLore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.getValue());
                    }
                }

                for(String ss:NBTUtil.getAllSignatures(stack)) {
                    String pString = NBTUtil.getSignature(stack,ss);
                    if(Bukkit.getPlayer(pString)!=null) {
                        if(Integer.parseInt(ss)>newLore.size()) {
                            ss = String.valueOf(newLore.size());
                        }
                        newLore.add(Integer.parseInt(ss),SUtil.colorize("&8Signed by: &e" + pString + " &a✔"));
                    }
                }

                meta.setLore(newLore);
                stack.setItemMeta(meta);
                stack = refreshName();
                return stack;
            }
        }
        return null;
    }

    static List<String> nthOccurence(String input, int n, char delimiter) {
        int k = 0;
        int startPoint = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == delimiter) {
                k++;
                if (k == n) {
                    String ab = input.substring(startPoint, i);
                    arrayList.add(ab.replace(",", ", "));
                    startPoint = i + 1;
                    k = 0;
                }
            }
        }
        return arrayList;
    }

    public double getStat(PlayerStat stat) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) return 0;

                return data.getDouble(stat.name());
            }
        }
        return 0;
    }

    public ItemStack addEnchantment(Enchantment enchantment, int lvl) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound enchants = data.getCompound("enchantments");
                if (enchants == null) {
                    enchants = new NBTTagCompound();
                }

                enchants.setInt(enchantment.name(), lvl);
                data.set("enchantments", enchants);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            }
        }
        return stack;
    }

    public boolean hasAnyEnchant() {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound enchants = data.getCompound("enchantments");
                if (enchants == null) {
                    enchants = new NBTTagCompound();
                }
                return enchants.c().size() != 0;
            }
        }
        return false;
    }

    public ItemStack setStat(ItemStack stack, PlayerStat stat, Double v) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

                data.setInt(stat.name(), v.intValue());
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                this.stack = CraftItemStack.asBukkitCopy(nmsItem);
                return this.stack;
            }
        }
        return null;
    }

    public ItemStack setInteger(ItemStack stack, int v, String key) {
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
                this.stack = CraftItemStack.asBukkitCopy(nmsItem);
                return this.stack;
            }
        }
        return null;
    }

    public Integer getInteger(ItemStack stack, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
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

    public ItemStack setString(ItemStack stack, String msg, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();

                data.setString(key, msg);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                this.stack = CraftItemStack.asBukkitCopy(nmsItem);
                return this.stack;
            }
        }
        return null;
    }
    public ItemStack setString(String msg, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();

                data.setString(key, msg);
                tag.set("ExtraAttributes", data);
                nmsItem.setTag(tag);
                this.stack = CraftItemStack.asBukkitCopy(nmsItem);
                return this.stack;
            }
        }
        return null;
    }

    public String getString(ItemStack stack, String key) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
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

    public int getEnchantment(Enchantment enchant) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                NBTTagCompound enchants = data.getCompound("enchantments");
                if (enchants == null) {
                    enchants = new NBTTagCompound();
                }

                return enchants.getInt(enchant.name());
            }
        }
        return 0;
    }

    public HashMap<Enchantment, Integer> getItemEnchants() {
        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        for (Enchantment enchant : Enchantment.values()) {
            int lvl = getEnchantment(enchant);
            if (lvl != 0) {
                enchants.put(enchant, lvl);
            }
        }
        return enchants;
    }

    public ItemStack removeAbil(ItemStack stack, int index) {
        if (index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());
        ability.remove("Ability_" + index);

        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        stack = CraftItemStack.asBukkitCopy(nmsItem);
        stack = setInteger(stack, 1, "hasAbility");
        this.stack = stack;

        return stack;
    }

    public ItemStack addAbilityDescriptionLine(ItemStack item, String line, int index) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        if (item != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound abilities = data.getCompound("Abilities");
            NBTTagCompound ability = abilities.getCompound("Ability_" + index);
            NBTTagCompound description = ability.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }
            List<String> descriptionList = new ArrayList<>(descriptionStrings);
            descriptionList.add(line);
            for (int i = 0; i < descriptionList.size(); i++) {
                description.setString(String.valueOf(i), descriptionList.get(i));
            }
            ability.set("description", description);
            abilities.set("Ability_" + index, ability);
            data.set("Abilities", abilities);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            this.stack = CraftItemStack.asBukkitCopy(nmsItem);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

    public ItemStack removeAbilityDescriptionLine(ItemStack host, int abilIndex, int lineindex) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound abilities = data.getCompound("Abilities");
            NBTTagCompound ability = abilities.getCompound("Ability_" + abilIndex);
            NBTTagCompound description = ability.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }
            List<String> descriptionList = new ArrayList<>(descriptionStrings);
            descriptionList.remove(lineindex);
            NBTTagCompound descript2 = new NBTTagCompound();
            for (int i = 0; i < descriptionList.size(); i++) {
                descript2.setString(String.valueOf(i), descriptionList.get(i));
            }
            ability.set("description", descript2);
            abilities.set("Ability_" + abilIndex, ability);
            data.set("Abilities", abilities);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

    public ItemStack setAbilDescriptLine(String line, int abilIndex, int lineindex) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        if (this.stack != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound abilities = data.getCompound("Abilities");
            NBTTagCompound ability = abilities.getCompound("Ability_" + abilIndex);
            NBTTagCompound description = ability.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }
            List<String> descriptionList = new ArrayList<>(descriptionStrings);
            descriptionList.set(lineindex, line);
            for (int i = 0; i < descriptionList.size(); i++) {
                description.setString(String.valueOf(i), descriptionList.get(i));
            }
            ability.set("description", description);
            abilities.set("Ability_" + abilIndex, ability);
            data.set("Abilities", abilities);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            this.stack = CraftItemStack.asBukkitCopy(nmsItem);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

    public ItemStack addDescriptionLine(String line, int lineindex) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        if (this.stack != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound description = data.getCompound("description");
            LinkedHashMap<Integer, String> descriptMap = new LinkedHashMap<>(getDescriptionWithLine(stack));

            /*int iter = descriptMap.size();
            if (lineindex > (descriptMap.size() - 1)) {
                for (int i = iter; i <= lineindex; i++) {
                    descriptMap.put(i,"");
                }
                descriptMap.put(lineindex, line);

            }*/

            descriptMap.put(lineindex, line);

            for (int i : descriptMap.keySet()) {
                /*if(descriptMap.get(i)!=null) {*/
                description.setString(String.valueOf(i), descriptMap.get(i));
                /*}*/
            }
            data.set("description", description);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            this.stack = CraftItemStack.asBukkitCopy(nmsItem);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;

    }

    public int getAbilAmount() {
        if (this.stack != null) {
            if (this.stack.hasItemMeta()) {
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
                NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
                NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
                NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
                return ability.c().size();
            }
        }
        return 0;
    }

    public List<String> getAbilityDescription(ItemStack host, int index) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound abilities = data.getCompound("Abilities");
            NBTTagCompound ability = abilities.getCompound("Ability_" + index);
            NBTTagCompound description = ability.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }

            return descriptionStrings;
        }
        return null;
    }

    public ItemStack addDescriptionLine(ItemStack host, String line) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound description = data.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }
            List<String> descriptionList = new ArrayList<>(descriptionStrings);
            descriptionList.add(line);
            for (int i = 0; i < descriptionList.size(); i++) {
                description.setString(String.valueOf(i), descriptionList.get(i));
            }
            data.set("description", description);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

    public List<String> getDescription(ItemStack host) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound description = data.getCompound("description");
            Set<String> desc = description.c();
            ArrayList<String> descList = new ArrayList<>();
            for (int i = 0; i < desc.size(); i++) {
                descList.add(description.getString(String.valueOf(i)));
            }
            return descList;
        }
        return null;
    }

    public LinkedHashMap<Integer, String> getDescriptionWithLine(ItemStack host) {
        if (host != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(host);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound description = data.getCompound("description");
            Set<String> desc = description.c();
            LinkedHashMap<Integer, String> descMap = new LinkedHashMap<>();
            for (String s : desc) {
                descMap.put(Integer.parseInt(s), description.getString(s));
            }
            return descMap;
        }
        return null;
    }

    public boolean hasAbility() {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound abilities = (attributes.getCompound("Abilities") != null ? tag.getCompound("Abilities") : new NBTTagCompound());

        return abilities.hasKey("has-ability");
    }

    public boolean hasAbility(int index) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound abils = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());

        return abils.hasKey("Ability_" + index);
    }

    public boolean hasFunction(int index) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound abils = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound func = (attributes.getCompound("Ability_" + index) != null ? attributes.getCompound("Ability_" + index) : new NBTTagCompound());

        for (int i = 0; i < 5; i++) {
            if (func.hasKey("Function_" + i + 1)) {
                return true;
            }
        }
        return false;
    }

    private ItemStack setName(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        StringBuilder builder = new StringBuilder();
        meta.setDisplayName(SUtil.colorize(name));
        stack.setItemMeta(meta);
        return stack;
    }

    public String getItemID() {
        return itemID;
    }

    public ItemStack asBukkitItem() {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.spigot().setUnbreakable(true);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack applyUrl(ItemStack item, String url) {
        if (item == null) return item;
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        item.setItemMeta(itemMeta);
        return item;
    }
    public ItemStack applyTexture(ItemStack item, String texture) {
        if (item == null) return item;
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = texture.getBytes();
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    public void setDurability(ItemStack item, int durability) {
        item.setDurability((short) durability);
    }

    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
