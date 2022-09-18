package net.atlas.SkyblockSandbox.database.mongo;

import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import net.atlas.SkyblockSandbox.util.Serialization;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoCoins implements MongoDB {

    private static boolean connected = false;

    private static MongoCollection<Document> col;

    public MongoCoins() {

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
      /*add your database uri*/      client = MongoClients.create("mongodb+srv://atlasDevelopment:qvm347n89fugyq89@cluster0.ha64p.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

        MongoDatabase database = client.getDatabase("AtlasSandbox");
        col = database.getCollection("playerdata");
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

    public void putIfAbsent(UUID uuid, String key, Object value) {
        Document query = new Document("uuid", uuid.toString());
        Document found = col.find(query).first();

        if (found == null) {
            Document update = new Document("uuid", uuid.toString());
            update.append(key, value);

            col.insertOne(update);
            return;
        }
        if(found.get(key) == null) {
            col.updateOne(Filters.eq("uuid", uuid.toString()), Updates.set(key, value));
        }
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

    public Document getDocument(UUID uuid, String key) {

        Document query = new Document("uuid", uuid.toString());
        if (query.isEmpty()) {
            setData(uuid, key, 0D);
        } else {
            if (col.find(query).first() != null) {
                Document found = col.find(query).first();
                if (found != null) {
                    if (found.get(key) != null) {
                        return (Document) found.get(key);
                    } else {
                        setData(uuid, key, new Document());
                        Document found2 = col.find(query).first();
                        if (found2 != null)
                            return (Document) found2.get(key);
                    }
                }
            } else {
                setData(uuid, key, new Document());
                if (col.find(query).first() != null) {
                    Document found2 = col.find(query).first();
                    if (found2 != null) {
                        return (Document) found2.get(key);
                    }
                }
            }

        }
        return null;
    }

    public Document getPlayerDocument(UUID uuid) {

        Document query = new Document("uuid", uuid.toString());
        if (col.find(query).first() != null) {
            Document found = col.find(query).first();
            if (found != null) {
                return found;
            }
        }
        return query;
    }

    public void removePetData(UUID uuid, String key) {

        Document query = new Document("uuid", uuid.toString());
        Document found = col.find(query).first();
        Document update = found;

        if (found == null) return;

        update.remove("pet_" + key);
        col.updateOne(Filters.eq("pet_" + key, key), Updates.unset("pet_" + key));


    }

    public List<ItemStack> getPets(UUID uuid) {
        List<String> petStrings = new ArrayList<>();
        Document query = new Document("uuid", uuid.toString());
        if (query.isEmpty()) {
            return null;
        } else {
            if (col.find(query).first() != null) {
                Document found = col.find(query).first();
                for (String s : found.keySet()) {
                    if (s.contains("pet_")) {
                        if (found.get(s) instanceof String)
                            petStrings.add((String) found.get(s));
                    }
                }
                List<ItemStack> items = new ArrayList<>();
                for (String s : petStrings) {
                    try {
                        items.add(Serialization.itemStackFromBase64(s));
                    } catch (IOException ignored) {
                    }
                }
                return items;
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
}
