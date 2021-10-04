package net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener.dragonDownMessage;
import static net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight.fightActive;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.worldEnderman;

public class EntityDeathEvent extends SkyblockListener<org.bukkit.event.entity.EntityDeathEvent> {

    public MongoCoins db;

    @EventHandler
    public void callEvent(org.bukkit.event.entity.EntityDeathEvent event) {

        if(event.getEntity() instanceof Enderman) {
            worldEnderman.remove((Enderman) event.getEntity());
        }
        if (holoMap.containsKey(event.getEntity())) {
            if (holoMap.get(event.getEntity()).getVehicle() != null) {
                holoMap.get(event.getEntity()).getVehicle().remove();
            }
            holoMap.get(event.getEntity()).remove();
        }
        if (!(event.getEntity() instanceof Player)) {
            if (event.getEntity().getKiller() != null) {
                SBPlayer pl = new SBPlayer(event.getEntity().getKiller());
                EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(event.getEntity().getKiller(),event.getEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK,1);
                SkillEXPGainEvent e = new SkillEXPGainEvent(pl, SkillType.COMBAT, 10,ev);
                Bukkit.getPluginManager().callEvent(e);
            }
        }
        if (event.getEntity() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) event.getEntity());
            Bukkit.getPluginManager().callEvent(new PlayerCustomDeathEvent((Player) event.getEntity(), p, EntityDamageEvent.DamageCause.CUSTOM));
        }

        if (event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            db = SBX.getMongoStats();
            event.setDroppedExp(0);

            fightActive = false;
            for (Player p : StartFight.playerDMG.keySet()) {
                db.setData(p.getUniqueId(), "dragon_kills", (Double) (db.getData(p.getUniqueId(), "dragon_kills")) + 1);
                db.setData(p.getUniqueId(), "damage_done", (Double) (db.getData(p.getUniqueId(), "damage_done")) + StartFight.playerDMG.get(p));
                db.setData(p.getUniqueId(), "times_fragged", db.getData(p.getUniqueId(), "times_fragged"));
                db.setData(p.getUniqueId(), "dragon_pets", db.getData(p.getUniqueId(), "dragon_pets"));

            }
            dragonDownMessage(StartFight.activeDrag, event.getEntity().getKiller(), event.getEntity().getLocation());
        }
        if(event.getEntity().hasMetadata("entity-tag")) {
            event.getDrops().clear();
        }
    }
}

