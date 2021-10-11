package net.atlas.SkyblockSandbox.scoreboard;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.scoreboard.CScoreboard.Row;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DragonScoreboard {
    CScoreboard scoreboard = null;
    public static HashMap<Player, CScoreboard> scoreboards = new HashMap<>();
    private final SBX plugin;

    public DragonScoreboard(SBX plugin) {
        this.plugin = plugin;
    }

    public void setScoreboard(Player p) {
        Coins coins = plugin.coins;
        Double basecoins = coins.getCoins(p);
        scoreboard = new CScoreboard("sidemenu", "", colorize("&e&lSKYBLOCK"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();


        double dragHealth = StartFight.dragonHealth;
        String dragHealthScore = "Dragon Health: " + ChatColor.GREEN + new DecimalFormat("#").format(dragHealth) + ChatColor.RED + " ❤";
        String yourDamageScore = "Your Damage: " + ChatColor.RED + StartFight.playerDMG.get(p);
        List<Row> rows = new ArrayList<>();
        //line 1
        Row row1 = scoreboard.addRow(colorize("&7" + dtf.format(now) + " &8" + SBX.getInstance().getServer().getServerName()));
        //line 2
        Row row2 = scoreboard.addRow("");
        //line 3
        Row row3 = scoreboard.addRow(colorize("&r" + "Early Summer 1st"));
        //line 4
        Row row4 = scoreboard.addRow(colorize("&7" + "12:00pm"));
        //line 5
        Row row5 = scoreboard.addRow(colorize("&7⏣ None"));
        //line 6
        Row row6 = scoreboard.addRow(" ");
        //line 7 and 8
        DecimalFormat format = new DecimalFormat("###,###");
        if (basecoins != null) {
            String purse = "Purse: " + ChatColor.GOLD + format.format(basecoins);

            if (purse.length() >= 31) {
                purse = ChatColor.WHITE + "Purse: " + ChatColor.GOLD + NumberSuffix.format(basecoins.longValue());
            }
            Row row7 = scoreboard.addRow(purse);
        }

        //line 9
        if (StartFight.fightActive) {
            Row row9 = scoreboard.addRow(dragHealthScore.substring(0, 15));
            //line 10
            Row row10 = scoreboard.addRow(yourDamageScore.substring(0, 15));
        } else {
            Row row9 = scoreboard.addRow("  ");
            //line 10
            Row row10 = scoreboard.addRow(ChatColor.YELLOW + "mc.the-atlas.net");
        }

        scoreboard.finish();
        scoreboard.display(p);
        scoreboards.put(p, scoreboard);

        Bukkit.getScheduler().runTaskTimer(SBX.getInstance(), () -> {
            updateScoreboard(p);
        }, 0L, 20L);
    }


    public void updateDragonDMG(Player p, double dmg) {
        StartFight.playerDMG.put(p, StartFight.playerDMG.get(p) + dmg);
    }

    void updateScoreboard(Player p) {
        Coins coins = plugin.coins;
        Double basecoins = coins.getCoins(p);
        scoreboard = new CScoreboard("sidemenu", "", colorize("&e&lSKYBLOCK"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();


        double dragHealth = StartFight.dragonHealth;
        String dragHealthScore = SUtil.colorize("Dragon HP:&a &a") + new DecimalFormat("#").format(dragHealth) + ChatColor.RED + " ❤";
        String yourDamageScore;
        if (StartFight.playerDMG.get(p) != null) {
            yourDamageScore = "Your Damage: " + ChatColor.RED + new DecimalFormat("###,###.#").format(StartFight.playerDMG.get(p));
        } else {
            yourDamageScore = "0";
        }
        List<Row> rows = new ArrayList<>();
        //line 1
        Row row1 = scoreboard.addRow(colorize("&7" + dtf.format(now) + " &8" + SBX.getInstance().getServer().getServerName()));
        //line 2
        Row row2 = scoreboard.addRow("");
        //line 3
        Row row3 = scoreboard.addRow(colorize("&r" + "Early Summer 1st"));
        //line 4
        Row row4 = scoreboard.addRow(colorize("&7" + "12:00pm"));
        //line 5
        Row row5 = scoreboard.addRow(colorize("&7⏣ None"));
        //line 6
        Row row6 = scoreboard.addRow(" ");
        //line 7 and 8
        DecimalFormat format = new DecimalFormat("###,###");
        if (basecoins != null) {
            String purse = "Purse: " + ChatColor.GOLD + format.format(basecoins);

            if (purse.length() >= 31) {
                purse = ChatColor.WHITE + "Purse: " + ChatColor.GOLD + NumberSuffix.format(basecoins.longValue());
            }
            Row row7 = scoreboard.addRow(purse);
        }

        //line 9
        if (StartFight.fightActive) {
            Row row9 = scoreboard.addRow("  ");
            if (dragHealthScore.length() > 31) {
                dragHealthScore = dragHealthScore.substring(0, 31);
            }
            Row row10 = scoreboard.addRow(dragHealthScore);
            //line 10
            if (yourDamageScore.length() > 31) {
                yourDamageScore = yourDamageScore.substring(0, 31);
            }
            Row row11 = scoreboard.addRow(yourDamageScore);
            Row row12 = scoreboard.addRow("   ");
            Row row13 = scoreboard.addRow(ChatColor.YELLOW + "mc.the-atlas.net");
        } else {
            Row row9 = scoreboard.addRow("  ");
            //line 10
            Row row10 = scoreboard.addRow(ChatColor.YELLOW + "mc.the-atlas.net");
        }
        scoreboard.finish();
        scoreboard.display(p);
    }

    String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
