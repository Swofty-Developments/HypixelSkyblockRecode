package net.atlas.SkyblockSandbox.event;

import net.atlas.SkyblockSandbox.economy.CoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomCoinEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled = false;
    private final Player player;
    private final CoinEvent coinCause;

    public CustomCoinEvent(Player player, CoinEvent coinCause) {
        this.coinCause = coinCause;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public CoinEvent getCoinCause() {
        return coinCause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
