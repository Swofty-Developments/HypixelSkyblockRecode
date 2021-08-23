package net.atlas.SkyblockSandbox.playerIsland;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerIslandHandler implements PlayerIsland {
	private final IslandId id;
	private final String stringId;

	public PlayerIslandHandler(IslandId id) {
		this.id = id;
		this.stringId = id.toString();
	}

	@Override
	public OfflinePlayer[] getMembers() {
		return Data.getMembers(id.toString());
	}

	@Override
	public void addMember(OfflinePlayer member) {
		List<String> list = new ArrayList<>();

		for (OfflinePlayer player : getMembers())
			list.add(player.getUniqueId().toString());

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

		for (OfflinePlayer player : getMembers())
			list.add(player.getUniqueId().toString());

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
