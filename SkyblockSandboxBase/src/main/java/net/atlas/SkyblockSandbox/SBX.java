package net.atlas.SkyblockSandbox;

import com.google.common.base.Enums;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SBCompleter;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.command.commands.*;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.database.sql.MySQL;
import net.atlas.SkyblockSandbox.database.sql.SQLBpCache;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.entity.SkyblockEntity;
import net.atlas.SkyblockSandbox.event.customEvents.ManaEvent;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import net.atlas.SkyblockSandbox.files.IslandInfoFile;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.AbiltyListener;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.ShortBowTerm;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.WitherImpact;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.listener.sbEvents.abilities.AbilityHandler;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.storage.MongoStorage;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import net.atlas.SkyblockSandbox.util.signGUI.SignManager;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap2;

public class SBX extends JavaPlugin {
    List<String> flags = new ArrayList<>(Arrays.asList("--noteleport", "--noai"));
    public static HashMap<UUID, Boolean> isSoulCryActive = new HashMap<>();
    public static HashMap<String, ItemStack> storedItem = new HashMap<>();
    public static HashMap<UUID, HashMap<Slayers, HashMap<SlayerTier, Double>>> activeSlayers = new HashMap<>();
    public static HashMap<UUID, HashMap<SkillType, Double>> cachedSkills = new HashMap<>();
    public static HashMap<UUID, HashMap<SkillType, Integer>> cachedSkillLvls = new HashMap<>();
    public static HashMap<UUID, SkillEXPGainEvent> prevSkillEvent = new HashMap<>();
    public static HashMap<UUID, SkillEXPGainEvent> cachedEvent = new HashMap<>();
    public static LinkedHashMap<UUID, LinkedHashMap<String, Long>> queuedBarMessages = new LinkedHashMap<>();
    public static HashMap<EntityArmorStand, Integer> cooldownMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Integer> counterMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Vector> vMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Integer> angleMap = new HashMap<>();
    public static BukkitTask prevRunnable = null;
    public static HashMap<UUID, String> prevBarMessage = new HashMap<>();
    public static Map<Player, Boolean> abilityUsed = new HashMap<>();
    public static HashMap<Player, Boolean> canfire = new HashMap<>();
    public static HashMap<Player, List<EntityArmorStand>> thrownAxes = new HashMap<>();
    public static final TreeMap<String, ItemStack> hypixelItemMap = new TreeMap<>();
    public MySQL sql;
    public SignManager signManager;

    private static SBX instance;
    SkyblockCommandFramework framework;
    private static MongoCoins mongoStats;
    public MongoStorage mongoStorage;
    public static MongoStorage storage = new MongoStorage();
    public Coins coins;

    private File dragonDataFile;
    private FileConfiguration dragonData;

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        instance = this;
        framework = new SkyblockCommandFramework(this);
        createDataFiles();
        mongoStats = new MongoCoins();
        mongoStats.connect();
        new MongoAH().connect();
        coins = new Coins();
        storage.connect();

        Data.initialize();
        sql = new MySQL();
        SQLBpCache.init();
        signManager = new SignManager(this);
        signManager.init();
        registerListeners();
        registerCommands();
        createIslandWorld();
        githubItems();

        startOnlineRunnables();
        createDataFiles();


        //registerEntity("Enderman", 58, EntityZombie.class, NoTeleportEnderman.class);
        SkyblockEntity.registerEntities();
    }

    @Override
    public void onDisable() {
        cacheSkills();
    }

    public static void cacheSkills() {
        for (UUID uid : cachedSkills.keySet()) {
            for (SkillType type : cachedSkills.get(uid).keySet()) {
                double amt = cachedSkills.get(uid).get(type);
                mongoStats.setData(uid, type.getName() + "_xp", amt);
            }
            for (SkillType type : cachedSkillLvls.get(uid).keySet()) {
                int amt = cachedSkillLvls.get(uid).get(type);
                mongoStats.setData(uid, type.getName() + "_lvl", amt);
            }
        }
    }

    @SBCompleter(name = "spawnmob", aliases = {"spawncustommob"})
    public List<String> spawnMobComplete(SBCommandArgs args) {
        List<String> list = new ArrayList<String>();
        for (EntityType e : EntityType.values()) {
            list.add(e.name());
        }
        return list;
    }

    void registerListeners() {
        SkyblockListener.registerListeners();
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new AbiltyListener(new SoulCry()), this);
        pm.registerEvents(new AbiltyListener(new HellShatter()), this);
        pm.registerEvents(new AbiltyListener(new WitherImpact()), this);
        pm.registerEvents(new AbiltyListener(new ShortBowTerm()), this);
        pm.registerEvents(new LootListener(), this);
        pm.registerEvents(new AbilityHandler(), this);
    }

    void registerCommands() {
        framework.registerCommands(new Command_spawnmob(this));
        framework.registerCommands(new Command_giveItem(this));
        framework.registerCommands(new Command_createitem(this));
        framework.registerCommands(new Command_island(this));
        framework.registerCommands(new Command_jingle(this));
        framework.registerCommands(new Command_warp(this));
        framework.registerCommands(new Command_importhead(this));
        framework.registerCommands(new Command_sbmenu(this));
        framework.registerCommands(new Command_storage(this));
        framework.registerCommands(new Command_dev(this));
        framework.registerCommands(new Command_items(this));
        framework.registerCommands(new Command_storage(this));
        framework.registerCommands(new Command_debugtest(this));
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
    }

    void startOnlineRunnables() {
        File file = new File("plugins/SkyblockSandboxBase-0.1.jar");
        long lastMod = file.lastModified();
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
            int ii = 0;

            @Override
            public void run() {

                if (i == 2) {
                    SBPlayer.doRegenStats();
                    i = 0;
                }
                if(ii==30) {
                    File file2 = new File("plugins/SkyblockSandboxBase-0.1.jar");
                    System.out.println(file.lastModified());
                    System.out.println(file2.lastModified());
                    if(lastMod!=file2.lastModified()) {
                        updateServer();
                    } else {
                        ii = 0;
                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    SBPlayer sbPlayer = new SBPlayer(p);
                    sbPlayer.sendBarMessage(getStatMessage(sbPlayer));
                }
                i++;
                ii++;
            }
        }.runTaskTimerAsynchronously(this, 0L, 10L);
    }

    public void updateServer() {
        for(Player pl:Bukkit.getOnlinePlayers()) {
            SBPlayer p = new SBPlayer(pl);
            p.sendTitle(SUtil.colorize("&cGame Update"), SUtil.colorize("&eServer Restarting in: &c30"));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, "stop");
            }
        }.runTaskLater(SBX.getInstance(),20*30L);
    }

    public static String getStatMessage(SBPlayer p) {
        DecimalFormat f = new DecimalFormat("#");
        String middlemsg = "";
        if (queuedBarMessages.containsKey(p.getUniqueId())) {
            String s = queuedBarMessages.get(p.getUniqueId()).keySet().stream().findFirst().orElse("");
            if (!s.equals("")) {
                middlemsg = s;
            }
        }
        return SUtil.colorize("&c" + f.format(p.getStat(SBPlayer.PlayerStat.HEALTH)) + "/" + f.format(p.getMaxStat(SBPlayer.PlayerStat.HEALTH)) + "❤ Health " + middlemsg + " &b" + f.format(p.getStat(SBPlayer.PlayerStat.INTELLIGENCE)) + "/" + f.format(p.getMaxStat(SBPlayer.PlayerStat.INTELLIGENCE)) + "✎ Mana");
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
            System.err.println("Starting to download neu-repo");
            File repoLocation = new File(getDataFolder(), File.separator + "github");
            repoLocation.mkdirs();
            File itemsZip = new File(repoLocation, "neu-items-master.zip");
            try {
                itemsZip.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }


            URL url = new URL("https://github.com/Moulberry/NotEnoughUpdates-REPO/archive/refs/heads/master.zip");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(30000);

            try (InputStream is = urlConnection.getInputStream()) {
                FileUtils.copyInputStreamToFile(is, itemsZip);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to download NEU Repo! Please report this issue to the mod creator");
                return;
            }
            System.out.println("Successfully downloaded NEU Repo!");
            System.out.println("Starting to unzip NEU Repo!");

            SUtil.unzipIgnoreFirstFolder(itemsZip.getAbsolutePath(), repoLocation.getAbsolutePath());
            File items = new File(repoLocation, "items");
            if (items.exists()) {
                File[] itemFiles = new File(repoLocation, "items").listFiles();
                if (itemFiles != null) {
                    for (File f : itemFiles) {
                        JSONParser parser = new JSONParser();
                        JSONObject json;
                        try {
                            try (Reader reader = Files.newBufferedReader(f.toPath().toAbsolutePath(), StandardCharsets.UTF_8)) {
                                json = (JSONObject) parser.parse(reader);
                            }
                            try {
                                MinecraftKey mk = new MinecraftKey(json.get("itemid").toString());
                                ItemStack item = CraftItemStack.asNewCraftStack(net.minecraft.server.v1_8_R3.Item.REGISTRY.get(mk));
                                Material mat = item.getType();
                                switch (mat) {
                                    case BARRIER:
                                    case COMMAND:
                                    case COMMAND_MINECART:
                                    case BURNING_FURNACE:
                                    case SOIL:
                                        continue;
                                }
                                String displayname = String.valueOf(json.get("displayname"));
                                ArrayList<String> lore = new ArrayList<>();
                                for (Object list : (JSONArray) json.get("lore")) {
                                    lore.add(list.toString());
                                }
                                String parsedRarity = ChatColor.stripColor(lore.get(lore.size() - 1)).split(" ")[0];
                                Rarity r = Enums.getIfPresent(Rarity.class, parsedRarity).orNull();

                                NBTTagCompound nbt = MojangsonParser.parse(json.get("nbttag").toString());
                                net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(StackUtils.makeColorfulItem(mat, displayname, 1, Integer.parseInt(json.get("damage").toString()), lore));
                                itemStack.setTag(nbt);
                                ItemStack bukkitStack = CraftItemStack.asBukkitCopy(itemStack);
                                for (String s : lore) {
                                    String parsedStat = ChatColor.stripColor(s).replace(' ', '_').toUpperCase().split(":")[0];
                                    List<String> statsFormat = new ArrayList<>(Arrays.asList(SBItemStack.statsformat));
                                    if (statsFormat.stream().anyMatch(parsedStat::equalsIgnoreCase)) {
                                        SBPlayer.PlayerStat stat = Enums.getIfPresent(SBPlayer.PlayerStat.class, parsedStat).orNull();
                                        if (stat != null) {
                                            try {
                                                String split1 = ChatColor.stripColor(s).replace(' ', '_').toUpperCase();
                                                if (split1.contains("{")) {
                                                    split1 = "0";
                                                } else {
                                                    if (split1.contains(":")) {
                                                        split1 = split1.split(":")[1];
                                                    }
                                                    if (split1.contains("_HP")) {
                                                        split1 = split1.replace("_HP", "");
                                                    }
                                                    if (split1.contains("%")) {
                                                        split1 = split1.replace("%", "");
                                                    }
                                                    if (split1.contains("+")) {
                                                        split1 = split1.replace("+", "");
                                                    }
                                                    if (split1.contains(".")) {
                                                        split1 = split1.split("\\.")[0];
                                                    }
                                                    if (split1.contains("-")) {
                                                        split1 = "0";
                                                    }
                                                    if (split1.contains(",")) {
                                                        split1 = split1.replace(",", "");
                                                    }
                                                    if (split1.contains("_")) {
                                                        split1 = split1.replace("_", "");
                                                    }
                                                    if (split1.contains("?")) {
                                                        split1 = split1.replace("?", "");
                                                    }
                                                    if (split1.contains("HEALTH") || split1.contains("PERSECOND") || split1.contains("SPEED") || split1.contains("INTELLIGENCE") || split1.contains("DAMAGE") || split1.contains("STRENGTH")) {
                                                        split1 = "0";
                                                    }
                                                    if (split1.contains("?")) {
                                                        split1 = split1.replace("?", "");
                                                    }
                                                    int amt = Integer.parseInt(split1);
                                                    if (amt != 0) {
                                                        bukkitStack = NBTUtil.setInteger(bukkitStack, amt, stat.name());
                                                    }
                                                }
                                            } catch (NumberFormatException ignored) {
                                                ignored.printStackTrace();
                                            }
                                        }
                                    } else {
                                        SBItemStack stack = new SBItemStack(bukkitStack);
                                        bukkitStack = stack.addDescriptionLine(s, lore.indexOf(s));
                                    }

                                }
                                if (r != null) {
                                    bukkitStack = NBTUtil.setString(bukkitStack, r.toString(), "RARITY");
                                }
                                bukkitStack = NBTUtil.setString(bukkitStack, "true", "is-hypixel");

                                hypixelItemMap.put(String.valueOf(json.get("internalname")), bukkitStack);
                            } catch (MojangsonParseException e) {
                                e.printStackTrace();
                            }
                        } catch (NullPointerException ignored) {
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
