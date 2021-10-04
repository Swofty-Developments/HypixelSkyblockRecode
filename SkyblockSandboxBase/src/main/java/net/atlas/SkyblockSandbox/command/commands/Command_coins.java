package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.economy.CoinEvent;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Command_coins extends SkyblockCommandFramework {

    String[] addArr = {"add", "give", "put"};
    String[] removeArr = {"take", "remove", "delete"};

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_coins(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "coins", aliases = {"skyblockcoins"}, description = "Gives/Takes coins", usage = "/coins <add/take> <Player> <Amount>",permission = "sbx.admin.economy")
    public void coinsCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            String[] args = cmd.getArgs();
            Coins coins = SBX.getInstance().coins;
            if (args.length >= 3) {
                String arg1 = args[0];
                Player pl = Bukkit.getPlayer(args[1]);
                if (pl != null && pl.isValid()) {
                    p = new SBPlayer(pl);
                } else {
                    p.sendMessage(SUtil.colorize("&cInvalid player!"));
                    return;
                }
                if (listContains(arg1, addArr)) {
                    if (NumUtils.isInt(args[2])) {
                        int amt = Integer.parseInt(args[2]);
                        coins.addCoins(p, (double) amt, CoinEvent.ADMIN);
                    } else {
                        p.sendMessage(SUtil.colorize("&cInvalid coin amount!"));
                    }
                }
                if (listContains(arg1, removeArr)) {
                    if (NumUtils.isInt(args[2])) {
                        int amt = Integer.parseInt(args[2]);
                        coins.removeCoins(p, (double) amt, CoinEvent.ADMIN);
                    } else {
                        p.sendMessage(SUtil.colorize("&cInvalid coin amount!"));
                    }
                }
            }
        }

    }

    private boolean listContains(String s, String[] arr) {
        for (String sl : arr) {
            if (s.equalsIgnoreCase(sl)) {
                return true;
            }
        }
        return false;
    }
}
