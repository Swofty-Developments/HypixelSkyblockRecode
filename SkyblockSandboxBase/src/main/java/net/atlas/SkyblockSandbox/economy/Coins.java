package net.atlas.SkyblockSandbox.economy;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.CustomCoinEvent;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Coins {
    public HashMap<UUID,Double> coins = new HashMap<>();

    public void addCoins(Player p,Double d,CoinEvent e) {
        if(coins.containsKey(p.getUniqueId())) {
            coins.put(p.getUniqueId(),coins.get(p.getUniqueId())+d);
            Bukkit.getPluginManager().callEvent(new CustomCoinEvent(p,e));
        } else {
            coins.put(p.getUniqueId(),d);
        }
    }

    public void setCoins(Player p,Double d,CoinEvent e) {
        Bukkit.getPluginManager().callEvent(new CustomCoinEvent(p,e));
        coins.put(p.getUniqueId(),d);
    }

    public void removeCoins(Player p,Double d,CoinEvent e) {
        try {
            coins.put(p.getUniqueId(),coins.get(p.getUniqueId())-d);
            Bukkit.getPluginManager().callEvent(new CustomCoinEvent(p,e));
        } catch (NullPointerException ignored) {
            System.out.println("[ERROR] Error while setting " + p.getName() + " \n UUID: (" + p.getUniqueId() + ") coin value through: " + e.getName());
        }
    }

    public Double getCoins(Player p) {
        return coins.get(p.getUniqueId());
    }

    public void loadCoins(Player p) {
        MongoCoins mongoCoins = SBX.getMongoStats();
        coins.put(p.getUniqueId(), (Double) mongoCoins.getData(p.getUniqueId(),"coins"));
    }

    public void cacheCoins(Player p) {
        MongoCoins mongoCoins = SBX.getMongoStats();
        try {
            mongoCoins.setData(p.getUniqueId(),"coins",coins.get(p.getUniqueId()));
        } catch (Exception e) {
            System.out.println("[ERROR] Error while caching coins for: " + p.getName() + " \nUUID: (" + p.getUniqueId() + ") \nCoin value at last update: " + coins.get(p.getUniqueId()));
            e.printStackTrace();
        }

    }
}
