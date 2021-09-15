package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.entity.customEntity.NoTeleportEnderman;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command_spawnmob extends SkyblockCommandFramework {
    List<String> flags = new ArrayList<>(Arrays.asList("--noteleport", "--noai"));

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_spawnmob(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "spawnmob", aliases = { "spawncustommob" }, description = "Spawns a mob with flags", usage = "/spawnmob <Type> <Health> --noai --noteleport")
    public void spawnMobCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            if (cmd.getArgs().length >= 3) {
                String[] args = cmd.getArgs();
                EntityType entity = Enums.getIfPresent(EntityType.class, args[0].toUpperCase()).orNull();
                if (entity != null) {
                    Entity en = p.getWorld().spawnEntity(p.getLocation(),entity);
                    String name = SUtil.colorize(args[1]).replace("_"," ").replace("/un","_");
                    en.setCustomName(name);

                    if (en instanceof LivingEntity) {
                        int health = 1;
                        try {
                            health = Integer.parseInt(args[2]);
                            ((LivingEntity) en).setMaxHealth(health);
                            ((LivingEntity) en).setHealth(health);
                        } catch (NumberFormatException ignored) {
                            p.sendMessage(SUtil.colorize("&cInvalid health amount!"));
                        }

                        if (entity.equals(EntityType.ENDERMAN)) {
                            if(args.length>3) {
                                if (flags.contains(args[3].toLowerCase())) {
                                    String flag = flags.get(flags.indexOf(args[3].toLowerCase()));
                                    switch (flag) {
                                        case "--noteleport":
                                            if (en instanceof Enderman) {
                                                en.remove();
                                                en = NoTeleportEnderman.spawn(p.getLocation());
                                                ((Enderman) en).setMaxHealth(health);
                                                ((Enderman) en).setHealth(health);
                                                en.setCustomName(name);
                                            }
                                            break;
                                        case "--noai":
                                            setAI((LivingEntity) en, false);
                                            en.setCustomName(name);
                                            break;

                                    }

                                }
                                if(args.length>4) {
                                    if (flags.contains(args[4].toLowerCase())) {
                                        String flag = flags.get(flags.indexOf(args[3].toLowerCase()));
                                        switch (flag) {
                                            case "--noteleport":
                                                if (en instanceof Enderman) {
                                                    en = NoTeleportEnderman.spawn(p.getLocation());
                                                    ((Enderman) en).setMaxHealth(health);
                                                    ((Enderman) en).setHealth(health);
                                                    en.setCustomName(name);
                                                }
                                                break;
                                            case "--noai":
                                                setAI((LivingEntity) en, false);
                                                en.setCustomName(name);
                                                break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }

            ///spawnMob enderman "pee" 100000 - noTeleport - noAI
        }

    }

    @SBCommand(name = "spawnmob.slayer", aliases = { "spawncustommob.slayer" }, description = "Spawns a slayer boss", usage = "/spawnmob slayer <Type> <Tier>")
    public void spawnMobSlayerCmd(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            if (cmd.getArgs().length >= 2) {
                String[] args = cmd.getArgs();
                Slayers slayer = Enums.getIfPresent(Slayers.class, args[0].toUpperCase()).orNull();
                SlayerTier tier = Enums.getIfPresent(SlayerTier.class,args[1].toUpperCase()).orNull();

                if (slayer != null) {
                    if(tier!=null) {
                        if (tier.toInteger() <=slayer.getSlayerClass().getMaxTier().toInteger()) {
                            slayer.spawn(p,tier,p.getLocation());
                        } else {
                            p.sendMessage(SUtil.colorize("&cTier too high!"));
                        }
                    } else {
                        p.sendMessage(SUtil.colorize("&cPlease specify a tier!"));
                    }
                } else {
                    p.sendMessage(SUtil.colorize("&cPlease specify a valid boss type!"));
                }

            }

            ///spawnMob enderman "pee" 100000 - noTeleport - noAI
        }

    }




    public static void setAI(LivingEntity entity, boolean hasAi) {
        EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
        handle.getDataWatcher().watch(15, (byte) (hasAi ? 0 : 1));
    }
}
