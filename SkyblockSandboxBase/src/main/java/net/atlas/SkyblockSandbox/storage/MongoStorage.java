package net.atlas.SkyblockSandbox.storage;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.atlas.SkyblockSandbox.database.mongo.MongoDB;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoStorage implements MongoDB
{
	private static boolean connected = false;

	private static MongoCollection<Document> col;

	public MongoStorage() {

	}


	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void connect() {
		DatabaseInformationFile dbinfo = new DatabaseInformationFile();

		MongoClient client;

		if (dbinfo.getConfiguration().getBoolean("use"))
			client = MongoClients.create(dbinfo.getConfiguration().getString("uri"));
		else
			client = MongoClients.create("mongodb+srv://atlasDevelopment:qvm347n89fugyq89@cluster0.ha64p.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

		MongoDatabase database = client.getDatabase("AtlasSandbox");
		col = database.getCollection("storage_data");
		System.out.println(col);
		Bukkit.getLogger().info("Successfully connected to MongoDB!");
		connected = true;
	}

	@Override
	public void setData(UUID uuid, String key, Object value) {
		Document query = new Document("uuid", uuid.toString());
		Document found = col.find(query).first();

		if (found == null) {
			Document update = new Document("uuid", uuid.toString());
			update.append(key, value);

			col.insertOne(update);
			return;
		}

		col.updateOne(Filters.eq("uuid", uuid.toString()), Updates.set(key, value));
	}

	@Override
	public Object getData(UUID uuid, String key) {

		Document query = new Document("uuid", uuid.toString());
		if (query.isEmpty()) {
			setData(uuid, key, 0D);
		} else {
			if (col.find(query).first() != null) {
				Document found = col.find(query).first();
				if (found.get(key) != null) {
					return found.get(key);
				} else {
					setData(uuid, key, 0D);
					Document found2 = col.find(query).first();
					return found2.get(key);
				}

			} else {
				setData(uuid, key, 0D);
				if (col.find(query).first() != null) {
					Document found2 = col.find(query).first();
					return found2.get(key);
				}
			}

		}
		return null;
	}

	@Override
	public boolean remove(UUID uuid) {

		Document query = new Document("uuid", uuid.toString());
		Document found = col.find(query).first();

		if (found == null) return false;

		col.deleteOne(found);
		return true;
	}

	@Override
	public List<Document> getAllDocuments() {
		FindIterable<Document> docs = col.find();
		MongoCursor<Document> cursor = docs.iterator();

		List<Document> found = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				found.add(cursor.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return found;
	}
	public void setPlayerData(String id) {
		Document query = new Document("uuid", id);
		Document found = col.find(query).first();

		if (found != null) return;

		long cms = System.currentTimeMillis();
		Log.warn("Data for \"" + id + "\" not found! Creating data for this player...");

		UUID id2 = UUID.fromString(id);

		setData(id2, "enderchest_page_1", null);
		setData(id2, "enderchest_page_2", null);
		setData(id2, "enderchest_page_3", null);
		setData(id2, "enderchest_page_4", null);
		setData(id2, "enderchest_page_5", null);
		setData(id2, "enderchest_page_6", null);
		setData(id2, "enderchest_page_7", null);
		setData(id2, "enderchest_page_8", null);
		setData(id2, "enderchest_page_9", null);


		Log.info("Created data for \"" + id + "\" in " + (System.currentTimeMillis() - cms) + "ms!");
	}
}
