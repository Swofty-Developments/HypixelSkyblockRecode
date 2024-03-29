package net.atlas.SkyblockSandbox.abilityCreator.functions;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes.ParticleChooserGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes.ParticleShapeGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

public class Particle extends Function {

    public Particle(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
    }

    @Override
    public void applyFunction() {
        SBPlayer player = getPlayer();
        ParticleShape shape = Enums.getIfPresent(ParticleShape.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_SHAPE)).orNull();
        String strRange = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_RANGE);
        if(strRange.isEmpty()) {
            strRange = "1";
        }
        int range = Integer.parseInt(strRange);
        Particles particles = Enums.getIfPresent(Particles.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_TYPE)).orNull();
        if (shape == null) {
            getPlayer().sendMessage(SUtil.colorize("&cYou don't have a shape set!"));
            return;
        }
        if (particles == null) {
            getPlayer().sendMessage(SUtil.colorize("&cYou don't have a particle type set!"));
            return;
        }
        List<Location> shapeList = shape.getShape(player, range);
        boolean message = Boolean.parseBoolean(FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), FunctionValues.SEND_MESSAGE));
        double d = 10000 * (1 + (player.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE) / 100) * 0.3)/*todo + damage*/;
        int i = 0;
        for (Location loc : shapeList) {
            PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(particles.getA(), true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1);
            for (Entity en : loc.getWorld().getNearbyEntities(loc, 30, 30, 30)) {
                if (en instanceof Player) {
                    ((CraftPlayer) en).getHandle().playerConnection.sendPacket(particlePacket);
                }
            }
            if(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.PARTICLE_DAMAGE).equals("true")) {
                for (Entity e : player.getWorld().getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
                    if (e instanceof LivingEntity) {
                        if (!(e instanceof Player || e instanceof ArmorStand || e instanceof NPC)) {
                            if (e.isDead()) {
                                ((LivingEntity) e).damage(0);
                            } else {
                                ++i;
                                if (((LivingEntity) e).getHealth() <= d) {
                                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(getPlayer(), e, EntityDamageEvent.DamageCause.MAGIC, d);
                                    SkillEXPGainEvent skillEvent = new SkillEXPGainEvent(getPlayer(), SkillType.COMBAT, 1000, event);
                                    Bukkit.getPluginManager().callEvent(skillEvent);
                                }
                                ((LivingEntity) e).damage(d);
                            }
                        }
                    }
                }
            }
        }
        if (message) {
            if (i >= 1) {
                DecimalFormat format = new DecimalFormat("#,###");
                int damage = (int) (d * i);
                player.sendMessage("&7Your Particle Function hit &c" + i + "&7 enemies dealing &c" + format.format(damage) + " damage&7.");
            }
        }
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.PARTICLE;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return null;
    }

    public enum ParticleShape {
        CIRCLE(Material.SNOW_BALL) {
            public List<Location> getShape(SBPlayer p, int rad) {
                return getCircle(p.getLocation(), rad, 40);
            }
        },
        CONE(Material.STRING) {
            public List<Location> getShape(SBPlayer p, int rad) {
                return getCircle(p.getLocation(), rad, 40);
            }
        },
        SQUARE(Material.PAINTING) {
            public List<Location> getShape(SBPlayer p, int rad) {
                return getSquare(p.getLocation(), rad, 40);
            }
        },
        LINE(Material.STICK) {
            public List<Location> getShape(SBPlayer p, int rad) {
                return getLine(p.getLocation(), rad);
            }
        };

        public abstract List<Location> getShape(SBPlayer p, int rad);
        private Material mat;

        ParticleShape(Material mat) {
            this.mat = mat;
        }

        public Material getMat() {
            return mat;
        }
    }

    public enum dataValues implements Function.dataValues {
        PARTICLE_TYPE, PARTICLE_RANGE, PARTICLE_SHAPE,PARTICLE_DAMAGE
    }

    public enum Particles {
        FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK, Material.FIREWORK),
        CRIT(EnumParticle.CRIT, Material.IRON_SWORD),
        SPELL(EnumParticle.SPELL, Material.STICK),
        NOTE(EnumParticle.NOTE, Material.NOTE_BLOCK),
        PORTAL(EnumParticle.PORTAL, Material.ENDER_PORTAL_FRAME),
        FLAME(EnumParticle.FLAME, Material.FLINT_AND_STEEL),
        FOOTSTEP(EnumParticle.FOOTSTEP, Material.ARROW),
        SMOKE_NORMAL(EnumParticle.SMOKE_NORMAL, Material.FLINT),
        EXPLOSION_HUGE(EnumParticle.EXPLOSION_HUGE, Material.TNT),
        EXPLOSION_LARGE(EnumParticle.EXPLOSION_LARGE, Material.TNT),
        EXPLOSION_NORMAL(EnumParticle.EXPLOSION_NORMAL, Material.TNT),
        SMOKE_LARGE(EnumParticle.SMOKE_LARGE, Material.FLINT),
        CLOUD(EnumParticle.CLOUD, Material.CLAY_BALL),
        SNOWBALL(EnumParticle.SNOWBALL, Material.SNOW_BALL),
        WATERDRIP(EnumParticle.WATER_DROP, Material.WATER_BUCKET),
        LAVADRIP(EnumParticle.DRIP_LAVA, Material.LAVA_BUCKET),
        SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL, Material.IRON_SPADE),
        SLIME(EnumParticle.SLIME, Material.SLIME_BALL),
        HEART(EnumParticle.HEART, Material.COOKED_BEEF),
        ITEM_TAKE(EnumParticle.ITEM_TAKE, Material.IRON_PICKAXE),
        WATER_BUBBLE(EnumParticle.WATER_BUBBLE, Material.WATER_BUCKET),
        WATER_SPLASH(EnumParticle.WATER_SPLASH, Material.WATER_BUCKET),
        WATER_WAKE(EnumParticle.WATER_WAKE, Material.WATER_BUCKET),
        SUSPENDED(EnumParticle.SUSPENDED, Material.CLAY_BALL),
        SUSPENDED_DEPTH(EnumParticle.SUSPENDED_DEPTH, Material.CLAY_BALL),
        CRIT_MAGIC(EnumParticle.CRIT_MAGIC, Material.STICK),
        SPELL_INSTANT(EnumParticle.SPELL_INSTANT, Material.STICK),
        SPELL_MOB(EnumParticle.SPELL_MOB, Material.STICK),
        SPELL_MOB_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT, Material.STICK),
        SPELL_WITCH(EnumParticle.SPELL_WITCH, Material.STICK),
        DRIP_WATER(EnumParticle.DRIP_WATER, Material.WATER_BUCKET),
        DRIP_LAVA(EnumParticle.DRIP_LAVA, Material.LAVA_BUCKET),
        VILLAGER_ANGRY(EnumParticle.VILLAGER_ANGRY, Material.HAY_BLOCK),
        VILLAGER_HAPPY(EnumParticle.VILLAGER_HAPPY, Material.HAY_BLOCK),
        TOWN_AURA(EnumParticle.TOWN_AURA, Material.HAY_BLOCK),
        ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE, Material.ENCHANTMENT_TABLE),
        LAVA(EnumParticle.LAVA, Material.LAVA_BUCKET),
        REDSTONE(EnumParticle.REDSTONE, Material.REDSTONE),
        BARRIER(EnumParticle.BARRIER, Material.BARRIER),
        WATER_DROP(EnumParticle.WATER_DROP, Material.WATER_BUCKET);

        private final EnumParticle a;
        private final Material b;

        public EnumParticle getA() {
            return this.a;
        }

        public Material getB() {
            return this.b;
        }

        Particles(EnumParticle a, Material b) {
            this.a = a;
            this.b = b;
        }
    }

    public static List<Location> getCircle(Location center, double radius, int amount) {
        List<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    public static List<Location> getLine(Location location, int range) {
        List<Location> locations = new ArrayList<>();
        World world = location.getWorld();
        Vector direction = location.getDirection().normalize();
        direction.multiply(range);
        for (double i = 0; i < range / 5f; i += 0.1) {
            locations.add(location.add(direction));
        }
        return locations;
    }

    public static List<Location> getSquare(Location center, double radius, int amount) {
        List<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        double startX = center.clone().subtract(radius, 0, 0).getX();
        double startZ = center.clone().subtract(0, 0, radius).getZ();
        double endX = center.clone().add(radius, 0, 0).getX();
        double endZ = center.clone().add(0, 0, radius).getZ();
        for (double x = startX; x < endX; x++) {
            for (double z = startZ; z < endZ; z++) {
                if (x != startX || z != startZ) {
                    locations.add(new Location(world, x, center.getY(), z));
                }
            }
        }
        return locations;
    }

    @Override
    public void getGuiLayout() {
        Particles particle = Enums.getIfPresent(Particles.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_TYPE)).orNull();
        if (particle == null) {
            setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aCurrently edited particle", 1, 0, "&7Currently editing:","&cNOT SET" + "","","&eClick to set!"));
        } else {
            setItem(13, makeColorfulItem(particle.getB(), "&aCurrently edited particle", 1, 0, "&7Currently editing:","&b" + particle.name() + "","","&eClick to change!"));
        }
        setItem(12, makeColorfulItem(Material.IRON_BLOCK, "&aParticle damage", 1, 0, "&7Turn on and off","&7Particle damage","&7for the &bParticle Function","","&eClick to set!"));
        setItem(14, makeColorfulItem(Material.WATCH, "&aShape of Particle", 1, 0, "&7Set the shape of the particles","&7played","&7Includes: Circle, Square, and Cone (AOTD/Ice Spray)","","&eLeft click to enable!","&bRight-click to disable"));
        setItem(15,makeColorfulItem(Material.SNOW_BALL,"&aParticle Range",1,0,"&7Set the particle function range!","","&eClick to edit!"));
        setAction(13, event -> {
            new ParticleChooserGUI(getPlayer(), getAbilIndex(), getFunctionIndex()).open();
        });
        if(FunctionUtil.getFunctionData(getStack(),getAbilIndex(),getFunctionIndex(),dataValues.PARTICLE_DAMAGE).equals("true")) {
            setItem(11, makeColorfulItem(Material.FEATHER, "&aParticle damage message", 1, 0, "&7Turn on and off","&7The Particle damage message","&7for the &bParticle Function","","&eLeft click to enable!","&bRight-click to disable"));
            setAction(11,event -> {
                ItemStack stack = getStack();
                switch (event.getClick()) {
                    case LEFT:
                        stack = FunctionUtil.setFunctionData(stack, getAbilIndex(), getFunctionIndex(), FunctionValues.SEND_MESSAGE, "true");
                        break;
                    case RIGHT:
                        stack = FunctionUtil.setFunctionData(stack, getAbilIndex(), getFunctionIndex(), FunctionValues.SEND_MESSAGE, "false");
                        break;
                }
                if (getStack() != stack) {
                    getPlayer().setItemInHand(stack);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.ITEM_PICKUP,1,1);
                }
            });
        }

        setAction(12, event -> {
            ItemStack stack = getStack();
            switch (event.getClick()) {
                case LEFT:
                    stack = FunctionUtil.setFunctionData(stack, getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_DAMAGE, "true");
                    break;
                case RIGHT:
                    stack = FunctionUtil.setFunctionData(stack, getAbilIndex(), getFunctionIndex(), dataValues.PARTICLE_DAMAGE, "false");
                    break;
            }
            if (getStack() != stack) {
                getPlayer().setItemInHand(stack);
                getPlayer().playSound(getPlayer().getLocation(), Sound.ITEM_PICKUP,1,1);
            }
        });
        setAction(14,event -> {
            new ParticleShapeGUI(getPlayer(),getAbilIndex(),getFunctionIndex(), getFunctionType()).open();
        });
        setAction(15,event -> {
            anvilGUI(dataValues.PARTICLE_RANGE,1,30);
        });
    }
}


