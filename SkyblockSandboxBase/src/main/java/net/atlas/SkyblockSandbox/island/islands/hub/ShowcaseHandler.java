package net.atlas.SkyblockSandbox.island.islands.hub;

import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowcaseHandler {
    public static ShowcaseHandler instance;

    private final List<ItemShowcaseBlock> showCaseBlocks = new ArrayList<>();

    public ShowcaseHandler() {
        instance = this;
    }

    public ItemShowcaseBlock getBlock(int index) {
        return showCaseBlocks.get(index);
    }

    public void addBlock(ItemShowcaseBlock block) {
        showCaseBlocks.add(block);
    }

    public void removeBlock(int index) {
        showCaseBlocks.remove(index);
    }

    public int getTotalBlocks() {
        return showCaseBlocks.size();
    }

    public int getIndexOf(ItemShowcaseBlock block) {
        return showCaseBlocks.indexOf(block);
    }

    public void updateBlocks(SBPlayer p) {
        //creating temp list to sort by upvotes

        List<ItemShowcaseBlock> temp = new ArrayList<>(showCaseBlocks);
        temp.sort(new ShowcaseComparator());
        showCaseBlocks.sort(new ShowcaseComparator());

        //looping through and then getting index of sorted one and setting item
        for(ItemShowcaseBlock block:temp) {
            showCaseBlocks.get(temp.indexOf(block)).updateItem(p,block.getSbItem());
        }
    }

    public void spawnAllBlocks(SBPlayer p) {
        for(ItemShowcaseBlock block:showCaseBlocks) {
            block.spawn(p);
        }
    }




}
