package net.atlas.SkyblockSandbox.util.builders;

import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SBItemBuilder {

    private Material mat;
    private String name;
    private String id;
    private Rarity rarity;
    private ItemType type;
    private String url;
    private String texture;
    private boolean stackable;
    private ArrayList<String> description = new ArrayList<>();
    private String hexColor = "";
    private HashMap<SBPlayer.PlayerStat, Double> stats = new HashMap<>();
    private HashMap<Integer, Ability> abilities = new HashMap<>();

    public SBItemBuilder() {
    }

    public static SBItemBuilder init() {
        return new SBItemBuilder();
    }

    public SBItemBuilder mat(Material mat) {
        this.mat = mat;
        return this;
    }

    public SBItemBuilder type(ItemType type) {
        this.type = type;
        return this;
    }

    public SBItemBuilder stat(SBPlayer.PlayerStat stat, Double val) {
        stats.put(stat, val);
        return this;
    }

    public SBItemBuilder url(String url) {
        this.url = url;
        return this;
    }

    public SBItemBuilder texture(String texture) {
        this.texture = texture;
        return this;
    }

    public SBItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SBItemBuilder stackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    public SBItemBuilder id(String id) {
        this.id = id;
        return this;
    }


    public SBItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public SBItemBuilder hexColor(String hexColor) {
        this.hexColor = hexColor;
        return this;
    }

    public SBItemBuilder ability(Ability abil, int index) {
        abilities.put(index, abil);
        return this;
    }

    public SBItemBuilder description(String desc) {
        description.add(desc);
        return this;
    }

    public SBItemStack build() {
        SBItemStack item = new SBItemStack(name, id, mat, rarity, type, 0, stackable, true);
        if (url != null) {
            if (!url.equals("") && mat.equals(Material.SKULL_ITEM)) {
                item = new SBItemStack(name, id, mat, rarity, type, url, 3, stackable, true, stats);
            }
        } else if (texture != null) {
            if (!texture.equals("") && mat.equals(Material.SKULL_ITEM)) {
                item = new SBItemStack(name, id, mat, rarity, type, texture, 3, stackable, true, stats);
            }
        }
        if (!abilities.isEmpty()) {
            for (int i : abilities.keySet()) {
                item.setAbility(abilities.get(i), i);
            }
        }
        if(!stats.isEmpty()) {
            for (Map.Entry<SBPlayer.PlayerStat, Double> entry : stats.entrySet()) {
                SBPlayer.PlayerStat stat = entry.getKey();
                Double value = entry.getValue();
                item.setStat(item, stat, value);
            }
        }
        if (!description.isEmpty()) {
            for (int i = 0; i < description.size(); i++) {
                item.addDescriptionLine(description.get(i), i);
            }
        }
        if (hexColor != null && !hexColor.isEmpty()) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.asBukkitItem().getItemMeta();
            meta.setColor(SUtil.hexToRgb("#" + hexColor));
            ItemStack temp = item.asBukkitItem();
            temp.setItemMeta(meta);
            item = new SBItemStack(temp);
        }
        return item;
    }
}
