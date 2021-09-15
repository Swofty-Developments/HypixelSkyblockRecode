package net.atlas.SkyblockSandbox.item;
public enum BackpackSize {
    NONE(0,"none"),
    SMALL(9,"Small"),
    MEDIUM(18,"Medium"),
    LARGE(27,"Large"),
    GREATER(36,"Greater"),
    JUMBO(54,"Jumbo");

    private int guiSize;
    private String name;

    BackpackSize(int guiSize,String name) {
        this.guiSize = guiSize;
        this.name = name;
    }

    public int getGUISize() {
        return guiSize;
    }

    public String getName() {
        return name;
    }
}
