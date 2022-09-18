package net.atlas.SkyblockSandbox.gui.guis;

import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.island.islands.hub.ItemShowcaseBlock;
import net.atlas.SkyblockSandbox.island.islands.hub.ShowcaseHandler;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShowcaseGUI extends NormalGUI {


    private ItemShowcaseBlock block;
    private SBItemStack showItem;
    private int itemVotes;

    public ShowcaseGUI(SBPlayer owner, int index) {
        super(owner);
        this.block = ShowcaseHandler.instance.getBlock(index);
        this.showItem = this.block.getSbItem();
        this.itemVotes = this.block.getVotes();
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(21,event -> {
            this.block.upvote();
        });
        setAction(23,event -> {
            this.block.downvote();
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Item Showcase";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        String itemVotesStr = (itemVotes > 0 ? "&a" : "&c") + itemVotes;
        setMenuGlass();
        setItem(13,showItem.asBukkitItem());

        setItem(21,makeColorfulItem(Material.STAINED_CLAY,"&aUpvote",1, DyeColor.GREEN.getData(),"","&eClick to upvote this item!"));
        setItem(22,makeColorfulItem(Material.GOLD_BLOCK,"&aScore: " + itemVotesStr,1,0,"&7The total score an item has!","&7Clicking upvote/downvote will","&7affect the items total score!"));
        setItem(23,makeColorfulItem(Material.STAINED_CLAY,"&cDownvote",1,DyeColor.RED.getData(),"","&eClick to downvote this item!"));

    }
}
