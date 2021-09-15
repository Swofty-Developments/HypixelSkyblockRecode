package net.atlas.SkyblockSandbox.database.sql;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

public class BackpackData {


    public static void save() {
        FileConfiguration config = SBX.getInstance().getConfig();
        boolean redisConnect = config.getBoolean("Redis.use-redis");
        if(!redisConnect) {
            Bukkit.broadcastMessage(ChatColor.RED + "Backpacks are currently disabled!");
        } else {
            String host = config.getString("Redis.redishost");
            int port = config.getInt("Redis.redisport");
            String password = config.getString("Redis.redispass");

            Jedis j = new Jedis(host, port);
            if (!(password.equals(""))) {
                j.auth(password);
            }

            Set<String> keys = j.hkeys("Backpack");
            SQLBpCache sqlBpCache = new SQLBpCache(SBX.getInstance());
            for(String key:keys) {
                String type = j.type(key);
                System.out.println(key);
                if(type != null) {
                    String serializedBP = j.hget("Backpack",key);
                    sqlBpCache.setBackpack(key,serializedBP);
                }
            }
            j.close();
        }
    }

    public static void restore() {
        FileConfiguration config = SBX.getInstance().getConfig();
        boolean redisConnect = config.getBoolean("Redis.use-redis");
        if(!redisConnect) {
            Bukkit.broadcastMessage(ChatColor.RED + "Backpacks are currently disabled!");
        } else {
            String host = config.getString("Redis.redishost");
            int port = config.getInt("Redis.redisport");
            String password = config.getString("Redis.redispass");

            Jedis j = new Jedis(host, port);
            if (!(password.equals(""))) {
                j.auth(password);
            }
            SQLBpCache sqlBpCache = new SQLBpCache(SBX.getInstance());
            Map<String, String> backPacks = sqlBpCache.getBackpack();
            for (Map.Entry<String, String> stringEntry : backPacks.entrySet()) {
                j.set(stringEntry.getKey(), stringEntry.getValue());
            }
        }

    }
}
