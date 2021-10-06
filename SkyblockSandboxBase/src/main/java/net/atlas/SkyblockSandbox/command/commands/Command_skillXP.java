package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_skillXP extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_skillXP(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "skillxp",aliases = {"addskill","addxp"},permission = "sbx.admin.skills")
    public void cmd(SBCommandArgs cmd) {
        String[] args = cmd.getArgs();
        SBPlayer p = new SBPlayer((Player) cmd.getSender());

        if(args.length>=2) {
            String s = args[0];
            SkillType type = Enums.getIfPresent(SkillType.class,s.toUpperCase()).orNull();
            if(type!=null) {
                if(args.length>=3) {
                    Player argsPlayer = Bukkit.getPlayer(args[2]);
                    if(argsPlayer!=null && argsPlayer.isValid() && argsPlayer.isOnline()) {
                        p = new SBPlayer(argsPlayer);
                    } else {
                        p.sendMessage(SUtil.colorize("&cInvalid player!"));
                        return;
                    }
                }
                if(NumUtils.isInt(args[1])) {
                    int amt = Integer.parseInt(args[1]);
                    p.addSkillXP(type,amt);
                    p.sendMessage(SUtil.colorize("&aSuccess!"));
                } else {
                    p.sendMessage(SUtil.colorize("&cInvalid amount!"));
                }
            } else {
                p.sendMessage(SUtil.colorize("&cInvalid skill type!"));
            }
        }
    }
}
