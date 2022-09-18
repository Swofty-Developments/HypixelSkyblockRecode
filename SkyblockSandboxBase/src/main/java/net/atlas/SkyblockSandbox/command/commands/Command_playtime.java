package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.listener.sbEvents.PlayerJoin;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.plugin.Plugin;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class Command_playtime extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_playtime(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name="playtime", description = "Get your current playtime", inGameOnly = true)
    public void playtimeCommand(SBCommandArgs cmd) {
        SBPlayer player = new SBPlayer(cmd.getPlayer());
        int rn = Math.toIntExact(TimeUnit.MILLISECONDS.toMinutes(ZonedDateTime.now(ZoneId.of("-05:00")).toInstant().toEpochMilli() - PlayerJoin.time.get(player.getUniqueId()).toInstant().toEpochMilli()));

        double t = PlayerJoin.playTimeMinutes.get(player.getUniqueId()) + rn;
        int hours = (int) (t / 60);
        int minutes = (int) (t % 60);
        if (hours > 0) {
            player.sendMessage(String.format("&aYou have %d hour(s) and %d minute(s) playtime!", hours, minutes));
        } else {
            player.sendMessage(String.format("&aYou have %d minute(s) playtime!", minutes));
        }
    }
}
