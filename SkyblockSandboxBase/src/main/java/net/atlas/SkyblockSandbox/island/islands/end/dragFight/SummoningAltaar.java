package net.atlas.SkyblockSandbox.island.islands.end.dragFight;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum SummoningAltaar {
    FRONT_LEFT(new Location(Bukkit.getWorlds().get(0), -669, 9, -275)),
    FRONT_RIGHT(new Location(Bukkit.getWorlds().get(0), -669, 9, -277)),
    FRONT_MIDDLE_LEFT(new Location(Bukkit.getWorlds().get(0), -670, 9, -274)),
    FRONT_MIDDLE_RIGHT(new Location(Bukkit.getWorlds().get(0), -670, 9, -278)),
    BACK_MIDDLE_LEFT(new Location(Bukkit.getWorlds().get(0), -672, 9, -274)),
    BACK_MIDDLE_RIGHT(new Location(Bukkit.getWorlds().get(0), -672, 9, -278)),
    BACK_LEFT(new Location(Bukkit.getWorlds().get(0), -673, 9, -275)),
    BACK_RIGHT(new Location(Bukkit.getWorlds().get(0), -673, 9, -277));

    private Location location;

    SummoningAltaar(Location location) {
        File f = new File(SBX.getInstance().getDataFolder() + "/dragon_data.yml");
        FileConfiguration fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(f);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        this.location = location;
    }

    public Location getLoc() {
        return location;
    }

    public void setWorld(World world) {
        location = new Location(world,location.getX(),location.getY(),location.getZ());
    }


}
