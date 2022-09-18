package net.atlas.SkyblockSandbox.database.mongo;

import com.mongodb.Mongo;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import net.atlas.SkyblockSandbox.util.Serialization;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoAH implements MongoDB {
    private static boolean connected = false;

    private static MongoCollection<Document> col;

    public MongoAH() {

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
       /*add your database uri*/     client = MongoClients.create("mongodb+srv://atlasDevelopment:qvm347n89fugyq89@cluster0.ha64p.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

        MongoDatabase database = client.getDatabase("AtlasSandbox");
        col = database.getCollection("auctions");
        System.out.println(col);
        Bukkit.getLogger().info("Successfully connected to MongoDB!");
        connected = true;
    }

    @Override
    public void setData(UUID uuid, String key, Object value) {
        Document query = new Document("auctionID", uuid.toString());
        Document found = col.find(query).first();

        if (found == null) {
            Document update = new Document("auctionID", uuid.toString());
            update.append(key, value);

            col.insertOne(update);
            return;
        }

        col.updateOne(Filters.eq("auctionID", uuid.toString()), Updates.set(key, value));
    }

    @Override
    public Object getData(UUID uuid, String key) {

        Document query = new Document("auctionID", uuid.toString());
        if (query.isEmpty()) {
            setData(uuid, key, null);
        } else {
            if (col.find(query).first() != null) {
                Document found = col.find(query).first();
                if (found.get(key) != null) {
                    return found.get(key);
                } else {
                    setData(uuid, key, null);
                    Document found2 = col.find(query).first();
                    return found2.get(key);
                }

            } else {
                setData(uuid, key, null);
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

        Document query = new Document("auctionID", uuid.toString());
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
    public Document getDoc(UUID uuid) {
        Document query = new Document("auctionID", uuid.toString());

        return col.find(query).first();
    }

    public void setData(AuctionItemHandler item) {
        UUID id = item.getAuctionID();
        Document query = new Document("auctionID", item.getAuctionID());
        Document found = col.find(query).first();

        if (found == null) {

            long cms = System.currentTimeMillis();
            Log.info("Creating data for auction(" + item.getAuctionID() + ")");

            setData(id, "item", item.getItemStack());
            setData(id, "startTime", item.getStartTime().toInstant().toEpochMilli());
            setData(id, "endTime", item.getEndTime().toInstant().toEpochMilli());
            setData(id, "hasEnded", item.isHasEnded());
            setData(id, "isClaimed", item.isClaimed());
            setData(id, "startingPrice", item.getStartingPrice());
            setData(id, "currentPrice", item.getCurrentPrice());
            setData(id, "highestBidder", item.getHighestBidder() != null ? item.getHighestBidder().toString() : "null");
            setData(id, "isBin", item.isBin());
            setData(id, "category", item.getCategory().name());
            setData(id, "owner", item.getOwner().toString());

            Log.info("Created data for \"" + id + "\" in " + (System.currentTimeMillis() - cms) + "ms!");
        } else {
            long cms = System.currentTimeMillis();
            Log.info("Updated data for auction(" + item.getAuctionID() + ")");

            setData(id, "item", item.getItemStack());
            setData(id, "startTime", item.getStartTime().toInstant().toEpochMilli());
            setData(id, "endTime", item.getEndTime().toInstant().toEpochMilli());
            setData(id, "hasEnded", item.isHasEnded());
            setData(id, "isClaimed", item.isClaimed());
            setData(id, "startingPrice", item.getStartingPrice());
            setData(id, "currentPrice", item.getCurrentPrice());
            setData(id, "highestBidder", item.getHighestBidder() != null ? item.getHighestBidder().toString() : "null");
            setData(id, "isBin", item.isBin());
            setData(id, "category", item.getCategory().name());
            setData(id, "owner", item.getOwner().toString());

            Log.info("Updated data for \"" + id + "\" in " + (System.currentTimeMillis() - cms) + "ms!");
        }
    }
}
