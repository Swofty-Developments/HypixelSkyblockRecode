package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;


public class InstantTransmission {} /*extends BaseAbility {
    public InstantTransmission(Player player, int damage, int cooldown, int manaCost, int speed) {
        super(player, damage, cooldown, manaCost, speed);
    }

    @Override
    public void run() {
        ArrayList<Block> blocks = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, 10);
        int range = 8;
        for (int i = 0; i < blocks.size(); i++)
        {
            Block block = blocks.get(i);
            if (block.getType() != Material.AIR || block.isLiquid())
            {
                if (i == 0 || i == 1)
                {
                    range = 0;
                    return;
                }
                range = i - 1;
                break;
            }
        }
        Block b = blocks.get(range);
        Location loc = new Location(b.getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5, player.getLocation().getYaw(),
                player.getLocation().getPitch());
        player.teleport(loc);
        if (ItemUtilities.getIntFromItem(player.getItemInHand(), "has_teleported") == 0) {
            player.setWalkSpeed(player.getWalkSpeed() + (speed/200F));
            ItemUtilities.storeIntInItem(player.getItemInHand(), 1, "has_teleported");
            ItemUtilities.scheduleTask(new Runnable() {
                public void run() {
                    player.setWalkSpeed(player.getWalkSpeed() - (speed/200F));
                    ItemUtilities.storeIntInItem(player.getItemInHand(), 0, "has_teleported");
                }
            }, 60);
        }
    }
}*/
