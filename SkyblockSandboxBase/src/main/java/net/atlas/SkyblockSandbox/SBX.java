package net.atlas.SkyblockSandbox;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.customMining.BreakListener;
import net.atlas.SkyblockSandbox.customMining.MineUtil;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.database.sql.MySQL;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.entity.SkyblockEntity;
import net.atlas.SkyblockSandbox.event.customEvents.ManaEvent;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.files.CfgFile;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import net.atlas.SkyblockSandbox.files.IslandInfoFile;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.item.ItemType;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.ability.AbiltyListener;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.ShortBowTerm;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.WitherImpact;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.MongoIslands;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.storage.MongoStorage;
import net.atlas.SkyblockSandbox.util.HypixelColorCodes;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.atlas.SkyblockSandbox.util.signGUI.SignManager;
import net.minecraft.server.v1_8_R3.*;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static net.atlas.SkyblockSandbox.command.commands.Command_forward.MESSAGE_CHANNEL;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap2;

public class SBX extends JavaPlugin {
    public static HashMap<UUID, Boolean> isSoulCryActive = new HashMap<>();
    public static HashMap<String, ItemStack> storedItem = new HashMap<>();
    public static HashMap<UUID, HashMap<Slayers, HashMap<SlayerTier, Double>>> activeSlayers = new HashMap<>();
    public static HashMap<UUID, HashMap<SkillType, Double>> cachedSkills = new HashMap<>();
    public static HashMap<UUID, HashMap<SkillType, Integer>> cachedSkillLvls = new HashMap<>();
    public static HashMap<UUID, SkillEXPGainEvent> prevSkillEvent = new HashMap<>();
    public static LinkedHashMap<UUID, LinkedHashMap<String, Long>> queuedBarMessages = new LinkedHashMap<>();
    public static HashMap<EntityArmorStand, Integer> cooldownMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Integer> counterMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Vector> vMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Integer> angleMap = new HashMap<>();
    public static BukkitTask prevRunnable = null;
    public static Map<Player, Boolean> abilityUsed = new HashMap<>();
    public static HashMap<Player, Boolean> canfire = new HashMap<>();
    public static HashMap<Player, List<EntityArmorStand>> thrownAxes = new HashMap<>();
    public static final TreeMap<String, ItemStack> hypixelItemMap = new TreeMap<>();
    public static HashMap<UUID,Document> cachedPets = new HashMap<>();
    public static boolean pvpEnabled;
    public MySQL sql;
    public SignManager signManager;

    private static SBX instance;
    SkyblockCommandFramework framework;
    private static MongoCoins mongoStats;
    private MongoIslands mongoIslands;
    public MongoStorage mongoStorage;
    public Coins coins;
    private static ProtocolManager protocolManager;

    private File dragonDataFile;
    private FileConfiguration dragonData;

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        instance = this;
        mongoStorage = new MongoStorage();
        mongoIslands = new MongoIslands();
        framework = new SkyblockCommandFramework(this);
        createDataFiles();
        mongoStats = new MongoCoins();
        mongoStats.connect();
        protocolManager = ProtocolLibrary.getProtocolManager();
        /*MongoAH mongoAH = new MongoAH();
        mongoAH.connect();
        long time = System.currentTimeMillis();
        System.out.println("Starting to cache all ah data....");
        try {
            for (Document doc : new MongoAH().getAllDocuments()) {
                AuctionItemHandler item = AuctionItemHandler.mongoToCache(UUID.fromString(doc.getString("auctionID")));
                if(AuctionItemHandler.time(item.getEndTime(), item.getStartTime()).equals("")) {
                    item.setHasEnded(true);
                    mongoAH.setData(item.getAuctionID(), "isClaimed", false);
                    item.setClaimed(false);
                }
                if (!item.isClaimed()) {
                    AuctionItemHandler.ITEMS.put(item.getAuctionID(), item);
                }
            }
            System.out.println("Successfully cached all ah data! (" + (System.currentTimeMillis() - time) + " ms)");
        } catch (Exception e) {
            System.err.println("Failed to cache all ah data! (" + (System.currentTimeMillis() - time) + " ms)");
            e.printStackTrace();
        }

         */
        mongoStorage.connect();
        mongoIslands.connect();
        coins = new Coins();

        Data.initialize();
        signManager = new SignManager(this);
        signManager.init();
        registerListeners();
        registerCommands();
        createIslandWorld();
        githubItems();

        startOnlineRunnables();
        createDataFiles();

        MineUtil.setupPacketListeners();


        //registerEntity("Enderman", 58, EntityZombie.class, NoTeleportEnderman.class);
        SkyblockEntity.registerEntities();
        getServer().getMessenger().registerOutgoingPluginChannel(this, MESSAGE_CHANNEL);
    }

    @Override
    public void onDisable() {
        cacheSkills();
        cachePets();
    }

    public static void cacheSkills() {

        for (UUID uid : cachedSkills.keySet()) {
            Document playerDoc = SBX.getMongoStats().getPlayerDocument(uid);
            Document skillDoc = (Document) playerDoc.get("Skills");
            for (SkillType type : cachedSkills.get(uid).keySet()) {
                double amt = cachedSkills.get(uid).get(type);
                skillDoc.put(type.getName() + "_xp",amt);
            }
            for (SkillType type : cachedSkillLvls.get(uid).keySet()) {
                int amt = cachedSkillLvls.get(uid).get(type);
                skillDoc.put(type.getName() + "_lvl",amt);
            }
            SBX.getMongoStats().setData(uid,"Skills",skillDoc);
        }
    }

    public static void cachePets() {
        for (UUID uid : cachedPets.keySet()) {
            getMongoStats().setData(uid,"pets",cachedPets.get(uid));
        }
    }

    void registerListeners() {
        SkyblockListener.registerListeners();
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new AbiltyListener(new SoulCry()), this);
        pm.registerEvents(new AbiltyListener(new HellShatter()), this);
        pm.registerEvents(new AbiltyListener(new WitherImpact()), this);
        pm.registerEvents(new AbiltyListener(new ShortBowTerm()), this);
        pm.registerEvents(new LootListener(), this);
        pm.registerEvents(new BreakListener(),this);
    }

    void registerCommands() {
        framework.registerAllCommands();
        framework.registerHelp();
    }

    void createIslandWorld() {
        WorldCreator wc = new WorldCreator("islands");
        wc.type(WorldType.FLAT);
        wc.generatorSettings("2;0;1");
        wc.createWorld();
    }

    void createDataFiles() {
        createDragonDataFile();
        new DatabaseInformationFile().create();
        new IslandInfoFile().create();
        new CfgFile().create();
    }

    public static ProtocolManager getProtcolManager() {
        return protocolManager;
    }

    void startOnlineRunnables() {
        MongoAH mongo = new MongoAH();
        BukkitTask updateAH = new BukkitRunnable() {
            @Override
            public void run() {
                for (Document doc : new MongoAH().getAllDocuments()) {
                    AuctionItemHandler item = AuctionItemHandler.mongoToCache(UUID.fromString(doc.getString("auctionID")));
                    if(AuctionItemHandler.time(item.getEndTime(), item.getStartTime()).equals("")) {
                        item.setHasEnded(true);
                        mongo.setData(item.getAuctionID(), "isClaimed", false);
                        item.setClaimed(false);
                    }
                    if (!item.isClaimed()) {
                        AuctionItemHandler.ITEMS.put(item.getAuctionID(), item);
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0, 100);
        updateAH.cancel();

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!holoMap2.isEmpty()) {
                    HashMap<Integer, HashMap<Entity, EntityArmorStand>> holoMapClone = new HashMap<>(holoMap2);
                    for (int x : holoMapClone.keySet()) {
                        Entity entity = holoMap2.get(x).keySet().stream().findFirst().orElse(null);
                        EntityArmorStand stand = holoMap2.get(x).get(entity);
                        assert entity != null;
                        //stand.setLocation(entity.getLocation().getX(),entity.getLocation().getY()+1,entity.getLocation().getZ(),0,0);
                        //PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(stand);

                    }
                }
                if (!holoMap.isEmpty()) {
                    HashMap<Entity, ArmorStand> holoMapClone = new HashMap<>(holoMap);
                    for (Entity en : holoMapClone.keySet()) {
                        if (en instanceof LivingEntity) {
                            ArmorStand as = holoMap.get(en);
                            //as.teleport(en.getLocation().add(0, 1D, 0));
                            StringBuilder builder = new StringBuilder();
                            Random random = new Random();
                            builder.append(as.getCustomName(), 0, 15);
                            if (en.getCustomName() != null) {
                                builder.append(SUtil.colorize(" &c" + en.getCustomName()));
                            } else {
                                builder.append(SUtil.colorize(" &c" + en.getName()));
                            }

                            DecimalFormat format = new DecimalFormat("#");
                            if (en.hasMetadata("hitshield")) {
                                if (en.getMetadata("hitshield").get(0).asInt() != 0) {
                                    builder.append(SUtil.colorize(" &r&l" + en.getMetadata("hitshield").get(0).asInt() + " Hits"));
                                } else {
                                    builder.append(SUtil.colorize(" &a" + NumberSuffix.format(((LivingEntity) en).getHealth()) + "&7/&a" + NumberSuffix.format(((LivingEntity) en).getMaxHealth())));
                                }
                            } else {
                                builder.append(SUtil.colorize(" &a" + NumberSuffix.format(((LivingEntity) en).getHealth()) + "&7/&a" + NumberSuffix.format(((LivingEntity) en).getMaxHealth())));
                            }
                            as.setCustomName(builder.toString());
                            holoMap.put(en, as);
                            if (en.isDead()) {
                                as.remove();
                                if (as.getVehicle() != null) {
                                    if (as.getVehicle().isDead()) {
                                        as.getVehicle().remove();
                                    }
                                }
                                holoMap.remove(en);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                SBPlayer.updateStats();
            }
        }.runTaskTimer(SBX.getInstance(), 0L, 5L);

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {

                if (i == 2) {
                    SBPlayer.doRegenStats();
                    i = 0;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    SBPlayer sbPlayer = new SBPlayer(p);
                    sbPlayer.sendBarMessage(getStatMessage(sbPlayer));
                }
                i++;
            }
        }.runTaskTimerAsynchronously(this, 0L, 10L);
    }

    public static String getStatMessage(SBPlayer p) {
        DecimalFormat f = new DecimalFormat("#");
        String middlemsg = "";
        if (p.getMaxStat(SBPlayer.PlayerStat.DEFENSE) != 0) {
            middlemsg = "    &a" + f.format(p.getMaxStat(SBPlayer.PlayerStat.DEFENSE)) + "❈ Defense§b    ";
        }
        if (queuedBarMessages.containsKey(p.getUniqueId())) {
            String s = queuedBarMessages.get(p.getUniqueId()).keySet().stream().findFirst().orElse("");
            if (!s.equals("")) {
                middlemsg = s;
            }
        }
        return SUtil.colorize("&c" + f.format(p.getStat(SBPlayer.PlayerStat.HEALTH)) + "/" + f.format(p.getMaxStat(SBPlayer.PlayerStat.HEALTH)) + "❤ " + middlemsg + " &b" + f.format(p.getStat(SBPlayer.PlayerStat.INTELLIGENCE)) + "/" + f.format(p.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE)) + "✎ Mana");
    }

    public static String getStatMessage(SBPlayer p, ManaEvent e) {
        return SUtil.colorize("&c" + p.getStat(SBPlayer.PlayerStat.HEALTH) + "/" + p.getMaxStat(SBPlayer.PlayerStat.HEALTH) + " &b" + p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) + "/" + p.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE));
    }

    public void createDragonDataFile() {
        dragonDataFile = new File(getDataFolder(), "dragon_data.yml");
        if (!dragonDataFile.exists()) {
            dragonDataFile.getParentFile().mkdirs();
            saveResource("dragon_data.yml", false);
        }
        dragonData = new YamlConfiguration();
        try {
            dragonData.load(dragonDataFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getDragonData() {
        return this.dragonData;
    }

    public static MongoCoins getMongoStats() {
        return mongoStats;
    }

    public static SBX getInstance() {
        return instance;
    }

    public static WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    public static ArrayList<Material> getIllegalMaterials() {
        ArrayList<Material> blacklisted = new ArrayList<>();
        for (Material mat : Material.values()) {
            try {
                ItemBuilder.from(new ItemStack(mat, 1, (short) 0)).asGuiItem();
            } catch (Exception e) {
                blacklisted.add(mat);
            }
        }
        return blacklisted;
    }

    private void githubItems() {
        try {
            JSONObject hypixelJSON = SUtil.readJsonObjFromUrl("https://api.hypixel.net/resources/skyblock/items");
            if (!hypixelJSON.getBoolean("success")) return;
            JSONArray jsonItems = hypixelJSON.getJSONArray("items");
            int count = -1;
            for (Object doc : jsonItems) {
                JSONObject json = (JSONObject) doc;
                SBItemBuilder item = new SBItemBuilder();
                try {
                    item.name(json.has("name") ? json.getString("name") : "Null")
                            .rarity(Rarity.valueOf(json.has("tier") ? json.getString("tier") : Rarity.COMMON.name()))
                            .material(Material.valueOf(json.getString("material")))
                            .type(ItemType.typeFromString(json.has("category") ? json.getString("category") : null))
                            .id(json.getString("id"))
                            .stackable(!json.has("unstackable"));
                    if (json.has("stats")) {
                        JSONObject statsJSON = json.getJSONObject("stats");
                        statsJSON.toMap().forEach((key, value) -> {
                            item.stat(SBPlayer.PlayerStat.getStat(key), Double.parseDouble(String.valueOf(value)));
                        });
                    }
                    if (json.has("color")) {
                        item.color(json.getString("color"));
                    }
                    if(json.has("skin")) {
                        item.texture(json.getString("skin"));
                    }
                    if (json.has("description")) {
                        for (String line : StackUtils.stringToLore(json.getString("description"), 43, ChatColor.GRAY)) {
                            item.addDescriptionLine(HypixelColorCodes.translateHypixelColorCodes(line));
                        }
                    }
                    hypixelItemMap.put(json.getString("id"), item.build());
                } catch (JSONException | NullPointerException e) {
                    count++;
                    if(count >= 20) {
                        return;
                    }
                    e.printStackTrace();
                    System.out.println(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
