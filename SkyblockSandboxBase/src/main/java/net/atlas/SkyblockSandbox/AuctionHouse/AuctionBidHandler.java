package net.atlas.SkyblockSandbox.AuctionHouse;

import lombok.Getter;
import lombok.Setter;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import org.bson.Document;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuctionBidHandler {
    public static HashMap<UUID, ArrayList<AuctionBidHandler>> bids = new HashMap<>();
    public static MongoAH mongo = new MongoAH();
    public final UUID uuid;
    public final String name;
    public final ZonedDateTime time;
    public final double price;

    public AuctionBidHandler(UUID uuid, String name, ZonedDateTime time, double price) {
        this.uuid = uuid;
        this.name = name;
        this.time = time;
        this.price = price;
    }

    public void addBid(UUID auctionID) {
        ArrayList<AuctionBidHandler> list = AuctionItemHandler.ITEMS.get(auctionID).getBids();
        list.add(this);
        bids.put(auctionID, list);
    }

    public static void cacheToMongo(UUID auctionID) {
        ArrayList<Document> list = new ArrayList<>();
        for (AuctionBidHandler bid : bids.get(auctionID)) {
            list.add(new Document(bid.getUuid().toString(), new Document(bid.getName(), new Document(String.valueOf(bid.time.toInstant().toEpochMilli()), bid.price))));
        }
        mongo.setData(auctionID, "bids", list);
    }
}
