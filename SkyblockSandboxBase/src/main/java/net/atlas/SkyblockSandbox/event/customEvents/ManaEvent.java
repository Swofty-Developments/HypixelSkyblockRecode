package net.atlas.SkyblockSandbox.event.customEvents;

import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ManaEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled = false;
    private final ManaCause manaCause;
    private final SBPlayer p;
    private final int manaCost;
    private final String abilityName;

    public ManaEvent(SBPlayer p, ManaCause manaCause, int manaCost, String abilityName) {
        this.manaCause = manaCause;
        this.p = p;
        this.manaCost = manaCost;
        this.abilityName = abilityName;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public int getManaCost() {
        return manaCost;
    }

    public ManaCause getManaCause() {
        return manaCause;
    }

    public SBPlayer getPlayer() {
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

    public enum ManaCause {
        ABILITY(), CUSTOM();
    }
}
