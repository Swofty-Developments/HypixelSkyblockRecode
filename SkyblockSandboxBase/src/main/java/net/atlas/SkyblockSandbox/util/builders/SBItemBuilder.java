package net.atlas.SkyblockSandbox.util.builders;

import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Objects;

public class SBItemBuilder {

    private Material mat;
    private String name;
    private String id;
    private Rarity rarity;
    private ItemType type;
    private String url;
    private boolean stackable;
    private HashMap<SBPlayer.PlayerStat,Double> stats = new HashMap<>();
    private HashMap<Integer,Ability> abilities = new HashMap<>();

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

    public SBItemBuilder stat(SBPlayer.PlayerStat stat,Double val) {
        stats.put(stat,val);
        return this;
    }
    public SBItemBuilder url(String url) {
        this.url = url;
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

    public SBItemBuilder ability(Ability abil,int index) {
        abilities.put(index,abil);
        return this;
    }

    public SBItemStack build() {
        if(url!=null) {
            if (!url.equals("") && mat.equals(Material.SKULL_ITEM)) {
                return new SBItemStack(name, id, mat, rarity, type, url, 3, stackable, true, stats);
            }
        }
        if(!abilities.isEmpty()) {
            SBItemStack item = new SBItemStack(name,id,mat,rarity,type,0,stackable,true,stats);
            for(int i:abilities.keySet()) {
                item.setAbility(abilities.get(i),i);
            }
            return item;
        }
        return new SBItemStack(name,id,mat,rarity,type,0,stackable,true,stats);
    }
}
