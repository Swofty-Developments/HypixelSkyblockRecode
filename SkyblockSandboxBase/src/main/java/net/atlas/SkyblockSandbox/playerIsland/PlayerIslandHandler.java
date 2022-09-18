package net.atlas.SkyblockSandbox.playerIsland;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerIslandHandler implements PlayerIsland {
	private final IslandId id;
	private final String stringId;


	public PlayerIslandHandler(IslandId id) {
		this.id = id;
		this.stringId = id.toString();
		new BukkitRunnable() {
			@Override
			public void run() {
				for(OfflinePlayer player : getMembers()) {
					if(player.isOnline()) {
						Player pl = player.getPlayer();
						if(pl.getLocation().getWorld()==getCenter().getWorld()) {
							if (pl.getLocation().distance(getCenter()) > dist()) {
								Location teleLoc = getCenter();
								while (teleLoc.getBlock().getType()!= Material.AIR) {
									teleLoc.add(0,1,0);
								}
								pl.teleport(teleLoc);
								pl.sendMessage(SUtil.colorize("&cYou cannot travel more than " + dist() + " blocks in that direction!"));
							}
						}
					}
				}
				if(getOwner().isOnline()) {
					Player pl = getOwner().getPlayer();
					if (!pl.hasPermission("sbx.admin")) {
						if (pl.getLocation().getWorld() == getCenter().getWorld()) {
							if (pl.getLocation().distance(getCenter()) > dist()) {
								Location teleLoc = getCenter();
								while (teleLoc.getBlock().getType() != Material.AIR) {
									teleLoc.add(0, 1, 0);
								}
								pl.teleport(teleLoc);
								pl.sendMessage(SUtil.colorize("&cYou cannot travel more than " + dist() + " blocks in that direction!"));
							}
						}
					}
				}
			}
		}.runTaskTimer(SBX.getInstance(),20L,20L);
	}

	public static int dist() {
		return 100;
	}

	@Override
	public OfflinePlayer[] getMembers() {
		return Data.getMembers(id.toString());
	}

	@Override
	public void addMember(OfflinePlayer member) {
		List<String> list = new ArrayList<>();

		if (Data.getMembers(id.toString()) != null) {
			for (OfflinePlayer player : getMembers())
				list.add(player.getUniqueId().toString());
		}

		if (!list.contains(member.getUniqueId().toString())) {
			list.add(member.getUniqueId().toString());

			Data.editData(stringId, "members", list);
			Log.info("Added " + member.getName() + " to " + stringId);

			return;
		}
		throw new IllegalStateException("Member already exists!");
	}

	@Override
	public boolean removeMember(OfflinePlayer member) {
		List<String> list = new ArrayList<>();

		if (getMembers() != null) {
			for (OfflinePlayer player : getMembers())
				list.add(player.getUniqueId().toString());
		}

		if (list.contains(member.getUniqueId().toString())) {
			list.remove(member.getUniqueId().toString());

			Data.editData(stringId, "members", list);
			Log.info("Removed " + member.getName() + " from " + stringId);

			return true;
		}
		throw new IllegalStateException("Member does not exist!");
	}

	@Override
	public OfflinePlayer getOwner() {
		return Bukkit.getOfflinePlayer(UUID.fromString(Data.getData(id.toString(), "owner").toString()));
	}

	@Override
	public IslandId getId() {
		return this.id;
	}

	@Override
	public Location getCenter() {
		double x = Double.parseDouble(Data.getData(stringId, "center.x").toString());
		double y = Double.parseDouble(Data.getData(stringId, "center.y").toString());
		double z = Double.parseDouble(Data.getData(stringId, "center.z").toString());

		return new Location(Bukkit.getWorld("islands"), x, y, z);
	}
}
