package net.atlas.SkyblockSandbox.storage;

import net.atlas.SkyblockSandbox.mongo.MongoDB;
import net.atlas.SkyblockSandbox.player.SBPlayer;

import java.util.*;

public class StorageCache
{
	private final static Map<UUID, String> ENDER_CHEST_PAGE_1 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_2 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_3 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_4 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_5 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_6 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_7 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_8 = new HashMap<>();
	private final static Map<UUID, String> ENDER_CHEST_PAGE_9 = new HashMap<>();

	private final static List<UUID> CACHED_PLAYERS = new ArrayList<>();

	private final SBPlayer player;

	public StorageCache(SBPlayer player) {
		this.player = player;
	}

	/**
	 * NEVER RUN THIS METHOD OMG
	 */
	@Deprecated
	public void refresh() {
		MongoDB mongoDB = new MongoStorage();

		if (mongoDB.getData(player.getUniqueId(), "enderchest_page_1") == null) {
			for (int i = 0; i < 9; i++) {
				mongoDB.setData(player.getUniqueId(), "enderchest_page_" + (i + 1), "-0-");
			}
		}

		ENDER_CHEST_PAGE_1.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_1").toString());
		ENDER_CHEST_PAGE_2.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_2").toString());
		ENDER_CHEST_PAGE_3.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_3").toString());
		ENDER_CHEST_PAGE_4.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_4").toString());
		ENDER_CHEST_PAGE_5.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_5").toString());
		ENDER_CHEST_PAGE_6.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_6").toString());
		ENDER_CHEST_PAGE_7.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_7").toString());
		ENDER_CHEST_PAGE_8.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_8").toString());
		ENDER_CHEST_PAGE_9.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_9").toString());

		CACHED_PLAYERS.add(player.getUniqueId());
	}

	/**
	 * ONLY RUN THIS METHOD WHEN PLAYER JOINS AND THEIR DATA IS NOT ALREADY CACHED!!!!
	 */
	public void refresh(int page) {
		MongoDB mongoDB = new MongoStorage();
		if (mongoDB.getData(player.getUniqueId(), "enderchest_page_1") != null) {
			switch (page) {
				case 1:
					ENDER_CHEST_PAGE_1.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_1").toString());
					break;
				case 2:
					ENDER_CHEST_PAGE_2.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_2").toString());
					break;
				case 3:
					ENDER_CHEST_PAGE_3.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_3").toString());
					break;
				case 4:
					ENDER_CHEST_PAGE_4.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_4").toString());
					break;
				case 5:
					ENDER_CHEST_PAGE_5.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_5").toString());
					break;
				case 6:
					ENDER_CHEST_PAGE_6.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_6").toString());
					break;
				case 7:
					ENDER_CHEST_PAGE_7.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_7").toString());
					break;
				case 8:
					ENDER_CHEST_PAGE_8.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_8").toString());
					break;
				case 9:
					ENDER_CHEST_PAGE_9.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_9").toString());
					break;
			}

			CACHED_PLAYERS.add(player.getUniqueId());
			return;
		}

		mongoDB.setData(player.getUniqueId(), "enderchest_page_" + page, "-0-");

		switch (page) {
			case 1:
				ENDER_CHEST_PAGE_1.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_1").toString());
				break;
			case 2:
				ENDER_CHEST_PAGE_2.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_2").toString());
				break;
			case 3:
				ENDER_CHEST_PAGE_3.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_3").toString());
				break;
			case 4:
				ENDER_CHEST_PAGE_4.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_4").toString());
				break;
			case 5:
				ENDER_CHEST_PAGE_5.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_5").toString());
				break;
			case 6:
				ENDER_CHEST_PAGE_6.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_6").toString());
				break;
			case 7:
				ENDER_CHEST_PAGE_7.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_7").toString());
				break;
			case 8:
				ENDER_CHEST_PAGE_8.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_8").toString());
				break;
			case 9:
				ENDER_CHEST_PAGE_9.put(player.getUniqueId(), mongoDB.getData(player.getUniqueId(), "enderchest_page_9").toString());
				break;
		}

		CACHED_PLAYERS.add(player.getUniqueId());
	}

	public boolean isCached() {
		return CACHED_PLAYERS.contains(player.getUniqueId());
	}

	public String getEnderChestPage(int page) {
		switch (page) {
			case 1:
				return ENDER_CHEST_PAGE_1.get(player.getUniqueId());
			case 2:
				return ENDER_CHEST_PAGE_2.get(player.getUniqueId());
			case 3:
				return ENDER_CHEST_PAGE_3.get(player.getUniqueId());
			case 4:
				return ENDER_CHEST_PAGE_4.get(player.getUniqueId());
			case 5:
				return ENDER_CHEST_PAGE_5.get(player.getUniqueId());
			case 6:
				return ENDER_CHEST_PAGE_6.get(player.getUniqueId());
			case 7:
				return ENDER_CHEST_PAGE_7.get(player.getUniqueId());
			case 8:
				return ENDER_CHEST_PAGE_8.get(player.getUniqueId());
			case 9:
				return ENDER_CHEST_PAGE_9.get(player.getUniqueId());
		}

		return "-0-";
	}

	public void setEnderChestPage(int page, String value) {
		switch (page) {
			case 1:
				ENDER_CHEST_PAGE_1.put(player.getUniqueId(), value);
				break;
			case 2:
				ENDER_CHEST_PAGE_2.put(player.getUniqueId(), value);
				break;
			case 3:
				ENDER_CHEST_PAGE_3.put(player.getUniqueId(), value);
				break;
			case 4:
				ENDER_CHEST_PAGE_4.put(player.getUniqueId(), value);
				break;
			case 5:
				ENDER_CHEST_PAGE_5.put(player.getUniqueId(), value);
				break;
			case 6:
				ENDER_CHEST_PAGE_6.put(player.getUniqueId(), value);
				break;
			case 7:
				ENDER_CHEST_PAGE_7.put(player.getUniqueId(), value);
				break;
			case 8:
				ENDER_CHEST_PAGE_8.put(player.getUniqueId(), value);
				break;
			case 9:
				ENDER_CHEST_PAGE_9.put(player.getUniqueId(), value);
				break;
		}
	}
}
