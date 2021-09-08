package net.atlas.SkyblockSandbox;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SBCompleter;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.command.commands.*;
import net.atlas.SkyblockSandbox.database.sql.BackpackData;
import net.atlas.SkyblockSandbox.database.sql.MySQL;
import net.atlas.SkyblockSandbox.database.sql.SQLBpCache;
import net.atlas.SkyblockSandbox.economy.Coins;
import net.atlas.SkyblockSandbox.entity.SkyblockEntity;
import net.atlas.SkyblockSandbox.event.customEvents.ManaEvent;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.files.DatabaseInformationFile;
import net.atlas.SkyblockSandbox.files.IslandInfoFile;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.item.ability.AbiltyListener;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.WitherImpact;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.listener.sbEvents.abilities.AbilityHandler;
import net.atlas.SkyblockSandbox.database.mongo.MongoCoins;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.util.NumberTruncation.NumberSuffix;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
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
    public static HashMap<UUID, SkillEXPGainEvent> prevSkillEvent = new HashMap<>();
    public static HashMap<UUID, SkillEXPGainEvent> cachedEvent = new HashMap<>();
    public static LinkedHashMap<UUID, LinkedHashMap<String, Long>> queuedBarMessages = new LinkedHashMap<>();
    public static HashMap<EntityArmorStand, Integer> cooldownMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Integer> counterMap = new HashMap<>();
    public static HashMap<EntityArmorStand, Vector> vMap = new HashMap<>();
    public static HashMap<EntityArmorStand,Integer> angleMap = new HashMap<>();
    public static BukkitTask prevRunnable = null;
    public static HashMap<UUID, String> prevBarMessage = new HashMap<>();
    public static Map<Player, Boolean> abilityUsed = new HashMap<>();
    public static HashMap<Player, Boolean> canfire = new HashMap<>();
    public static HashMap<Player, List<EntityArmorStand>> thrownAxes = new HashMap<>();

    private static SBX instance;
    SkyblockCommandFramework framework;
    private static MongoCoins mongoStats;
    public Coins coins;
    public MySQL sql;

    private File dragonDataFile;
    private FileConfiguration dragonData;

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
        instance = this;
        framework = new SkyblockCommandFramework(this);
        createDataFiles();
        mongoStats = new MongoCoins();
        mongoStats.connect();
        coins = new Coins();
        sql = new MySQL();
        SQLBpCache.init();

        Data.initialize();
        registerListeners();
        registerCommands();
        createIslandWorld();

        startOnlineRunnables();
        createDataFiles();


        //registerEntity("Enderman", 58, EntityZombie.class, NoTeleportEnderman.class);
        SkyblockEntity.registerEntities();
    }

    @Override
    public void onDisable() {
        BackpackData.save();
        getServer().getConsoleSender().sendMessage(SUtil.colorize("&cSkyblock Sandbox core was disabled. Please re-enable if you want the server to work."));
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
        Bukkit.getServer().getPluginManager().registerEvents(new AbiltyListener(new SoulCry()), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AbiltyListener(new HellShatter()), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AbiltyListener(new WitherImpact()), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LootListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AbilityHandler(),this);
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
                for (Player p : Bukkit.getOnlinePlayers()) {
                    SBPlayer sbPlayer = new SBPlayer(p);
                    sbPlayer.updateStats();
                }
            }
        }.runTaskTimer(SBX.getInstance(), 0L, 5L);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 0) {

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        SBPlayer sbPlayer = new SBPlayer(p);
                        if(i==2) {
                            sbPlayer.doRegenStats();
                            i=0;
                        }
                        sbPlayer.sendBarMessage(getStatMessage(sbPlayer));
                        i++;
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 10L);
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
}
