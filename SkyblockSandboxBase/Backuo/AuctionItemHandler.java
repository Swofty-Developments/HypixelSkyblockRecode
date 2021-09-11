package net.atlas.SkyblockSandbox.AuctionHouse;

import net.atlas.SkyblockSandbox.AuctionHouse.gui.AuctionBrowserGUI;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AuctionItemHandler implements AuctionItem {
	private final static MongoDB DB = new MongoAH();

	private final int price;
	private final OfflinePlayer owner;
	private final ItemStack stack;
	private final boolean isBIN;
	private final long timeEnding;
	private final UUID id;

	public AuctionItemHandler(UUID id, int price, OfflinePlayer owner, ItemStack stack, boolean isBIN, long timeEnding) {
		this.price = price;
		this.owner = owner;
		this.stack = stack;
		this.isBIN = isBIN;
		this.timeEnding = timeEnding;
		this.id = id;
	}

	@Override
	public UUID getId() {
		return this.id != null ? id : UUID.randomUUID();
	}

	@Override
	public OfflinePlayer getOwner() {
		return this.owner;
	}

	@Override
	public AuctionItem save() {
		DB.setData(this.getId(), "owner", this.getOwner().getUniqueId().toString());
		DB.setData(this.getId(), "bin", this.isBIN());
		DB.setData(this.getId(), "price", this.getPrice());
		DB.setData(this.getId(), "top-bid-amount", this.getHighestBid());
		DB.setData(this.getId(), "end-time", this.getTimeEnding());
		DB.setData(this.getId(), "top-bidder", (this.getHighestBidder() == null ? "none" : this.getHighestBidder().getUniqueId().toString()));
		DB.setData(this.getId(), "item-stack", BukkitSerilization.itemStackToBase64(this.getStack()));

		SBX.getInstance().getLogger().info("Saved auction with id " + this.getId() + "!");

		return this;
	}

	@Override
	public ItemStack getStack() {
		return this.stack;
	}

	@Override
	public OfflinePlayer getHighestBidder() {
		try {
			return Bukkit.getOfflinePlayer(UUID.fromString(DB.getData( this.id, "top-bidder").toString()));
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void setHighestBid(int i) {
		DB.setData(this.getId(), "top-bid-amount", i);
	}

	@Override
	public void setHighestBidder(Player player) {
		DB.setData(this.getId(), "top-bidder", player.getUniqueId().toString());
	}

	@Override
	public int getPrice() {
		return this.price;
	}

	@Override
	public boolean isBIN() {
		return this.isBIN;
	}

	@Override
	public void remove() {
		AuctionBrowserGUI.ITEMS.remove(this);
		for (AuctionItem item : AuctionBrowserGUI.ITEMS) {
			if (item.getId() == this.getId()) {
				AuctionBrowserGUI.ITEMS.remove(item);
				break;
			}
		}
		DB.remove(this.getId());
	}

	@Override
	public boolean isEnded() {
		if (DB.getData(this.getId(), "ended") == null) return false;

		return Boolean.parseBoolean(DB.getData(this.getId(), "ended").toString());
	}

	@Override
	public boolean isBought() {
		if (DB.getData(this.getId(), "bought") == null) return false;

		return Boolean.parseBoolean(DB.getData(this.getId(), "bought").toString());
	}

	@Override
	public void setBought(boolean b) {
		DB.setData(this.getId(), "bought", String.valueOf(b));
	}

	@Override
	public void setEnded(boolean b) {
		DB.setData(this.getId(), "ended", String.valueOf(b));
	}

	@Override
	public void setBuyer(String uuid) {
		DB.setData(this.getId(), "buyer", uuid);
	}

	@Override
	public String getBuyer() {
		if (DB.getData(this.getId(), "buyer") == null) return null;

		return DB.getData(this.getId(), "buyer").toString();
	}

	@Override
	public int getHighestBid() {
		return this.price;
	}

	@Override
	public long getTimeEnding() {
		return this.timeEnding;
	}

	@Override
	public AuctionBrowserGUI.AuctionCategory getCategory() {
		String e = stack.getType().toString().toLowerCase();

		if (e.contains("helmet")) {
			return AuctionBrowserGUI.AuctionCategory.ARMOR;
		} else if (e.contains("chestplate")) {
			return AuctionBrowserGUI.AuctionCategory.ARMOR;
		} else if (e.contains("leggings")) {
			return AuctionBrowserGUI.AuctionCategory.ARMOR;
		} else if (e.contains("boots")) {
			return AuctionBrowserGUI.AuctionCategory.ARMOR;
		} else if (e.contains("skull")) {
			return AuctionBrowserGUI.AuctionCategory.ACCESSORIES;
		} else if (stack.getType().isEdible()) {
			return AuctionBrowserGUI.AuctionCategory.CONSUMABLES;
		} else if (e.contains("sword") || e.contains("bow")) {
			return AuctionBrowserGUI.AuctionCategory.WEAPONS;
		} else if (stack.getType().isBlock()) {
			return AuctionBrowserGUI.AuctionCategory.BLOCKS;
		} else {
			return AuctionBrowserGUI.AuctionCategory.TOOLS_MISC;
		}
	}
	public static class AuctionData {
		public ItemStack put(ItemStack item, String key, String value) {
			net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
			NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
			NBTTagCompound data = tag.getCompound("AuctionData");
			data.setString(key, value);
			data.setString("ah-item", "1");
			tag.set("AuctionData", data);
			nmsItem.setTag(tag);

			return CraftItemStack.asBukkitCopy(nmsItem);
		}

		public String getString(ItemStack item, String key) {
			if(item != null) {
				net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
				NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
				NBTTagCompound data = tag.getCompound("AuctionData");
				if(data == null) return  null;

				return data.getString(key);
			}
			return null;
		}

		public int getInt(ItemStack item, String key) {
			if(item != null) {
				net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
				NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
				NBTTagCompound data = tag.getCompound("AuctionData");
				if(data == null) return  0;

				return data.getInt(key);
			}
			return 0;
		}

		public boolean isAuctionedItem(ItemStack item) {
			if(item != null) {
				net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
				NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
				NBTTagCompound data = tag.getCompound("AuctionData");
				if(data == null) return false;

				return data.hasKey("ah-item");
			}
			return false;
		}

		public ItemStack remove(ItemStack stack) {
			net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
			NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
			tag.remove("AuctionData");
			nmsItem.setTag(tag);

			return CraftItemStack.asBukkitCopy(nmsItem);
		}

		public ItemStack put(ItemStack item, String key, int value) {
			net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
			NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
			NBTTagCompound data = tag.getCompound("AuctionData");
			data.setInt(key, value);
			data.setString("ah-item", "1");
			tag.set("AuctionData", data);
			nmsItem.setTag(tag);

			return CraftItemStack.asBukkitCopy(nmsItem);
		}
	}
}
