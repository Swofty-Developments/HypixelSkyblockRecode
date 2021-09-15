package net.atlas.SkyblockSandbox.item.enchant;

import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.TriFunction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum Enchantment {
    ANGLER("Angler", 5, ItemType.ITEM, (sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE) + (lvl));
        return 1;
    }),
    AQUA_AFFINITY("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    BANE_OF_ARTHROPODS("Bane of Arthropods", 6, ItemType.SWORD, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 8;
            return new Integer((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return 1;

    }),
    BIG_BRAIN("Big Brain", 5, ItemType.HELMET, (sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.INTELLIGENCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) + (5 * lvl));
        return 1;
    }),
    BLAST_PROTECTION("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    BLESSING("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    CASTER("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    CHANCE("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    CLEAVE("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    COMPACT("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    COUNTER_STRIKE("Counter-Strike", 3, ItemType.CHESPLATE, (event, dmg, lvl) ->{
        return 1;
    }),
    CRITICAL("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    CUBISM("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    CULTIVATING("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    DELICATE("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    DEPTH_STRIDER("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    DRAGON_HUNTER("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    DRAGON_TRACER("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Efficiency("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Ender_Slayer("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Execute("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Experience("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Expertise("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Feather_Falling("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Fire_Aspect("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Fire_Protection("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    First_Strike("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Flame("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Fortune("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Frail("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Frost_Walker("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Giant_Killer("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Growth("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Harvesting_Pristine("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Impaling("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Infinite_Quiver("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Knockback("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Lethality("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    LIFE_STEAL("Life Steal", 5, ItemType.SWORD, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) event.getDamager());
            double mult = (0.5 * lvl) / 100;
            double health = p.getStat(SBPlayer.PlayerStat.HEALTH) + (p.getMaxStat(SBPlayer.PlayerStat.HEALTH) * mult);
            if (!(health > p.getMaxStat(SBPlayer.PlayerStat.HEALTH))) {
                p.setStat(SBPlayer.PlayerStat.HEALTH, health);
            }
        }
        return 1;
    }),
    Looting("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Luck("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Luck_of_the_Sea("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Lure("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Mana_Steal("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Magnet("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Overload("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Piercing("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Power("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Projectile_Protection("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Prosecute("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Protection("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }), Punch("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Rainbow("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Rejuvenate("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Replenish("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Respiration("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Respite("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Scavenger("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    SHARPNESS("Sharpness", 7, ItemType.SWORD, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 5;
            return new Integer((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return 1;
    }),
    Silk_Touch("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Smelting_Touch("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    Smarty_Pants("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    SMITE("Smite", 7, ItemType.SWORD, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 8;
            return new Integer((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return 1;
    }),
    Snipe("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    SPIKED_HOOK("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    SUGAR_RUSH("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    SYPHON("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    TELEKINESIS("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    THORNS("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    THUNDERBOLT("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    THUNDERLORD("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    TITAN_KILLER("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    TRIPLE_STRIKE("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    TRUE_PROTECTION("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    VAMPIRISM("Vampirism", 6, ItemType.SWORD, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof LivingEntity) {
                if (dmg > ((LivingEntity) event.getEntity()).getHealth()) {
                    SBPlayer p = new SBPlayer((Player) event.getDamager());
                    int mult = lvl / 100;
                    p.setStat(SBPlayer.PlayerStat.HEALTH, p.getMaxStat(SBPlayer.PlayerStat.HEALTH) * mult);
                }
            }
        }
        return dmg;
    }),
    VENOMOUS("Aqua Affinity", 3, ItemType.HELMET, (sbPlayer, lvl) -> {
        return 1;
    }),
    VICIOUS("Vicious", 5, ItemType.SWORD, (sbPlayer, lvl) -> lvl);

    private String name;
    private int maxLvl;
    private ItemType type;
    private TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action2;
    private BiFunction<SBPlayer, Integer, Integer> action3;


    Enchantment(String name, int maxLvl, ItemType type, TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action2 = action;
    }

    Enchantment(String name, int maxLvl, ItemType type, BiFunction<SBPlayer, Integer, Integer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action3 = action;
    }

    public ItemType getItemType() {
        return type;
    }

    public TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> getDamageAction() {
        return action2;
    }

    public BiFunction<SBPlayer, Integer, Integer> getHeldAction() {
        return action3;
    }
}
