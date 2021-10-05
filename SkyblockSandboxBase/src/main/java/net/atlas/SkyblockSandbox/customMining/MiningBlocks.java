package net.atlas.SkyblockSandbox.customMining;

import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public enum MiningBlocks {
    STONE("Stone",Material.STONE,1,15,(byte) 0),
    COBBLESTONE("Cobblestone",Material.COBBLESTONE,1,20,(byte) 0),
    IRON_ORE("Iron Ore",Material.IRON_ORE,5,60,(byte) 0),
    GOLD_ORE("Gold Ore",Material.GOLD_ORE,6,60,(byte) 0),
    EMERALD_ORE("Emerald Ore",Material.EMERALD_ORE,9,60,(byte) 0),
    COAL_ORE("Coal Ore",Material.COAL_ORE,5,60,(byte) 0),
    DIAMOND_ORE("Diamond Ore",Material.DIAMOND_ORE,10,60,(byte) 0),
    ENDSTONE("Endstone",Material.ENDER_STONE,3,60,(byte) 0),
    DIAMOND_BLOCK("Diamond Block",Material.DIAMOND_BLOCK,15,100,(byte) 0),
    OBSIDIAN("Obsidian",Material.OBSIDIAN,20,1000,(byte) 0),
    HARD_STONE("Hard Stone",Material.STONE,1,50,(byte) 1),
    OUTER_MITHRIL_ORE("Outer Mithril Ore",Material.WOOL,45,500, DyeColor.GRAY.getData()),
    INNER_MITHRIL_ORE("Inner Mithril Ore",Material.PRISMARINE,45,800,(byte) 0),
    CENTER_MITHRIL_ORE("Center Mithril Ore",Material.WOOL,45,1500,DyeColor.LIGHT_BLUE.getData()),
    TITANIUM_ORE("Titanium Ore",Material.STONE,100,2000,(byte) 4),
    RUBY_GEMSTONE("Ruby Gemstone",Material.STAINED_GLASS,80,2500,(byte) 14),
    TOPAZ_GEMSTONE("Topaz Gemstone",Material.STAINED_GLASS,80,4000,(byte) 4),
    JASPER_GEMSTONE("Jasper Gemstone",Material.STAINED_GLASS,80,5000,(byte) 2),
    OAK_LOG_1("Oak Log 1",Material.LOG,8,SkillType.FORAGING,80,(byte) 0),
    OAK_LOG_2("Oak Log 2",Material.LOG,8,SkillType.FORAGING,80,(byte) 4),
    OAK_LOG_3("Oak Log 3",Material.LOG,8,SkillType.FORAGING,80,(byte) 8),
    OAK_LOG_4("Oak Log 4",Material.LOG,8,SkillType.FORAGING,80,(byte) 12);



    private final String name;
    private final Material mat;
    private final int hp;
    private final byte damage;
    private final double xpAmt;
    private final SkillType skillType;

    MiningBlocks(String name, Material mat, double xpAmt, SkillType skillType, int hp, byte damage) {
        this.name = name;
        this.mat = mat;
        this.hp = hp;
        this.damage = damage;
        this.xpAmt = xpAmt;
        this.skillType = skillType;
    }

    MiningBlocks(String name, Material mat, double xpAmt, int hp, byte damage) {
        this.name = name;
        this.mat = mat;
        this.hp = hp;
        this.damage = damage;
        this.xpAmt = xpAmt;
        this.skillType = SkillType.MINING;
    }

    public byte getDamage() {
        return damage;
    }

    public int getBlockHP() {
        return hp;
    }

    public Material getMaterial() {
        return mat;
    }

    public double getXpAmt() {
        return xpAmt;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public static MiningBlock toMiningBlock(Block b) {
        MiningBlock bl = new MiningBlock(b);
        bl.setBlockHP(0);
        if(isMineable(b)) {
            for(MiningBlocks blc:MiningBlocks.values()) {
                if (b.getType().equals(blc.getMaterial())) {
                    if(b.getData()==blc.getDamage()) {
                        bl.setBlockHP(blc.getBlockHP());
                        bl.setXPAmt(blc.getXpAmt());
                        bl.setXPType(blc.getSkillType());
                    }
                }
            }
        }
        return bl;
    }

    public String getName() {
        return name;
    }

    public static boolean isMineable(Block b) {
        for(MiningBlocks bl:MiningBlocks.values()) {
            if (b.getType().equals(bl.getMaterial())) {
                if(b.getData()==bl.getDamage()) {
                    return true;
                }
            }
        }
        return false;
    }
}
