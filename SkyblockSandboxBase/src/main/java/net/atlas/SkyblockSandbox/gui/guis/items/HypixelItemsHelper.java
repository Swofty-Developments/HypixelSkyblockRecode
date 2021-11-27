package net.atlas.SkyblockSandbox.gui.guis.items;

import com.google.gson.Gson;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoHypixelItems;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HypixelItemsHelper {
    public static ArrayList<SBItemBuilder> hypixelItems = new ArrayList<>();
    public static void cacheItems() {
        SBX.getInstance().hypixelItems.getAllDocuments().forEach(doc -> hypixelItems.add(new Gson().fromJson(doc.toJson(), SBItemBuilder.class)));
    }
    public static void cacheToMongo() {
        hypixelItems.forEach(item -> SBX.getInstance().hypixelItems.setJsonObject(new JSONObject(new Gson().toJson(item))));
    }
    public static SBItemBuilder getItem(String id) {
        for (SBItemBuilder item : hypixelItems) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }
}
