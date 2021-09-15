package net.atlas.SkyblockSandbox.playerIsland;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum SBLocations {
    HUB(new Location(Bukkit.getWorld("Skyblock_Hub"),-2,70,-70));


    private Location loc;

    SBLocations(Location loc) {
        this.loc = loc;
    }

    public void teleport(Player p) {
        p.teleport(loc);
    }
}
