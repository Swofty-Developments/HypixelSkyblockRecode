package net.atlas.SkyblockSandbox.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.Set;

public class TeleportAbility {

    public Block getTeleportBlock(Player player, Set<Material> transparent, int range) {
        BlockIterator iter = new BlockIterator(player, range);

        Block lastBlock;
        Block previousBlock;
        for (previousBlock = null; iter.hasNext(); previousBlock = lastBlock) {
            lastBlock = iter.next();
            if (lastBlock.getType() != Material.AIR || !(transparent.contains(lastBlock.getType()))) {
                break;
            }
        }

        return previousBlock;
    }

}
