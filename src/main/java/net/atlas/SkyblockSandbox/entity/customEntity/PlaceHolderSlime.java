package net.atlas.SkyblockSandbox.entity.customEntity;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.atlas.SkyblockSandbox.command.commands.Command_spawnmob.setAI;

public class PlaceHolderSlime {

    public static Slime spawn(Location loc) {
        Slime empty = (Slime) loc.getWorld().spawnEntity(loc.add(0D, 0D, 0D), EntityType.SLIME);
        empty.setMetadata("entity-tag", new FixedMetadataValue(SBX.getInstance(), 1));
        empty.setSize(1);
        empty.setCustomName(SUtil.colorize("&70"));
        empty.setMaxHealth(Integer.MAX_VALUE);
        setAI(empty, false);
        empty.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10, false, false));
        empty.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 10, false, false));
        return empty;
    }
}
