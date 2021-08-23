package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.citizensnpcs.npc.ai.speech.Chat;
import net.minecraft.server.v1_8_R3.BlockFloorSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_jingle extends SkyblockCommandFramework {


    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_jingle(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "playJingle", aliases = { "jingle" }, description = "Allows you to play jingles", usage = "/playJingle <Jingle ID> <Player> <Loop>")
    public void jingleCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer sender = new SBPlayer((Player) cmd.getSender());
            SBPlayer target;
            String[] args = cmd.getArgs();
            boolean flag = false;
            if(args.length>=1) {
                if(args.length>=2) {
                    if(Bukkit.getPlayer(args[1])!=null && Bukkit.getPlayer(args[1]).isValid()) {
                        target = new SBPlayer(Bukkit.getPlayer(args[1]));
                    } else {
                        sender.sendMessage(ChatColor.RED + "Please specify a valid player!");
                        return;
                    }
                    if(args.length>=3) {
                        try {
                            flag = Boolean.parseBoolean(args[2]);
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    target = sender;
                }
                String songID = args[0].toUpperCase();
                Jingle j = Enums.getIfPresent(Jingle.class,songID).orNull();
                if(j!=null) {
                    target.playJingle(j, flag);
                } else {
                    target.playJingle(songID, flag);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Please specify a valid song id!");
            }
        }

    }

    @SBCommand(name = "stopJingle", description = "Stops jingles playing", usage = "/stopJingle <Player>")
    public void stopJingleCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer sender = new SBPlayer((Player) cmd.getSender());
            SBPlayer target;
            String[] args = cmd.getArgs();
            boolean flag = false;
            if(args.length>=1) {
                target = new SBPlayer(Bukkit.getPlayer(args[0]));
                if (target.getPlayer()==null || !target.isValid()) {
                    sender.sendMessage(ChatColor.RED + "Please specify a valid player!");
                    return;
                }
            } else {
                target = sender;
            }
            if(NoteBlockAPI.isReceivingSong(target.getPlayer())) {
                NoteBlockAPI.stopPlaying(target.getPlayer());
            }
        }

    }

}
