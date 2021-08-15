package net.atlas.SkyblockSandbox.slayer;

import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.slayerBosses.EndSlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public enum Slayers {
    ZOMBIE(new EndSlayer(),"Zombie","Zombies"),
    SPIDER(new EndSlayer(),"Spider","Spiders"),
    WOLF(new EndSlayer(),"Wolf","Wolves"),
    ENDERMAN(new EndSlayer(),"Enderman","Endermen");

    private Slayer clazz;
    private String singular;
    private String plural;

    Slayers(Slayer clazz,String singular,String plural) {
        this.clazz = clazz;
        this.singular = singular;
        this.plural = plural;
    }

    public Slayer getSlayerClass() {
        return clazz;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public Entity spawn(SBPlayer p, SlayerTier tier, EntityDamageByEntityEvent e) {
        return clazz.spawnSlayer(tier,p,e.getEntity().getLocation());
    }

    public void spawn(SBPlayer p, SlayerTier tier, Location loc) {
        clazz.spawnSlayer(tier,p,loc);
    }
}
