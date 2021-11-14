package net.atlas.SkyblockSandbox.entitySpawner;

import org.bukkit.entity.EntityType;

public interface Spawner {
    Spawner forceSpawn();

    Spawner setSpawnCount(int count);

    int getSpawnCount();

    Spawner setSpawnRange(int range);

    int getSpawnRange();

    Spawner setSpawnDelay(int delay);

    Spawner setHealth(int health);

    int getSpawnDelay();

    Spawner setSpawnKeywords(String[] keywords);

    String[] getSpawnKeywords();

    Spawner setSpawnerType(EntityType entityType);

    Spawner start();

    int stop();

    boolean isRunning();

    Spawner setSpawnerLocation(org.bukkit.Location location);

    Spawner setDisplayname(String displayName);

    String getDisplayname();
}
