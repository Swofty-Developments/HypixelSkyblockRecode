package net.atlas.SkyblockSandbox.database.mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MongoHypixelItems {
    private static boolean connected = false;

    private static MongoCollection<Document> col;

    public MongoHypixelItems() {

    }


    public boolean isConnected() {
        return connected;
    }

    public void connect() {
        DatabaseInformationFile dbinfo = new DatabaseInformationFile();

        MongoClient client;

        if (dbinfo.getConfiguration().getBoolean("use"))
            client = MongoClients.create(dbinfo.getConfiguration().getString("uri"));
        else
          /*put your mongo uri in here*/  client = MongoClients.create("mongodb+srv://atlasDevelopment:qvm347n89fugyq89@cluster0.ha64p.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

        MongoDatabase database = client.getDatabase("AtlasSandbox");
        col = database.getCollection("HypixelItems");
        System.out.println(col);
        Bukkit.getLogger().info("Successfully connected to MongoDB!");
        connected = true;
    }

    public void setData(String id, String key, Object value) {
        Document query = new Document("ID", id.toString());
        Document found = col.find(query).first();

        if (found == null) {
            Document update = new Document("ID", id.toString());
            update.append(key, value);

            col.insertOne(update);
            return;
        }

        col.updateOne(Filters.eq("ID", id.toString()), Updates.set(key, value));
    }

    public Object getData(String id, String key) {

        Document query = new Document("ID", id.toString());
        if (query.isEmpty()) {
            setData(id, key, null);
        } else {
            if (col.find(query).first() != null) {
                Document found = col.find(query).first();
                if (found.get(key) != null) {
                    return found.get(key);
                } else {
                    setData(id, key, null);
                    Document found2 = col.find(query).first();
                    return found2.get(key);
                }

            } else {
                setData(id, key, null);
                if (col.find(query).first() != null) {
                    Document found2 = col.find(query).first();
                    return found2.get(key);
                }
            }

        }
        return null;
    }

    public boolean remove(String id) {

        Document query = new Document("ID", id.toString());
        Document found = col.find(query).first();

        if (found == null) return false;

        col.deleteOne(found);
        return true;
    }

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
    
    public void setJsonObject(JSONObject json) {
        String id = json.getString("ID");
        Document query = new Document("ID", id.toString());
        Document found = col.find(query).first();

        if (found == null) {
            System.out.println("Creating new item!");
        }

        Document update = Document.parse(json.toString());

        col.insertOne(update);
    }

    public JSONObject getJsonObject(String id) {
        Document query = new Document("ID", id.toString());
        Document found = col.find(query).first();

        if (found != null) {
            return new JSONObject(found.toJson());
        }

        return null;
    }

    public Document getDoc(String uuid) {
        Document query = new Document("ID", uuid.toString());

        return col.find(query).first();
    }

    public void setData(String id, JSONObject json) {
        Document query = new Document("ID", id);
        Document found = col.find(query).first();

        if (found == null) {
            long cms = System.currentTimeMillis();
            Log.info("Creating data for item (" + id + ")");
            setData(id, id, json);
            Log.info("Created data for \"" + id + "\" in " + (System.currentTimeMillis() - cms) + "ms!");
        } else {
            long cms = System.currentTimeMillis();
            Log.info("Updating data for item (" + id + ")");
            setData(id, id, json);
            Log.info("Updated data for \"" + id + "\" in " + (System.currentTimeMillis() - cms) + "ms!");
        }
    }
}
