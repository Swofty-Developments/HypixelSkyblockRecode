package net.atlas.SkyblockSandbox.gui.guis.backpacks;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.BackpackSize;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.Serialization;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;

public class Backpack extends NormalGUI {
    private BackpackSize size;
    private ItemStack backpack;

    public Backpack(SBPlayer owner, BackpackSize size, ItemStack backpack) {
        super(owner);
        this.size = size;
        this.backpack = backpack;
        Bukkit.getServer().getPluginManager().registerEvents(new BackpackHandler(this,backpack),SBX.getInstance());
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        String name = NBTUtil.getString(event.getCurrentItem(), "ID");
        event.setCancelled(name.toLowerCase().contains("backpack"));
        if(event.getAction()== InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        return size.getName() + " Backpack";
    }

    @Override
    public int getRows() {
        return size.getGUISize() / 9;
    }

    @Override
    public void setItems() {
        if (NBTUtil.getBytes(backpack, "stored-items")==null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(backpack);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            String bpUUID = data.getString("UUID");
            int i = 0;

            try {
                FileConfiguration config = SBX.getInstance().getConfig();
                boolean redisConnect = config.getBoolean("Redis.use-redis");
                if (!redisConnect) {
                    getOwner().sendMessage(ChatColor.RED + "Backpacks are currently disabled!");
                } else {
                    String host = config.getString("Redis.redishost");
                    int port = config.getInt("Redis.redisport");
                    String password = config.getString("Redis.redispass");
                    Jedis j = new Jedis(host, port);
                    if (!(password.equals(""))) {
                        j.auth(password);
                    }
                    if (!j.hexists("Backpack", bpUUID)) {
                        return;
                    }
                    String serializedBackPack = j.hget("Backpack", bpUUID);
                    Inventory backpacktoSet = Serialization.fromBase64(serializedBackPack);
                    if (backpacktoSet.getContents() == null) {
                    } else {
                        ItemStack[] backpackItems = new ItemStack[backpacktoSet.getSize()];
                        for (ItemStack item : backpacktoSet.getContents()) {
                            backpackItems[i] = item;
                            i++;
                        }
                        getGui().setInventory(backpacktoSet);
                        j.close();
                    }
                }
            } catch (IOException e) {
                FileConfiguration config = SBX.getInstance().getConfig();
                config.set("Redis.use-redis", false);
                e.printStackTrace();
            }
        } else {
            try {
                String contents = new String(NBTUtil.getBytes(backpack, "stored-items"));
                if (contents.equals("0000")) {
                    return;
                }
                Inventory toSet = Serialization.fromBase64(contents);
                if (toSet.getContents() != null) {
                    int i=0;
                    for(ItemStack it:toSet.getContents()) {
                        if(it!=null&&it.getType()!= Material.AIR) {
                            setItem(i, it);
                        }
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                getOwner().sendMessage(SUtil.colorize("&cUh oh! Something went wrong. Please contact a server administrator."));
                getOwner().closeInventory();
            }
        }

    }

    public void closeInv(InventoryCloseEvent event) {
        String serialized = Serialization.toBase64(event.getInventory());
        byte[] byteArray = serialized.getBytes();
        ItemStack newBp = NBTUtil.setBytes(backpack, byteArray, "stored-items");
        getOwner().setItemInHand(newBp);
    }

}
