package net.atlas.SkyblockSandbox.playerIsland;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public interface PlayerIsland {
	OfflinePlayer[] getMembers();

	void addMember(OfflinePlayer member);

	boolean removeMember(OfflinePlayer member);

	OfflinePlayer getOwner();

	IslandId getId();

	Location getCenter();
}
