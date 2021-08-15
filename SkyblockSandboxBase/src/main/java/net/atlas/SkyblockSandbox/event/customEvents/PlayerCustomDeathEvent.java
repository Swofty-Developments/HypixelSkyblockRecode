package net.atlas.SkyblockSandbox.event.customEvents;

import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerCustomDeathEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled = false;
    private final Player player;
    private final EntityDamageEvent.DamageCause deathCause;
    private final SBPlayer p;

    public PlayerCustomDeathEvent(Player player, SBPlayer p, EntityDamageEvent.DamageCause deathCause) {
        this.deathCause = deathCause;
        this.p = p;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityDamageEvent.DamageCause getDeathCause() {
        return deathCause;
    }

    public SBPlayer getSBPlayer() {
        return p;
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
