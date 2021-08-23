package net.atlas.SkyblockSandbox.playerIsland;

import lombok.experimental.UtilityClass;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.files.IslandInfoFile;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.WorldEditUtil;
import net.citizensnpcs.npc.ai.speech.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@UtilityClass
public class Data {
	public void initialize() {
		File userdata = new File(SBX.getInstance().getDataFolder(), File.separator + "IslandDatabase");
		File islandSchem = new File(SBX.getInstance().getDataFolder(), File.separator + "Schematics");

		userdata.mkdirs();
		islandSchem.mkdirs();
	}

	public void editData(String islandId, String dataType, Object newValue) {
		File userdata = new File(SBX.getInstance().getDataFolder(), File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + islandId + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

		try {
			playerData.set(dataType, newValue);

			playerData.save(f);
		} catch (Exception exception) {
			exception.printStackTrace();
			Bukkit.getLogger().info("Could not edit " + islandId + " data");
		}
	}

	public PlayerIsland createIsland(Player owner, IslandId islandId, Player... members) throws IOException {
		File islandsData = new File(SBX.getInstance().getDataFolder(), File.separator + "IslandDatabase");
		File f = new File(islandsData, File.separator + islandId.toString() + ".yml");
		FileConfiguration islandData = YamlConfiguration.loadConfiguration(f);

		if (!f.exists()) {
			try {
				IslandInfoFile info = new IslandInfoFile();
				double x = info.getConfiguration().getDouble("latest.location.x");
				double y = 50.0D;
				double z = info.getConfiguration().getDouble("latest.location.z");

				x += 1000.0D;

				Location loc = new Location(Bukkit.getWorld("islands"), x, y, z);

				// Paste schematic at loc;
				File schematicPath = new File(SBX.getInstance().getDataFolder(), File.separator + "Schematics");
				File schematic = new File(schematicPath, File.separator + info.getConfiguration().getString("island-schematic-file"));

				WorldEditUtil.loadSchemFromFile(schematic, loc);

				islandData.set("id", islandId.toString());
				islandData.set("owner", owner.getUniqueId().toString());

				List<String> uuids = new ArrayList<>();
				for (Player player : members)
					uuids.add(player.getUniqueId().toString());

				islandData.set("members", uuids);

				islandData.set("center.x", loc.getX());
				islandData.set("center.y", loc.getY());
				islandData.set("center.z", loc.getZ());

				String date = new SimpleDateFormat("dd/MM/yy").format(new Date());

				islandData.set("timestamp", date);

				islandData.save(f);

				info.getConfiguration().set("latest.location.x", loc.getX());
				info.getConfiguration().set("latest.location.z", loc.getZ());

				info.getConfiguration().set("latest.id", islandId.toString());
				info.getConfiguration().set("latest.date", date);

				info.save();

				new BukkitRunnable() {
					@Override
					public void run() {
						owner.teleport(loc);
						owner.playSound(owner.getLocation(), Sound.NOTE_PLING,2,1);
						owner.sendMessage(SUtil.colorize("&e&lWelcome to Skyblock Sandbox!"));
						if (members.length != 0)
							for (Player player : members) {
								player.teleport(loc);
								player.playSound(player.getLocation(), Sound.NOTE_PLING,2,1);
								player.sendMessage(SUtil.colorize("&e&lWelcome To Skyblock Sandbox!"));
							}
					}
				}.runTaskLater(SBX.getInstance(), 10);

				return new PlayerIslandHandler(islandId);
			} catch (Exception ex) {
				throw new IOException(ex);
			}
		} else {
			islandData.save(f);

			return new PlayerIslandHandler(islandId);
		}
	}

	public Object getData(String islandId, String dataType) {
		File islandsData = new File(SBX.getInstance().getDataFolder(), File.separator + "IslandDatabase");
		File f = new File(islandsData, File.separator + islandId + ".yml");
		FileConfiguration islandData = YamlConfiguration.loadConfiguration(f);

		Object requestedData = "null";

		try {
			requestedData = islandData.get(dataType);
		} catch (Exception exception) {
			Log.warn("Exception occurred while retreiving requested data from " + islandId + ".yml");
			exception.printStackTrace();
		}

		return requestedData;
	}

	public OfflinePlayer[] getMembers(String islandId) {
		File islandsData = new File(SBX.getInstance().getDataFolder(), File.separator + "IslandDatabase");
		File f = new File(islandsData, File.separator + islandId + ".yml");
		FileConfiguration islandData = YamlConfiguration.loadConfiguration(f);

		List<OfflinePlayer> members = new ArrayList<>();

		List<String> list = new ArrayList<>(islandData.getStringList("members"));
		List<UUID> uuids = new ArrayList<>();

		for (String item : list)
			uuids.add(UUID.fromString(item));

		for (UUID uuid : uuids)
			members.add(Bukkit.getOfflinePlayer(uuid));

		return members.toArray(new OfflinePlayer[uuids.size()]);
	}

	public FileConfiguration[] getAllIslandFiles() {
		File islandsData = new File(SBX.getInstance().getDataFolder(), File.separator + "IslandDatabase");

		List<FileConfiguration> list = new ArrayList<>();

		for (File f : islandsData.listFiles()) {
			FileConfiguration islandData = YamlConfiguration.loadConfiguration(f);

			list.add(islandData);
		}

		return list.toArray(new FileConfiguration[0]);
	}
}
