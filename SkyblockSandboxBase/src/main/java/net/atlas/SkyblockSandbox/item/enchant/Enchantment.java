package net.atlas.SkyblockSandbox.item.enchant;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.economy.CoinEvent;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.TriFunction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.function.BiFunction;

import static net.atlas.SkyblockSandbox.item.ItemType.*;

public enum Enchantment {
    BANK(0, "Bank", "Saves 10% of your coins per level on death.", 5, ARMOR, true, (item, lvl) -> {
        return item;
    }),
    CHIMERA(1, "Chimera", "Copies 20% per level of your active pet's stats.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, true, (item, lvl) -> {
       return item;
    }),
    COMBO(2, "Combo", "Every mob kill within 3 seconds per level grants +2 Strength and +1 Crit Damage.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, true, (item, lvl) -> {
       return item;
    }),
    LAST_STAND(3, "Last Stand", "Increases your Defense by +5% per level when you are below 40% Health.", 5, ARMOR, true, (item, lvl) -> {
        return item;
    }),
    LEGION(4, "Legion", "Increases most* of your player stats by +0.07% per player per level within 30 blocks of you, up to 20 players.", 5, ARMOR, true, (item, lvl) -> {
        return item;
    }),
    NO_PAIN_NO_GAIN(5, "No Pain No Gain", "You have 20% chance per level to gain 10 experience orbs every time you take hits from mobs.", 5, ARMOR, true, (item, lvl) -> {
        return item;
    }),
    ONE_FOR_ALL(6, "One For All", "Removes all other enchants but increases your weapon damage by 210%.", 1, new ItemType[]{SWORD, DUNGEON_SWORD}, true, (item, lvl) -> {
        return item;
    }),
    REND(7, "Rend", "Grants a bow a Left Click ability that rips out the arrows from nearby enemies and deals 5% per level of your last critical hit for every arrow in the target. 2s Cooldown. Max of 5 arrows at a time.", 5, new ItemType[]{BOW, DUNGEON_BOW}, true, (item, lvl) -> {
        return item;
    }),
    SOUL_EATER(8, "Soul Eater", "Your weapon gains 2x the Damage of the latest monster killed and applies it on your next hit.", 5, new ItemType[]{SWORD, DUNGEON_BOW, DUNGEON_SWORD, BOW}, true, (item, lvl) -> {
        return item;
    }),
    SWARM(9, "Swarm", "Increases your damage by 1.25% per level for each enemy within 10 blocks. Maximum of 16 enemies.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, true, (item, lvl) -> {
        return item;
    }),
    ULTIMATE_JERRY(10, "Ultimate Jerry", "Increases the base Damage of Aspect of the Jerry by 1000% per level", 5, CUSTOM, true, (item, lvl) -> {
        return item;
    }),
    ULTIMATE_WISE(11, "Ultimate Wise", "Reduces the ability Mana Cost of this item by 10% per level", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, true, (item, lvl) -> {
        return item;
    }),
    WISDOM(12, "Wisdom", "Gain 1 Intelligence per level, for every 5 levels of exp you have on you. Capped at 20 Intelligence per level.", 5, ARMOR, true, (item, lvl) -> {
        return item;
    }),
    ANGLER(13, "Angler", "Increases the chance to catch a sea creature by 1% per level.", 6, ITEM, false, (sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE, sbPlayer.getHiddenStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE) + (lvl));
        return sbPlayer;
    }),
    AQUA_AFFINITY(14, "Aqua Affinity", "Increases underwater mining rate to normal level mining rate.", 1, HELMET, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    BANE_OF_ARTHROPODS(15, "Bane of Arthropods", "Increases damage dealt to Cave Spiders, Spiders and Silverfish by 8% per level.", 7, SWORD, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case SILVERFISH:
                case SPIDER:
                case CAVE_SPIDER:
                    int mult = lvl * 8;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;

    }),
    BIG_BRAIN(16, "Big Brain", "Increases Intelligence by 5 per level.", 5, HELMET, false, (sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.INTELLIGENCE, sbPlayer.getHiddenStat(SBPlayer.PlayerStat.INTELLIGENCE) + (5 * lvl));
        return sbPlayer;
    }),
    BLAST_PROTECTION(17, "Blast Protection", "Grants +30 Defense per level against explosions", 7, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    BLESSING(18, "Blessing", "Increases the chance to get double drops when fishing by 5% per level.", 5, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    CASTER(19, "Caster", "Increases the chance to not consume bait by 1% per level.", 6, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    CHANCE(20, "Chance", "Increases chance of a mob dropping an item by 15% per level.", 5, new ItemType[]{SWORD, DUNGEON_SWORD, BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    CLEAVE(21, "Cleave", "Deals 3% per level of damage dealt to other monsters within (3 + 0.3 per level) blocks of the target.", 6, SWORD, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    COMPACT(22, "Compact", "Gain 1% extra Mining exp per level with a 0.1–0.6% chance to drop an enchanted item.", 10, new ItemType[]{PICKAXE, DRILL}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    COUNTER_STRIKE(23, "Counter-Strike", "Gain +10 Defense for 7s on your first hit from an enemy.", 5, CHESTPLATE,false, (event, dmg, lvl) ->{
        return dmg;
    }),
    CRITICAL(24, "Critical", "Increases critical damage by 10% per level.", 7, SWORD, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    CUBISM(25, "Cubism", "Increases damage dealt to Creepers, Magma Cubes, and Slimes by 10% per level.", 6, new ItemType[]{SWORD, DUNGEON_SWORD, BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case CREEPER:
                case MAGMA_CUBE:
                case SLIME:
                    int mult = lvl * 10;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;
    }),
    CULTIVATING(26, "Cultivating", "Gain 1% extra Farming exp and +1 Farming Fortune per level. Upgrade by breaking crops using a hoe or axe with the enchantment applied to it. Cannot be combined in an anvil.", 10, new ItemType[]{AXE, HOE}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    DELICATE(27, "Delicate", "Avoids breaking stems and baby crops.", 5, new ItemType[]{AXE, HOE}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    DEPTH_STRIDER(28, "Depth Strider", "Increases horizontal movement speed in water by 33% per level. At level III, the movement speed is the same as on land.", 3, BOOTS, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    DRAGON_HUNTER(29, "Dragon Hunter", "Increases damage dealt to Ender Dragons by 8% per level.", 5, new ItemType[]{SWORD, DUNGEON_BOW, DUNGEON_SWORD, BOW}, false, (event, dmg, lvl) ->{
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case ENDER_DRAGON:
                    int mult = lvl * 8;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;
    }),
    DRAGON_TRACER(30, "Dragon Tracer", "Arrows home towards dragons if they are within 2 blocks per level.", 5, new ItemType[]{BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    EFFICIENCY(31, "Efficiency", "Grants +10 Mining Speed and an extra +20 per level.", 10, new ItemType[]{AXE, PICKAXE, DRILL, SHEARS}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    ENDER_SLAYER(32, "Ender Slayer", "Increases damage dealt to Ender Dragons and Enderman by 12% per level.", 7, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) ->{
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case ENDERMAN:
                case ENDER_DRAGON:
                    int mult = lvl * 12;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;
    }),
    EXECUTE(33, "Execute", "Increases damage dealt by 0.2% per level for each percent of Health missing on your target.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) ->{
        if (event.getDamager() instanceof Player) {
            LivingEntity entity = (LivingEntity)event.getEntity();
            double mult = ((entity.getMaxHealth()-entity.getHealth())/entity.getMaxHealth())*0.002*lvl;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult)))));
        }
        return dmg;
    }),
    EXPERIENCE(34, "Experience", "Increases the chance for Mobs and ores to drop double experience by 12.5% per level.", 4, new ItemType[]{SWORD, DUNGEON_SWORD, PICKAXE, DRILL}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    EXPERTISE(35, "Expertise", "Gain 2% extra Fishing exp per level and increase Sea Creature Chance by 0.6% per level. Upgrades by fishing up Sea Creatures while the rod is active.", 10, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FEATHER_FALLING(36, "Feather Falling", "Decreases fall damage by 5% per level and increases height before fall damage by 1 block per level.", 10, BOOTS, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FIRE_ASPECT(37, "Fire Aspect", "Ignites your enemies for (2 + 1 per level) seconds, dealing 50% per level of your weapon damage per second.", 2, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            event.getEntity().setFireTicks(2 + lvl);
        }
        return dmg;
    }),
    FIRE_PROTECTION(38, "Fire Protection", "Grants +2 True Defense per level against fire and lava.", 7, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FIRST_STRIKE(39, "First Strike", "Increases the first melee damage dealt to a mob by 25% per level.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
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
    FLAME(40, "Flame", "Arrow ignites target for 3 seconds, dealing 5 damage every second.", 5, new ItemType[]{BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FORTUNE(41, "Fortune", "Grants +10 Mining Fortune per level. Not compatible with Silk Touch.", 4, new ItemType[]{PICKAXE, DRILL}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FRAIL(42, "Frail", "Decrease a Sea Creature's Health by 5% per level when caught.", 6, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    FROST_WALKER(43, "Frost Walker", "Creates Ice when walking on water with a radius of 1 block per level.", 2, BOOTS, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    GIANT_KILLER(44, "Giant Killer", "Increases damage dealt by 0.1% per level for each percent of extra Health that your target has above you, up to 5% per level.", 7, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            SBPlayer player = new SBPlayer((Player) event.getDamager());
            LivingEntity entity = (LivingEntity)event.getEntity();
            double mult = ((entity.getHealth() - player.getStat(SBPlayer.PlayerStat.HEALTH))/player.getStat(SBPlayer.PlayerStat.HEALTH))*0.1*lvl;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult)))));
        }
        return dmg;
    }),
    GROWTH(45, "Growth", "Grants +15 Health per level.", 7, ARMOR, false, (sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.HEALTH, sbPlayer.getHiddenStat(SBPlayer.PlayerStat.HEALTH) + (lvl*15));
        return sbPlayer;
    }),
    HARVESTING(46, "Harvesting", "Grants +12.5 Farming Fortune per level. Can only be applied to hoes.",6,HOE,false,(sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.FARMING_FORTUNE, sbPlayer.getHiddenStat(SBPlayer.PlayerStat.FARMING_FORTUNE) + (lvl*12.5));
        return sbPlayer;
    }),
    PRISTINE(47, "Pristine", "When mining Gemstones, there is a 1% chance per level to drop a flawed version.", 5, new ItemType[]{DRILL, PICKAXE}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    IMPALING(48, "Impaling", "Increases damage dealt to guardians and squids by 12.5% per level.", 3, new ItemType[]{SWORD, DUNGEON_SWORD, BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case GUARDIAN:
                case SQUID:
                    double mult = lvl * 12.5;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;
    }),
    INFINITE_QUIVER(49, "Infinite Quiver", "Increases the chance to not consume an arrow by 3% per level.", 10, new ItemType[]{BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    KNOCKBACK(50, "Knockback", "Increases knockback by 3 blocks per level.", 2, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            Entity p = event.getEntity();

            p.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(3*lvl));
        }
        return dmg;
    }),
    LETHALITY(51, "Lethality", "Reduce the armor of the target every time you hit them by 3.0% per level for 8 seconds. Stacks up to 5 times.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    LIFE_STEAL(52, "Life Steal", "Heals for 0.5% per level of your max health each time you hit a mob.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
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
    LOOTING(53, "Looting", "Increases the chance of a monster dropping an item by 15% per level.", 5, HELMET, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    LUCK(54, "Luck", "Increases the chance for monsters to drop their armor pieces by 5% per level.", 7, HELMET, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    LUCK_OF_THE_SEAS(55, "Luck of the Sea", "Increases the chance of fishing treasure by 1% per level.", 6, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    LURE(56, "Lure", "Decreases the maximum time to catch something by 5% per level.", 3, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    MANA_STEAL(57, "Mana Steal", "Regain (0.3% per level - 0.1%) of your mana on hit.", 3, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
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
    MAGNET(58, "Magnet", "Grants 1 additional experience orb per level every time you successfully catch a fish.", 6, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    OVERLORD(59, "Overload", "Increases Crit Chance by 1% and Crit Damage by 1% per level. Having a Critical chance above 100% grants a chance to perform a Mega Critical Hit dealing 10% extra damage per level.", 5, new ItemType[]{BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    PIERCING(60, "Piercing", "Arrows fired will go through enemies. The extra targets hit take 25% of the damage.", 1, new ItemType[]{BOW, DUNGEON_BOW}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    POWER(61, "Power", "Increases bow damage by 8% per level.", 7, new ItemType[]{BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        double mult = lvl * 8;
        return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
    }),
    PROJECTILE_PROTECTION(62, "Projectile Protection", "Grants +7 Defense per level against projectiles.", 7, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    PROSECUTE(63, "Prosecute", "Increases damage dealt by 0.1% per level for each percent of health on your target up to 35% damage.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    PROTECTION(64, "Protection", "Grants +3 Defense per level.", 7, ARMOR, false, (sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.DEFENSE, sbPlayer.getHiddenStat(SBPlayer.PlayerStat.DEFENSE) + (lvl*15));
        return sbPlayer;
    }),
    PUNCH(65, "Punch", "Increases knockback dealt by 3 blocks per level.", 2, new ItemType[]{BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            Entity p = event.getEntity();

            p.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(3*lvl));
        }
        return dmg;
    }),
    RAINBOW(66, "Rainbow", "Causes Sheep to drop a random wool color when sheared.", 1, SHEARS, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    REJUVINATE(67, "Rejuvenate", "Increases your natural regeneration by 2% per level.", 5, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    REPLENISH(68, "Replenish", "Upon breaking crops, including Cocoa Beans and Nether Wart, automatically replant from materials in the inventory.", 1, new ItemType[]{AXE, HOE}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    RESPERATION(69, "Resperation", "Extends underwater breathing time by 15 seconds per level.", 3, HELMET, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    RESPITE(70, "Respite", "Increases your natural regeneration by 5% per level while out of combat.", 5, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    SCAVENGER(71, "Scavenger", "Enemies drop 0.3 coins per level times the mob's level.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
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
    SHARPNESS(72, "Sharpness", "Increases melee damage dealt by 5% per level.", 7, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            int mult = lvl * 5;
            return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
        }
        return dmg;
    }),
    SILK_TOUCH(73, "Silk Touch", "Mined blocks drop themselves instead of their normal drops (e.g., coal ore instead of coal) Not compatible with Smelting Touch and Fortune.", 1, new ItemType[]{AXE, PICKAXE, DRILL}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    SMELTING_TOUCH(74, "Smelting Touch", "Automatically smelts mined blocks into their smelted form (e.g., iron ingots instead of iron ore) Not compatible with Silk Touch.", 1, new ItemType[]{AXE, PICKAXE, DRILL}, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    SMARTY_PANTS(75, "Smarty Pants", "Gives +5 intelligence per level.", 5, LEGGINGS, false, (sbPlayer, lvl) -> {
        sbPlayer.hiddenStat(SBPlayer.PlayerStat.INTELLIGENCE, sbPlayer.stats.get(SBPlayer.PlayerStat.INTELLIGENCE) + (5 * lvl));
        return sbPlayer;
    }),
    SMITE(76, "Smite", "Increases damage dealt to Zombies, Zombie Pigmen, Withers, Wither Skeletons, and Skeletons by 8% per level.", 7, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if (event.getDamager() instanceof Player) {
            switch (event.getEntity().getType()) {
                case WITHER:
                case GIANT:
                case ZOMBIE:
                case PIG_ZOMBIE:
                case SKELETON:
                    int mult = lvl * 8;
                    return Integer.valueOf((int) Math.ceil((dmg * (1 + (mult / 100D)))));
            }
        }
        return dmg;
    }),
    SNIPE(77, "Snipe", "Arrows deal +1% damage per level for every 10 blocks traveled. Damage is calculated based on the distance between the player and the arrow when it hits an enemy.", 4, new ItemType[]{BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    SPIKED_HOOK(78, "Spiked Hook", "Fishing rod deals 5% more damage to monsters per level.", 6, ITEM, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    SUGAR_RUSH(79, "Sugar Rush", "GGrants +2 Speed per level.", 3, LEGGINGS, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    SYPHON(80, "Syphon", "Heals for (0.1% + 0.1% per level) of your max health per 100 Crit Damage you deal per hit, up to 1,000 Crit damage.", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    TELEKINESIS(81, "Telekinesis", "Blocks and mob drops go directly into your inventory.", 1, ItemType.values(), false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    THORNS(82, "Thorns", "Grants a 50% chance to rebound 3% per level of damage dealt back at the attacker.", 3, ARMOR, false, (sbPlayer, lvl) -> {
        return sbPlayer;
    }),
    THUNDERBOLT(83, "Thunderbolt", "Strikes Monsters within 2 blocks with lightning every 3 consecutive hits, dealing 15% per level of your strength as damage", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    THUNDERLORD(84, "Thunderlord", "Strikes a monster with thunder every 3 consecutive hits, dealing 30% per level of your Strength as damage.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    TITAN_KILLER(85, "Titan Killer", "Increases damage dealt by 2% per level for each 100 defense your target has up to 50%.", 7, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    TRIPLE_STRIKE(86, "Triple Strike", "Increases damage dealt by 10% per level for the first three hits on a mob", 5, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        if(event.getDamager() instanceof Player) {
            SBPlayer p = new SBPlayer(((Player) event.getDamager()));
            double mult = (lvl*0.1);
            int timeshit = 0;
            LivingEntity en = (LivingEntity) event.getEntity();
            if(en.getMetadata("times-hit").size()>=1) {
                timeshit = en.getMetadata("times-hit").get(0).asInt();
            }
            if(timeshit<3) {
                return Integer.valueOf((int) (dmg *mult));
            }
        }
        return dmg;
    }),
    TRUE_PROTECTION(87, "True Protection", "Grants +5 True Defense.", 1, CHESTPLATE, false, (item, lvl) -> {
        item.hiddenStat(SBPlayer.PlayerStat.FARMING_FORTUNE, item.getHiddenStat(SBPlayer.PlayerStat.FARMING_FORTUNE) + (lvl*5));
        return item;
    }),
    VAMPIRISM(88, "Vampirism", "Heals for 1% of your missing Health per level whenever you kill an enemy.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
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
    VENOMOUS(89, "Venomous", "Reduces the target's walking Speed by 5 per level and deals 5 damage per level every second. This effect lasts for 4 seconds.", 6, new ItemType[]{SWORD, DUNGEON_SWORD}, false, (event, dmg, lvl) -> {
        return dmg;
    }),
    VICIOUS(90, "Vicious", "Grants 1 Ferocity per level.", 5, new ItemType[]{SWORD, DUNGEON_SWORD, BOW, DUNGEON_BOW}, false, (event, dmg, lvl) -> {
        return dmg;
    });

    private String name;
    private final int index;
    private String description;
    private int maxLvl;
    private ItemType type;
    private ItemType[] types;
    private TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action2;
    private BiFunction<SBItemBuilder, Integer, SBItemBuilder> action3;
    private boolean isUlt;


    Enchantment(int index, String name, String description, int maxLvl, ItemType type,boolean isUlt, TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action) {
        this.index = index;
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action2 = action;
        this.isUlt = isUlt;
        this.description = description;
    }

    Enchantment(int index, String name, String description, int maxLvl, ItemType type, boolean isUlt, BiFunction<SBItemBuilder, Integer, SBItemBuilder> action) {
        this.index = index;
        this.name = name;
        this.maxLvl = maxLvl;
        this.type = type;
        this.action3 = action;
        this.isUlt = isUlt;
        this.description = description;
    }

    Enchantment(int index, String name, String description, int maxLvl, ItemType[] type, boolean isUlt, TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> action) {
        this.index = index;
        this.name = name;
        this.maxLvl = maxLvl;
        this.types = type;
        this.action2 = action;
        this.isUlt = isUlt;
        this.description = description;
    }

    Enchantment(int index, String name, String description, int maxLvl, ItemType[] type, boolean isUlt, BiFunction<SBItemBuilder, Integer, SBItemBuilder> action) {
        this.index = index;
        this.name = name;
        this.maxLvl = maxLvl;
        this.types = type;
        this.action3 = action;
        this.isUlt = isUlt;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public boolean isUltimate() {
        return isUlt;
    }

    public ItemType getItemType() {
        return type;
    }

    public ItemType[] getItemTypes() {
        return types;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public String getDescription () {
        return description;
    }

    public TriFunction<EntityDamageByEntityEvent, Integer, Integer, Integer> getDamageAction() {
        return action2;
    }

    public BiFunction<SBItemBuilder, Integer, SBItemBuilder> getHeldAction() {
        return action3;
    }

    public static ArrayList<Enchantment> sortedEnchants = new ArrayList<>();
}
