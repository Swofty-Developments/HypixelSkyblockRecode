package net.atlas.SkyblockSandbox.player.skills;

public enum SkillType {

    COMBAT("Combat",0,60);

    private int minLvl;
    private int maxLvl;
    private String name;

    SkillType(String name,int minLvl,int maxLvl) {
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
        this.name = name;
    }

    public int getTotalXP() {
        return 55432357;
    }

    public String getName() {
        return name;
    }
}
