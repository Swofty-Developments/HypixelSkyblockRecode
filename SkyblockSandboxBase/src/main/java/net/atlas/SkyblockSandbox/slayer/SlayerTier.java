package net.atlas.SkyblockSandbox.slayer;

public enum SlayerTier {
    ONE(1,150), TWO(2,1200), THREE(3,2400), FOUR(4,4800), FIVE(5,9600);

    public int num;
    private double xpAmt;

    SlayerTier(int num,int xpAmt) {
        this.num =num;
        this.xpAmt = xpAmt;
    }

    public int toInteger() {
        return num;
    }

    public double getXpAmt() {
        return xpAmt;
    }

    public static SlayerTier fromInt(int num) {
        switch (num) {
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            default:
                return ONE;
        }
    }
}
