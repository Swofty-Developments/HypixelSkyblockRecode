package net.atlas.SkyblockSandbox.playerIsland;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public enum SBLocations {
    HUB(new Location(Bukkit.getWorld("Skyblock_Hub"), -2, 70, -70)),
    END(new Location(Bukkit.getWorld("Skyblock_Hub"), -499, 100, -276));


    private Location loc;

    SBLocations(Location loc) {
        this.loc = loc;
    }

    public void setWorld(World world) {
        loc = new Location(world, loc.getX(), loc.getY(), loc.getZ());
    }

    public void teleport(Player p) {
        p.teleport(loc);
    }
}
