package net.atlas.SkyblockSandbox.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderAPI extends PlaceholderExpansion {
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sbx";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Atlas";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        SBPlayer p = new SBPlayer(player);
        if (params.toLowerCase().startsWith("stats_")) {
            for (SBPlayer.PlayerStat stat : SBPlayer.PlayerStat.values()) {
                if (params.replace(params.substring(0,5), "").equals(stat.getDisplayName().toLowerCase())) {
                    return "" + p.getStat(stat);
                }
            }
        } else if (params.toLowerCase().startsWith("maxstat_")) {
            for (SBPlayer.PlayerStat stat : SBPlayer.PlayerStat.values()) {
                if (params.replace(params.substring(0, 7), "").equals(stat.getDisplayName().toLowerCase())) {
                    return "" + p.getMaxStat(stat);
                }
            }
        }
        return null;
    }
}
