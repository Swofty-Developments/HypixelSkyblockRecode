package net.atlas.SkyblockSandbox.item.enchant;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.economy.CoinEvent;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.TriFunction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum Enchantment {
    ANGLER("Angler", 5, ItemType.ITEM,false, (sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE) + (lvl));
        return 1;
    }),
    AQUA_AFFINITY("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    BANE_OF_ARTHROPODS("Bane of Arthropods", 6, ItemType.SWORD, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 8;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return dmg;

    }),
    BIG_BRAIN("Big Brain", 5, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.INTELLIGENCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) + (5 * lvl));
        return 1;
    }),
    BLAST_PROTECTION("Blast Protection", 3, ItemType.ARMOR, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    BLESSING("Blessing", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    CASTER("Caster", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    CHANCE("Chance", 3, ItemType.SWORD, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    CLEAVE("Cleave", 3, ItemType.SWORD, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    COMPACT("Compact", 3, ItemType.PICKAXE, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    COUNTER_STRIKE("Counter-Strike", 3, ItemType.CHESTPLATE,false, (event, dmg, lvl) ->{
        return 1;
    }),
    CRITICAL("Critical", 3, ItemType.SWORD, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    CUBISM("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    CULTIVATING("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    DELICATE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    DEPTH_STRIDER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    DRAGON_HUNTER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    DRAGON_TRACER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    EFFICIENCY("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    ENDER_SLAYER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    EXECUTE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    EXPERIENCE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    EXPERTISE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FEATHER_FALLING("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FIRE_ASPECT("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FIRE_PROTECTION("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FIRST_STRIKE("First Strike", 5, ItemType.SWORD, false, (event,dmg,lvl) -> {
        if(event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer(((Player) event.getDamager()));
            double mult = (lvl*0.25);
            int timeshit = 0;
            LivingEntity en = (LivingEntity) event.getEntity();
            if(en.getMetadata("times-hit").size()>=1) {
                timeshit = en.getMetadata("times-hit").get(0).asInt();
            }
            if(timeshit==0) {
                return Integer.valueOf((int) (dmg *mult));
            }
        }
        return dmg;
    }),
    FLAME("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FORTUNE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FRAIL("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    FROST_WALKER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    GIANT_KILLER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    GROWTH("Aqua Affinity", 7, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    HARVESTING("Harvesting",6,ItemType.HOE,false,(sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.FARMING_FORTUNE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.FARMING_FORTUNE) + (lvl*12.5));
        return 1;
    }),
    PRISTINE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    IMPALING("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    INFINITE_QUIVER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    KNOCKBACK("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Lethality("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    LIFE_STEAL("Life Steal", 5, ItemType.SWORD, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) event.getDamager());
            double mult = (0.5 * lvl) / 100;
            double health = p.getStat(SBPlayer.PlayerStat.HEALTH) + (p.getMaxStat(SBPlayer.PlayerStat.HEALTH) * mult);
            if (!(health > p.getMaxStat(SBPlayer.PlayerStat.HEALTH))) {
                p.setStat(SBPlayer.PlayerStat.HEALTH, health);
            }
        }
        return dmg;
    }),
    Looting("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Luck("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Luck_of_the_Sea("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Lure("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    MANA_STEAL("Mana Steal", 3, ItemType.SWORD, false, (event,dmg,lvl) -> {
        if (event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) event.getDamager());
            double mult = ((0.3 * lvl)-0.1) / 100;
            double intel = p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) + (p.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) * mult);
            if (!(intel > p.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE))) {
                p.setStat(SBPlayer.PlayerStat.INTELLIGENCE, intel);
            }
        }
        return dmg;
    }),
    Magnet("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Overload("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Piercing("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Power("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Projectile_Protection("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Prosecute("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Protection("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }), Punch("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Rainbow("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Rejuvenate("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Replenish("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Respiration("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Respite("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    SCAVENGER("Scavenger", 3, ItemType.SWORD, false, (event,dmg,lvl) -> {
        if (event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) event.getDamager());
            if(((LivingEntity)event.getEntity()).getHealth()<=dmg) {
                double mult = ((0.3 * lvl)*50);
                Coins coins = SBX.getInstance().coins;
                coins.addCoins(p.getPlayer(),mult, CoinEvent.MOB);
            }
        }
        return dmg;
    }),
    SHARPNESS("Sharpness", 7, ItemType.SWORD, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 5;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return dmg;
    }),
    Silk_Touch("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    Smelting_Touch("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    SMARTY_PANTS("Smarty Pants", 3, ItemType.LEGGINGS, false, (sbPlayer, lvl) -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.INTELLIGENCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) + (5 * lvl));
        return 1;
    }),
    SMITE("Smite", 7, ItemType.SWORD, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 8;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return 1;
    }),
    Snipe("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    SPIKED_HOOK("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    SUGAR_RUSH("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    SYPHON("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    TELEKINESIS("Telekinesis", 3, ItemType.ITEM, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    THORNS("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    THUNDERBOLT("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    THUNDERLORD("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    TITAN_KILLER("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    TRIPLE_STRIKE("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    TRUE_PROTECTION("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    VAMPIRISM("Vampirism", 6, ItemType.SWORD, false, (event, dmg, lvl) -> {
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
    VENOMOUS("Aqua Affinity", 3, ItemType.HELMET, false, (sbPlayer, lvl) -> {
        return 1;
    }),
    VICIOUS("Vicious", 5, ItemType.SWORD, false, (sbPlayer, lvl) -> lvl);

    private String name;
    private int maxLvl;
    private ItemType type;
    private TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action2;
    private BiFunction<SBPlayer, Integer, Integer> action3;
    private boolean isUlt;


    Enchantment(String name, int maxLvl, ItemType type,boolean isUlt, TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action2 = action;
        this.isUlt = isUlt;
    }

    Enchantment(String name, int maxLvl, ItemType type,boolean isUlt, BiFunction<SBPlayer, Integer, Integer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action3 = action;
        this.isUlt = isUlt;
    }

    public String getName() {
        return name;
    }

    public boolean isUltimate() {
        return isUlt;
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
