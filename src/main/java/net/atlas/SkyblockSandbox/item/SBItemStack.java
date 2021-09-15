package net.atlas.SkyblockSandbox.item;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.NumberTruncation.RomanNumber;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.*;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.RaritiesGUI.masterStarKey;
import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.RaritiesGUI.starKey;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.*;
import static net.atlas.SkyblockSandbox.player.pets.PetBuilder.petXP;

public class SBItemStack extends ItemStack {
    public static String[] statsformat = {"Gear_Score", "Damage", "Strength", "Crit_Chance", "Crit_Damage", "Attack_Speed", "blank", "Health", "Defense", "Intelligence", "Speed", "Ferocity", "Mining_Speed", "Mining_Fortune", "Pristine", "blank"};

    private ItemStack stack;
    private String itemID;

    public SBItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public SBItemStack(String name, Material mat, int durability, boolean stackable, boolean reforgeable, double damage, double strength, double intelligence, double ferocity, double health, double attack_speed, double defense) {
        stack = new ItemStack(mat, 1);
        setName(stack, name);
        setDurability(stack, durability);
        stack = setStat(stack, DAMAGE, damage);
        stack = setStat(stack, STRENGTH, strength);
        stack = setStat(stack, INTELLIGENCE, intelligence);
        stack = setStat(stack, FEROCITY, ferocity);
        stack = setStat(stack, ATTACK_SPEED, attack_speed);
        stack = setStat(stack, DEFENSE, defense);
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");

    }

    public SBItemStack(String name, Material mat, int durability, boolean stackable, boolean reforgeable, String url, Rarity rarity, double damage, double strength, double intelligence, double ferocity, double crit_chance, double crit_damage, double health, double attack_speed, double defense) {
        stack = new ItemStack(mat, 1);
        setName(stack, rarity.getColor() + name);
        setDurability(stack, durability);
        stack = setString(stack, rarity.getName(), "RARITY");
        stack = setStat(stack, DAMAGE, damage);
        stack = setStat(stack, STRENGTH, strength);
        stack = setStat(stack, INTELLIGENCE, intelligence);
        stack = setStat(stack, FEROCITY, ferocity);
        stack = setStat(stack, CRIT_CHANCE, crit_chance);
        stack = setStat(stack, CRIT_DAMAGE, crit_damage);
        stack = setStat(stack, HEALTH, health);
        stack = setStat(stack, ATTACK_SPEED, attack_speed);
        stack = setStat(stack, DEFENSE, defense);
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
        this.itemID = name.toUpperCase().replace(' ', '_');
        stack = setString(stack, this.itemID, "ID");
        applyTexture(stack, url);

    }

    public SBItemStack(ItemStack item, String itemID) {
        this.stack = item;
        itemID = itemID.toUpperCase().replace(' ', '_');
        this.stack = setString(stack, itemID, "ID");
    }

    public SBItemStack(String name, String itemID, Material mat, int durability, boolean stackable, boolean reforgeable, String url, Rarity rarity, double damage, double strength, double intelligence, double ferocity, double crit_chance, double crit_damage, double health, double attack_speed, double defense) {
        stack = new ItemStack(mat, 1);
        setName(stack, rarity.getColor() + name);
        setDurability(stack, durability);
        stack = setString(stack, rarity.getName(), "RARITY");
        stack = setStat(stack, DAMAGE, damage);
        stack = setStat(stack, STRENGTH, strength);
        stack = setStat(stack, INTELLIGENCE, intelligence);
        stack = setStat(stack, FEROCITY, ferocity);
        stack = setStat(stack, CRIT_CHANCE, crit_chance);
        stack = setStat(stack, CRIT_DAMAGE, crit_damage);
        stack = setStat(stack, HEALTH, health);
        stack = setStat(stack, ATTACK_SPEED, attack_speed);
        stack = setStat(stack, DEFENSE, defense);
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
        this.itemID = itemID;
        stack = setString(stack, this.itemID, "ID");
        applyTexture(stack, url);

    }

    public SBItemStack(String name, String itemID, Material mat, int durability, boolean stackable, boolean reforgeable, Ability ability, ItemType type, Rarity rarity, double damage, double strength, double intelligence, double ferocity, double crit_chance, double crit_damage, double health, double attack_speed, double defense) {
        stack = new ItemStack(mat, 1);
        setName(stack, rarity.getColor() + name);
        setDurability(stack, durability);
        if (ability != null) {
            stack = setAbility(stack, ability, 1);
        }
        stack = setString(stack, rarity.getName(), "RARITY");
        stack = setStat(stack, DAMAGE, damage);
        stack = setStat(stack, STRENGTH, strength);
        stack = setStat(stack, INTELLIGENCE, intelligence);
        stack = setStat(stack, FEROCITY, ferocity);
        stack = setStat(stack, CRIT_CHANCE, crit_chance);
        stack = setStat(stack, CRIT_DAMAGE, crit_damage);
        stack = setStat(stack, HEALTH, health);
        stack = setStat(stack, ATTACK_SPEED, attack_speed);
        stack = setStat(stack, DEFENSE, defense);
        if (!stackable) {
            stack = setString(stack, UUID.randomUUID().toString(), "UUID");
        }
        stack = setString(stack, Boolean.toString(reforgeable), "reforgeable");
        this.itemID = itemID;
        stack = setString(stack, this.itemID, "ID");

    }

    public SBItemStack(String name, Material mat, int amount) {
        stack = new ItemStack(mat, amount);
        stack = setName(stack, name);
    }

    public SBItemStack(String name, Material mat, int amount, byte dmg) {
        stack = new ItemStack(mat, amount, dmg);
        stack = setName(stack, name);
    }

    public ItemStack refreshLore() {
        if (stack != null) {
            if (NBTUtil.getString(stack, "is-hypixel").equals("true")) {
                //return stack;
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

                /*EnchantUtil eutil = new EnchantUtil(stack);
                if (eutil.hasAnyEnchant()) {
                    HashMap<Enchantment, Integer> enchants = getItemEnchants(stack);
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
                }*/


                List<String> description = getDescription(stack);
                LinkedHashMap<Integer, String> descriptionMap = getDescriptionWithLine(stack);
                Map<Integer, String> map = new TreeMap<>(descriptionMap);
                if (!descriptionMap.isEmpty()) {
                    for (Integer bb : map.keySet()) {
                        int iter = newLore.size();
                        if (bb > (iter - 1)) {
                            for (int c = iter; c <= bb; c++) {
                                newLore.add("can-overwrite");
                            }

                        }
                        newLore.add(bb, descriptionMap.get(bb));


                    }
                    for (String s : newLore) {
                        if (s.equals("can-overwrite")) {
                            newLore.set(newLore.indexOf(s), "");
                        }
                    }
                }
                /*if (!description.isEmpty()) {
                    newLore.addAll(description);
                    newLore.add("");
                } else {
                    if (oldLore != null) {
                        List<String> oldLoreClone = new ArrayList<>(oldLore);
                        for (String s : oldLoreClone) {
                            for (String b : statsformat) {
                                if (ChatColor.stripColor(s).contains(b.replace('_', ' '))) {
                                    oldLore.remove(s);
                                }
                            }
                        }
                        newLore.addAll(oldLore);
                        for (String d : oldLore) {
                            stack = addDescriptionLine(stack, d);
                            assert stack != null;
                        }
                    }
                }*/

                HashMap<String, String> abilityNames = new HashMap<>();
                List<String> abilityType = new ArrayList<>();

                if (hasAbility()) {

                    for (int b = 1; b < getAbilAmount() + 1; b++) {
                        abilityNames.put((String) getAbilData(EnumAbilityData.NAME, b), (String) getAbilData(EnumAbilityData.MANA_COST, b));
                        abilityType.add((String) getAbilData(EnumAbilityData.FUNCTION, b));
                    }
                    int j = 1;
                    for (String s : abilityNames.keySet()) {
                        newLore.add(ChatColor.GOLD + "Item Ability: " + s + " " + ChatColor.YELLOW + ChatColor.BOLD + abilityType.get(j - 1).replace('_', ' '));
                        newLore.addAll(getAbilityDescription(stack, j));
                        DecimalFormat format = new DecimalFormat("#");
                        Integer d = new Double(abilityNames.get(s)).intValue();
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
                if (getString(stack, " reforgable").equals("true")) {
                    newLore.add(ChatColor.DARK_GRAY + "This stack can be reforged!");
                }
                if (rarity != null) {
                    if (getInteger(stack, "rarity_upgrades") >= 1) {
                        String recombsymbol = rarity.getColor() + "" + ChatColor.MAGIC + "L" + ChatColor.stripColor("") + rarity.getColor() + "" + ChatColor.BOLD;
                        newLore.add(recombsymbol + " " + rarity.name() + " " + recombsymbol);
                    } else {
                        newLore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name());
                    }
                }
                int starAmt = NBTUtil.getInteger(stack, starKey);
                int masterStarAmt = NBTUtil.getInteger(stack, masterStarKey);
                if (masterStarAmt > starAmt) {
                    masterStarAmt = starAmt;
                }
                stack.setItemMeta(meta);
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
                meta.setLore(newLore);
                stack.setItemMeta(meta);
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
                NBTTagCompound data = tag.getCompound("ExtraAttributes");
                if (data == null) {
                    data = new NBTTagCompound();
                }

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

    public ItemStack setAbilData(ItemStack stack, EnumAbilityData dataType, Object data, int index) {
        if (index > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
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

        stack = CraftItemStack.asBukkitCopy(nmsItem);
        stack = setInteger(stack, 1, "hasAbility");
        this.stack = stack;

        return stack;
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

    public Object getAbilData(EnumAbilityData dataType, int index) {
        if (index > 5) {
            throw new NullPointerException("Ability index can't be higher than 5!");
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + index) != null ? ability.getCompound("Ability_" + index) : new NBTTagCompound());

        return abilitySlot.getString(dataType.getA());
    }

    public ItemStack setAbility(ItemStack item, AbilityType type, String name, double manaCost, List<String> description, int index) {
        item = setAbilData(item, EnumAbilityData.NAME, name, index);
        item = setAbilData(item, EnumAbilityData.MANA_COST, manaCost, index);
        item = setAbilData(item, EnumAbilityData.FUNCTION, type.getText(), index);
        for (String s : description) {
            item = addAbilityDescriptionLine(item, s, index);
        }
        return item;
    }

    public ItemStack setAbility(ItemStack item, Ability ability, int index) {
        item = setAbilData(item, EnumAbilityData.NAME, ability.getAbilityName(), index);
        item = setAbilData(item, EnumAbilityData.MANA_COST, ability.getManaCost(), index);
        item = setAbilData(item, EnumAbilityData.FUNCTION, ability.getAbilityType().getText(), index);
        for (String s : ability.getAbilDescription()) {
            item = addAbilityDescriptionLine(item, s, index);
        }
        this.stack = item;
        return item;
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

        return attributes.hasKey("hasAbility");
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
        meta.setDisplayName(SUtil.colorize(name));
        stack.setItemMeta(meta);
        stack = setString(stack, SUtil.colorize(name), "item-name");
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

    public ItemStack applyTexture(ItemStack item, String url) {
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
