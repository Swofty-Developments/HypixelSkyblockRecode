package net.atlas.SkyblockSandbox.gui.guis.items;

import com.google.gson.Gson;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoHypixelItems;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.util.BukkitSerilization;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bson.Document;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HypixelItemsHelper {
    public static ArrayList<SBItemBuilder> hypixelItems = new ArrayList<>();
    public static void cacheItems() {
        SBX.getInstance().hypixelItems.getAllDocuments().forEach(doc -> {
            hypixelItems.add(new SBItemBuilder(BukkitSerilization.itemStackFromBase64(doc.getString("item"))));
        });
    }
    public static void cacheToMongo() {
        hypixelItems.forEach(item -> {
            SBX.getInstance().hypixelItems.setData(item.id, "item", BukkitSerilization.itemStackToBase64(item.build()));
        });
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
