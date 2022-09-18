package net.atlas.SkyblockSandbox.entitySpawner;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Random;

public class DefaultSpawnerObject implements Spawner {
    private int task;
    private boolean running = false;

    private Location spawnerLocation;
    private EntityType entityType;
    private int range;
    private int delay;
    private int spawnCount;
    private String[] keywords;
    private int health;
    private String displayName;

    @Override
    public Spawner forceSpawn() {
        return this;
    }

    @Override
    public Spawner setSpawnCount(int count) {
        this.spawnCount = count;
        return this;
    }

    @Override
    public int getSpawnCount() {
        return spawnCount;
    }

    @Override
    public Spawner setSpawnRange(int range) {
        this.range = range;
        return this;
    }

    @Override
    public int getSpawnRange() {
        return range;
    }

    @Override
    public Spawner setSpawnDelay(int delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public Spawner setHealth(int health) {
        this.health = health;
        return this;
    }

    @Override
    public int getSpawnDelay() {
        return delay;
    }

    @Override
    public Spawner setSpawnKeywords(String[] keywords) {
        this.keywords = keywords;
        return this;
    }

    @Override
    public String[] getSpawnKeywords() {
        return keywords;
    }

    @Override
    public Spawner setSpawnerType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    @Override
    public Spawner start() {
        running = true;
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(SBX.getInstance(), () -> {
            Collection<Entity> entities = spawnerLocation.getWorld().getNearbyEntities(spawnerLocation, range, range, range);
            if (entities.size() == 0) {
                for (int i = 0; i < spawnCount; i++) {
                    Entity en = spawnerLocation.getWorld().spawnEntity(utilGetRandoLoc(), entityType);
                    en.setCustomName(this.displayName);
                    ((LivingEntity) en).setMaxHealth(this.health);
                    ((LivingEntity) en).setHealth(this.health);
                }
            }
        }, 10, delay);

        return this;
    }

    @Override
    public int stop() {
        Bukkit.getScheduler().cancelTask(task);
        running = false;
        return task;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Spawner setSpawnerLocation(Location location) {
        this.spawnerLocation = location;
        return this;
    }

    @Override
    public Spawner setDisplayname(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public String getDisplayname() {
        return displayName;
    }

    private Location utilGetRandoLoc() {
        int randox = new Random().nextInt(range);
        int randoz = new Random().nextInt(range);

        Location selected = new Location(spawnerLocation.getWorld(), spawnerLocation.getX() + randox, spawnerLocation.getY(), spawnerLocation.getZ() + randoz);

        if (selected.getBlock().getType().isSolid()) {
            return utilGetRandoLoc();
        }

        return selected;
    }
}
