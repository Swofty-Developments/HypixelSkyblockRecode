package net.atlas.SkyblockSandbox.item;

import com.google.common.base.Enums;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.atlas.SkyblockSandbox.abilityCreator.Ability;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.item.enchant.Enchantment;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumberTruncation.RomanNumber;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.IntStream;

import static net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil.getAbilityData;
import static net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil.getAbilityString;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.GEAR_SCORE;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.PET_LUCK;
import static net.atlas.SkyblockSandbox.util.NBTUtil.*;

public class SBItemBuilder {
    public boolean legacy;
    public ItemStack stack;
    public Material mat;
    public String name;
    public String id = "";
    public Rarity rarity = Rarity.COMMON;
    public ItemType type = ItemType.ITEM;
    public String url;
    public String texture;
    public int normalstar;
    public int masterstar;
    public boolean stackable;
    public boolean glowing;
    public boolean reforgeable;
    public boolean recombobulated;
    public ArrayList<String> description = new ArrayList<>();
    public ArrayList<ItemFlag> flags = new ArrayList<>();
    public String hexColor = "";
    public HashMap<SBPlayer.PlayerStat, Double> stats = new HashMap<>();
    public HashMap<Integer, Ability> abilities = new HashMap<>();
    public HashMap<Enchantment, Integer> enchants = new HashMap<>();
    public static String[] statsformat = {"Gear_Score", "Damage", "Strength", "Crit_Chance", "Crit_Damage", "Attack_Speed", "Sea_Creature_Chance", "Ability_Damage", "Health", "Defense", "Intelligence", "Speed", "Magic_Find", "True_Defense", "Ferocity", "Mining_Speed", "Mining_Fortune", "Pristine"};

    public SBItemBuilder(ItemStack item) {
        legacy = false;
        stack = item;
        id = getString(item, "ID");
        rarity = Rarity.valueOf(getString(item, "RARITY"));
        mat = item.getType();
        name = getString(item, "name");
        masterstar = getInteger(item, "masterStars");
        normalstar = getInteger(item, "normalStars");
        stackable = getInteger(item, "stackable") == 1;
        glowing = getInteger(item, "glowing") == 1;
        reforgeable = getInteger(item, "reforgeable") == 1;
        recombobulated = getInteger(item, "recombobulated") == 1;
        hexColor = getString(item, "color");
        type = ItemType.valueOf(getString(item, "type"));
        enchants.putAll(getEnchants(item));
        for(SBPlayer.PlayerStat stat : SBPlayer.PlayerStat.values()) {
            int extraStats = 0;
            if (!enchants.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    Enchantment enchant = entry.getKey();
                    Integer lvl = entry.getValue();
                }
            }
            if (getStat(item, stat) != null) {
                stats.put(stat, getStat(item, stat) + extraStats);
            }
        }
        description.addAll(getDescription(item));
        IntStream.range(0, 5).filter(i -> !getAbilityString(item, i, AbilityValue.NAME.name()).equals("")).forEach(i -> abilities.put(i, new Ability(this, i)));
    }

    public SBItemBuilder() {

    }

    public SBItemBuilder material(Material var) {
        this.mat = var;
        return this;
    }

    public SBItemBuilder name(String var) {
        this.name = var;
        return this;
    }

    public SBItemBuilder id(String var) {
        this.id = var;
        return this;
    }

    public SBItemBuilder rarity(Rarity var) {
        this.rarity = var;
        return this;
    }

    public SBItemBuilder type(ItemType var) {
        this.type = var;
        return this;
    }

    public SBItemBuilder url(String var) {
        this.url = var;
        return this;
    }

    public SBItemBuilder texture(String var) {
        this.texture = var;
        return this;
    }

    public SBItemBuilder normalStar(int star) {
        this.normalstar = star;
        return this;
    }

    public SBItemBuilder masterStar(int star) {
        this.masterstar = star;
        return this;
    }

    public SBItemBuilder stackable(boolean var) {
        this.stackable = var;
        return this;
    }

    public SBItemBuilder glowing(boolean var) {
        this.glowing = var;
        return this;
    }

    public SBItemBuilder reforgeable(boolean var) {
        this.reforgeable = var;
        return this;
    }

    public SBItemBuilder stat(SBPlayer.PlayerStat stat, double amount) {
        stats.put(stat, amount);
        return this;
    }

    public SBItemBuilder ability(int index, Ability ability) {
        abilities.put(index, ability);
        return this;
    }

    public SBItemBuilder setDescriptionLine(int index, String line) {
        description.set(index, line);
        return this;
    }

    public SBItemBuilder addDescriptionLine(String line) {
        description.add(line);
        return this;
    }

    public SBItemBuilder addEnchantment(Enchantment enchant, int lvl) {
        enchants.put(enchant, lvl);
        return this;
    }

    public SBItemBuilder removeEnchantment(Enchantment enchant) {
        enchants.remove(enchant);
        return this;
    }

    public SBItemBuilder recomb(boolean var) {
        recombobulated = var;
        return this;
    }

    public SBItemBuilder flag(ItemFlag flag) {
        flags.add(flag);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(mat, 1, (short) 0);
        if (stack != null) {
            item = stack;
        }
        if(item.getType().equals(Material.SKULL_ITEM)) {
            if (texture != null) {
                item = applyTexture(item, texture);
            } else if(url != null) {
                item = applyUrl(item, url);
            }
        }
        ItemMeta meta = item.getItemMeta();
        if (!flags.isEmpty()) {
            for (ItemFlag flag : flags) {
                meta.addItemFlags(flag);
            }
        }
        if(glowing) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.LUCK, 100, true);
        }
        StringBuilder petLvl = new StringBuilder();
        if (Boolean.parseBoolean(NBTUtil.getString(item, "is-pet"))) {
            petLvl.append(SUtil.colorize("&7[Lvl"));
            petLvl.append(NBTUtil.getInteger(item, "pet-level"));
            petLvl.append("] ");
        }
        StringBuilder stars = new StringBuilder();
        for (int b = 0; b < normalstar; b++) {
            if (masterstar >= b + 1) {
                stars.append("§c✪");
            } else {
                stars.append("§6✪");
            }
        }

        String name = petLvl + rarity.getColor().toString() + this.name + (stars.toString().equals("") ? "" : " " + stars);
        meta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        int i = 0;
        if (!NBTUtil.getString(item, "pet-type").equals("")) {
            lore.add(SUtil.colorize("&8" + NBTUtil.getString(item, "pet-type")));
            lore.add("");
        }
        String prevS = "";
        if (!stats.isEmpty()) {
            for (String s : statsformat) {
                i++;
                if (s.equals("blank")) {
                    if (!prevS.equals("")) {
                        lore.add("");
                    }
                } else {
                    SBPlayer.PlayerStat stat = Enums.getIfPresent(SBPlayer.PlayerStat.class, s.toUpperCase()).orNull();
                    if (!stats.containsKey(stat)) continue;
                    double a = stats.get(stat);
                    String statString = "";
                    if ((a % 2) == 0) {
                        statString = String.valueOf(Math.toIntExact((long) a));
                    } else {
                        statString = String.valueOf(a);
                    }
                    if (a != 0) {
                        prevS = s;
                        s = s.replace('_', ' ');
                        if (stat != null) {
                            if (stat.equals(GEAR_SCORE)) {
                                lore.add(ChatColor.GRAY + s + ":" + stat.getColor() + " " + Math.toIntExact((long) a));
                            } else {
                                lore.add(ChatColor.GRAY + s + ":" + stat.getColor() + " +" + statString);
                            }
                        }
                    }
                }
            }
            if (!prevS.equals("")) {
                lore.add("");
            }
        }
        if(!enchants.isEmpty()) {
            for (Map<Enchantment, Integer> enchantmap : split(enchants)) {
                StringBuilder builder = new StringBuilder();
                for (Map.Entry<Enchantment, Integer> entry : enchantmap.entrySet()) {
                    Enchantment enchant = entry.getKey();
                    Integer value = entry.getValue();
                    if (enchant.isUltimate()) {
                        builder.append("&b&l").append(enchant.getName()).append(" ").append(RomanNumber.toRoman(value));
                        if (!(enchantmap.keySet().toArray()[enchantmap.size() - 1] == enchant)) {
                            builder.append(",");
                        }
                    builder.append(" ");
                    } else {
                        builder.append("&9").append(enchant.getName()).append(" ").append(RomanNumber.toRoman(value));
                        if (!(enchantmap.keySet().toArray()[enchantmap.size() - 1] == enchant)) {
                            builder.append(",");
                        }
                        builder.append(" ");
                    }
                }
                lore.add(builder.toString());
            }
            lore.add("");
        }

        if(!description.isEmpty()) {
            lore.addAll(description);
            lore.add("");
        }

        if(!abilities.isEmpty()) {
            for (Map.Entry<Integer, Ability> entry : abilities.entrySet()) {
                Ability ability = entry.getValue();
                if(ability.functions.containsValue(AbilityValue.FunctionType.SHORTBOW)) {
                    lore.add("&5Shortbow: " + ability.name);
                } else {
                  lore.add("&6Ability: " + ability.name + "&e&l " + ability.clickType.name().replace("_", " ") + " CLICK");
                }
                lore.addAll(ability.description);
                if(ability.manaCost != 0) {
                    lore.add("&7Mana Cost: &3" + ability.manaCost);
                }
            }
            lore.add("");
        }

        if (reforgeable) {
            lore.add("&8This item can be reforged!");
        }
        if (recombobulated) {
            String recombsymbol = rarity.getColor() + "" + ChatColor.MAGIC + "L" + ChatColor.stripColor("") + rarity.getColor() + "" + ChatColor.BOLD;
            lore.add(recombsymbol + " " + rarity.name() + " " + type.getValue() + " " + recombsymbol);
        } else {
            lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.getValue());
        }
        meta.setLore(SUtil.colorize(lore));
        item.setItemMeta(meta);
        item = setNBT(item);

        return item;
    }

    public ItemStack setNBT(ItemStack item) {
        item = setString(item, "true", "non-legacy");
        item = setString(item, id, "ID");
        item = setString(item, rarity.name(), "RARITY");
        item = setString(item, name, "name");
        item = setString(item, type.name(), "type");
        item = setInteger(item, masterstar, "masterStars");
        item = setInteger(item, normalstar, "normalStars");
        item = setInteger(item, stackable ? 1 : 0, "stackable");
        item = setInteger(item, glowing ? 1 : 0, "glowing");
        item = setInteger(item, reforgeable ? 1 : 0, "reforgeable");
        item = setInteger(item, recombobulated ? 1 : 0, "recombobulated");
        item = setString(item, hexColor, "color");

        if(!stackable) {
            item = setString(item, UUID.randomUUID().toString(), "uuid");
        }

        if(!stats.isEmpty()) {
            for (Map.Entry<SBPlayer.PlayerStat, Double> entry : stats.entrySet()) {
                SBPlayer.PlayerStat stat = entry.getKey();
                Double value = entry.getValue();
                item = setStat(item, value, stat);
            }
        }

        if(!description.isEmpty()) {
            for (int i = 0; i < description.size(); i++) {
                item = setDescription(item, i, description.get(i));
            }
        }

        if(!enchants.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                Enchantment enchant = entry.getKey();
                Integer value = entry.getValue();
                item = setEnchant(item ,enchant, value);
            }
        }

        for (Ability ability : abilities.values()) {
            ability.build();
        }

        return item;
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

    private List<Map<Enchantment, Integer>> split(Map<Enchantment, Integer> original) {

        int max = 3;
        int counter = 0;
        int lcounter = 0;
        List<Map<Enchantment, Integer>> listOfSplitMaps = new ArrayList<Map<Enchantment, Integer>> ();
        Map<Enchantment, Integer> splitMap = new HashMap<> ();

        for (Map.Entry<Enchantment, Integer> m : original.entrySet()) {
            if (counter < max) {
                splitMap.put(m.getKey(), m.getValue());
                counter++;
                lcounter++;

                if (counter == max || lcounter == original.size()) {
                    counter = 0;
                    listOfSplitMaps.add(splitMap);
                    splitMap = new HashMap<> ();
                }
            }
        }

        return listOfSplitMaps;
    }

}
