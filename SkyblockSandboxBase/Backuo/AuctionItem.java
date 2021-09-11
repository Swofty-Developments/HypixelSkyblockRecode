package net.atlas.SkyblockSandbox.AuctionHouse;

import net.atlas.SkyblockSandbox.AuctionHouse.gui.AuctionBrowserGUI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface AuctionItem
{
	UUID getId();

	OfflinePlayer getOwner();

	AuctionItem save();

	ItemStack getStack();

	int getPrice();

	boolean isBIN();

	void remove();

	boolean isEnded();

	boolean isBought();

	void setBought(boolean b);

	void setEnded(boolean b);

	void setBuyer(String uuid);

	String getBuyer();

	int getHighestBid();

	OfflinePlayer getHighestBidder();

	void setHighestBid(int i);

	void setHighestBidder(Player player);

	long getTimeEnding();

	AuctionBrowserGUI.AuctionCategory getCategory();
}
