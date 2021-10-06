package net.atlas.SkyblockSandbox.player.skills;

public enum SkillType {

    FARMING("Farming",0,60),
    MINING("Mining",0,60),
    COMBAT("Combat", 0, 60),
    FORAGING("Foraging",0,50),
    FISHING("Fishing",0,50),
    ENCHANTING("Enchanting",0,60),
    ALCHEMY("Alchemy",0,60),
    CARPENTRY("Carpentry",0,50),
    RUNECRAFTING("Runecrafting",0,25),
    TAMING("Taming",0,50);

    private int minLvl;
    private int maxLvl;
    private String name;
    private static final int[] xpLvls = {
            50, 125, 200, 300, 500, 750, 1000, 1500,
            2000, 3500, 5000, 7500, 10000, 15000, 20000, 30000, 50000, 75000, 100000, 200000, 300000, 400000, 500000,
            600000, 700000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000, 1600000, 1700000, 1800000,
            1900000, 2000000, 2100000, 2200000, 2300000, 2400000, 2500000, 2600000, 2750000, 2900000, 3100000, 3400000,
            3700000, 4000000, 4300000, 4600000, 4900000, 5200000, 5500000, 5800000, 6100000, 6400000, 6700000, 7000000
    };

    SkillType(String name, int minLvl, int maxLvl) {
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
        this.name = name;
    }

    public static int getLvlXP(int lvl) {
        if(lvl>=60) {
            lvl = 59;
        }
        if(lvl<0) {
            lvl = 0;
        }
        return xpLvls[lvl];
    }


    public int getTotalXP() {
        return xpLvls[xpLvls.length-1];
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public int getMinLvl() {
        return minLvl;
    }

    public String getName() {
        return name;
    }
}
