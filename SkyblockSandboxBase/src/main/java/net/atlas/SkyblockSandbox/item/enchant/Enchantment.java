package net.atlas.SkyblockSandbox.item.enchant;

import net.atlas.SkyblockSandbox.listener.sbEvents.damageEvents.EntityDamageEntityEvent;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum Enchantment {
    Angler("Angler", 5, sbPlayer -> {
        sbPlayer.setMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE, sbPlayer.getMaxStat(SBPlayer.PlayerStat.SEA_CREATURE_CHANCE) + 1);
    }),
    Aqua_Affinity("Aqua Affinity", 3, sbPlayer -> {

    }),
    Bane_of_Arthropods("Bane of Arthropods",6, (entityDamageEntityEvent, integer) -> {
    }),
    Big_Brain(),
    Blast_Protection(),
    Blessing(),
    Caster(),
    Chance(),
    Cleave(),
    Compact(),
    Counter_Strike("Counter-Strike"),
    Critical(),
    Cubism(),
    Cultivating(),
    Delicate(),
    Depth_Strider(),
    Dragon_Hunter(),
    Dragon_Tracer(),
    Efficiency(),
    Ender_Slayer(),
    Execute(),
    Experience(),
    Expertise(),
    Feather_Falling(),
    Fire_Aspect(),
    Fire_Protection(),
    First_Strike(),
    Flame(), Fortune(),
    Frail(),
    Frost_Walker(),
    Giant_Killer(),
    Growth(),
    Harvesting_Pristine(),
    Impaling(),
    Infinite_Quiver(),
    Knockback_Lethality(),
    Life_Steal(),
    Looting(),
    Luck(),
    Luck_of_the_Sea(),
    Lure(),
    Mana_Steal(),
    Magnet(),
    Overload(),
    Piercing(),
    Power(),
    Projectile_Protection(),
    Prosecute(),
    Protection(), Punch(),
    Rainbow(),
    Rejuvenate(),
    Replenish(),
    Respiration(),
    Respite(),
    Scavenger(),
    Sharpness(),
    Silk_Touch(),
    Smelting_Touch(),
    Smarty_Pants(),
    Smite(),
    Snipe(),
    Spiked_Hook(),
    Sugar_Rush(),
    Syphon(),
    Telekinesis(),
    Thorns(),
    Thunderbolt(),
    Thunderlord(),
    Titan_Killer(),
    Triple_Strike(),
    True_Protection(),
    Turbo_Crop(),
    Vampirism(),
    Venomous(),
    Vicious();

    private String name;
    private int maxLvl;
    private Consumer<SBPlayer> action;
    private BiFunction<EntityDamageEntityEvent,Integer,Integer> action2;

    Enchantment(String name, int maxLvl, Consumer<SBPlayer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.action = action;
    }

    Enchantment(String name, int maxLvl, BiFunction<EntityDamageEntityEvent,Integer,Integer> action) {
        this.name = name;
        this.maxLvl = maxLvl;
        this.action2 = action;
    }

}
