package net.atlas.SkyblockSandbox.economy;

public enum CoinEvent {
    MOB("mob_kill"),
    PICKUP("coin_pickup"),
    DEPOSIT("deposit_coin"),
    WITHDRAW("withdraw_coin"),
    SELL("sell_item"),
    ADMIN("coin_from_admin");

    private final String name;

    CoinEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
