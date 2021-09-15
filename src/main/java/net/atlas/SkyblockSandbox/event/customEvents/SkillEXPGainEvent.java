package net.atlas.SkyblockSandbox.event.customEvents;

import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SkillEXPGainEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled = false;

    private final SkillType type;
    private final double expAmt;
    private final SBPlayer p;
    private final EntityDamageByEntityEvent e;

    public SkillEXPGainEvent(SBPlayer p, SkillType type, double expAmt, EntityDamageByEntityEvent e) {
        this.type = type;
        this.expAmt = expAmt;
        this.p = p;
        this.e = e;
    }

    public SkillEXPGainEvent(SBPlayer p, SkillType type, double expAmt) {
        this.type = type;
        this.expAmt = expAmt;
        this.p = p;
        this.e = null;
    }

    public double getExpAmt() {
        return expAmt;
    }

    public SkillType getSkill() {
        return type;
    }

    public SBPlayer getPlayer() {
        return p;
    }

    public EntityDamageByEntityEvent getDamage() {
        return e;
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
