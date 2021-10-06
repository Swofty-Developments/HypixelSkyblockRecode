package net.atlas.SkyblockSandbox.command.commands;

import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Optional;

public class Command_forward extends SkyblockCommandFramework {
    public final static String MESSAGE_CHANNEL = "commandforward:cmd";
    public final static String VISIT_MESSAGE_CHANNEL = "visiting:player";

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_forward(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "forwardcmdtobungeecord", description = "forward a cmd to bungee",permission = "sbx.admin.bungee")
    public void forwardCmd(SBCommandArgs arguments) {
        String[] args = arguments.getArgs();
        CommandSender sender = arguments.getSender();

        if (sender instanceof Player) {
            sender.sendMessage("§cProcess failed. Please try again later.");

            return;
        }

        if (args.length <= 1) {
            sendErrorMessage(sender, "Wrong command, Missing arguments");
            return;
        }

        String channelPlayer = args[0];

        Optional<? extends Player> optPlayer = Bukkit.getOnlinePlayers().stream().findAny();
        if (!optPlayer.isPresent()) {
            sendErrorMessage(sender, "No player is online to forward this command");
            return;
        }

        Player messageSender = optPlayer.get();

        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        if ("Console".equalsIgnoreCase(channelPlayer)) {
            dataOutput.writeBoolean(false);
        } else {
            if(SBX.getInstance().getServer().getPlayer(channelPlayer) == null) {
                sendErrorMessage(sender, "Player '" + channelPlayer + "' not found");
                return;
            }

            dataOutput.writeBoolean(true);
            messageSender = SBX.getInstance().getServer().getPlayer(channelPlayer);
        }

        dataOutput.writeUTF(args[1]);
        dataOutput.writeUTF(Joiner.on(' ').join(Arrays.copyOfRange(args, 2, args.length)));
        dataOutput.writeBoolean(sender.isOp());
        messageSender.sendPluginMessage(SBX.getInstance(), MESSAGE_CHANNEL, dataOutput.toByteArray());
    }

    private void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage( ChatColor.YELLOW + "An unexpected error occurred: " + ChatColor.RED + message);
    }
}
