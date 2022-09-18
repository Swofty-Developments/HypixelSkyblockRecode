package net.atlas.SkyblockSandbox.island.islands.hub;

import java.util.Comparator;
import java.util.Random;

public class ShowcaseComparator implements Comparator<ItemShowcaseBlock> {

    @Override
    public int compare(ItemShowcaseBlock o1, ItemShowcaseBlock o2) {
        return Integer.compare(o2.getVotes(), o1.getVotes());
    }
}
