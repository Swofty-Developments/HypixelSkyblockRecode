package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private static final Map<String, Long> cooldownMap = new HashMap<>();

    public static boolean abilityCooldown(String ability, Player player, int cooldown){
        String uuid = ability + "." + player.getUniqueId();
        if(cooldownMap.containsKey(uuid)) {
            if(cooldownMap.get(uuid) > System.currentTimeMillis()) {
                return true;
            }
        }
        long time = (long) (cooldown * 1000.0) + System.currentTimeMillis();
        cooldownMap.put(uuid, time);
        return false;
    }
}
