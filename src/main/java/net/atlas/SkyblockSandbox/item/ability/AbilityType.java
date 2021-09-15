package net.atlas.SkyblockSandbox.item.ability;

public enum AbilityType {
    LEFT_CLICK("LEFT CLICK"),
    RIGHT_CLICK("RIGHT CLICK"),
    MIDDLE_CLICK("MIDDLE CLICK"),
    SHIFT("SHIFT"),
    NONE("");

    private String text;

    AbilityType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
