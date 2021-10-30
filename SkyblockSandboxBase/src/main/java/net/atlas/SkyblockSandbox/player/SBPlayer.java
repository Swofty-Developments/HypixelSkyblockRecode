package net.atlas.SkyblockSandbox.player;

import com.google.common.base.Strings;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import lombok.Setter;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionBidHandler;
import net.atlas.SkyblockSandbox.AuctionHouse.AuctionItemHandler;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
import net.atlas.SkyblockSandbox.event.customEvents.PlayerCustomDeathEvent;
import net.atlas.SkyblockSandbox.island.islands.FairySouls;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.SkyblockItem;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.playerIsland.Data;
import net.atlas.SkyblockSandbox.playerIsland.IslandId;
import net.atlas.SkyblockSandbox.playerIsland.PlayerIsland;
import net.atlas.SkyblockSandbox.playerIsland.PlayerIslandHandler;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.atlas.SkyblockSandbox.SBX.*;
import static net.atlas.SkyblockSandbox.listener.sbEvents.PlayerJoin.bonusStats;
import static net.atlas.SkyblockSandbox.listener.sbEvents.PlayerJoin.maxStats;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.HEALTH;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.INTELLIGENCE;
import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;
import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulSkullItem;


@Setter
public class SBPlayer extends PluginPlayer {

    static SBPlayer sbPlayer;
    public static HashMap<UUID, EntityDamageEvent.DamageCause> causes = new HashMap<>();

    public SBPlayer(Player player) {
        super(player);
        sbPlayer = this;
    }


    /*public void sendTitle(String s,String s1,ChatColor sColor,ChatColor s1Color) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\",color:" + sColor.name().toLowerCase() + "}");
        IChatBaseComponent subTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\",color:" + s1Color.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle title2 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) sbPlayer.getPlayer()).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) sbPlayer.getPlayer()).getHandle().playerConnection.sendPacket(title2);
        ((CraftPlayer) sbPlayer.getPlayer()).getHandle().playerConnection.sendPacket(length);
    }*/

    public double getStat(PlayerStat stat) {
        boolean flag = !bonusStats.containsKey(sbPlayer.getUniqueId());
        if (!flag && !bonusStats.get(sbPlayer.getUniqueId()).containsKey(stat)) {
            flag = true;
        }
        if(flag) {
            HashMap<PlayerStat, Double> temp = new HashMap<>(bonusStats.get(sbPlayer.getUniqueId()));
            temp.put(stat, 0D);
            bonusStats.put(sbPlayer.getUniqueId(), temp);
        }
        return bonusStats.get(sbPlayer.getUniqueId()).get(stat);
    }

    public double getMaxStat(PlayerStat stat) {
        boolean flag = !maxStats.containsKey(sbPlayer.getUniqueId());
        if (!flag && !maxStats.get(sbPlayer.getUniqueId()).containsKey(stat)) {
            flag = true;
        }
        if (flag) {
            HashMap<PlayerStat, Double> temp = new HashMap<>(maxStats.get(sbPlayer.getUniqueId()));
            temp.put(stat, 0D);
            maxStats.put(sbPlayer.getUniqueId(), temp);
        }
        return maxStats.get(sbPlayer.getUniqueId()).get(stat);
    }

    public void setStat(PlayerStat stat, double v) {
        if (getMaxStat(stat) < v) {
            HashMap<PlayerStat, Double> updatedStat = new HashMap<>(bonusStats.get(sbPlayer.getUniqueId()));
            updatedStat.put(stat, getMaxStat(stat));
            bonusStats.put(sbPlayer.getUniqueId(), updatedStat);
        } else {
            HashMap<PlayerStat, Double> updatedStat = new HashMap<>(bonusStats.get(sbPlayer.getUniqueId()));
            updatedStat.put(stat, v);
            bonusStats.put(sbPlayer.getUniqueId(), updatedStat);
        }

    }

    public void giveItem(SkyblockItem item) {
        if (hasSpace()) {
            sbPlayer.getInventory().addItem(item.item());
            sbPlayer.sendMessage(SUtil.colorize("&aSuccessfully gave &e" + item));
        }
    }

    public void giveItem(SBItemStack item) {
        if (hasSpace()) {
            sbPlayer.getInventory().addItem(item.asBukkitItem());
            sbPlayer.sendMessage(SUtil.colorize("&aSuccessfully gave &e" + item.getItemID()));
        }
    }

    public void setItemInHand(SkyblockItem i) {
        sbPlayer.getPlayer().setItemInHand(i.item());
    }

    public boolean hasSpace() {
        for (ItemStack i : sbPlayer.getInventory().getContents()) {
            if (i == null || i.getType().equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }

    public static void updateStats() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            SBPlayer pl = new SBPlayer(p);
            HashMap<PlayerStat, Double> map = NBTUtil.getAllStats(sbPlayer);

            if(pl.getStat(HEALTH) <= 0) {
              pl.setStat(HEALTH, pl.getMaxStat(HEALTH));
              Bukkit.getPluginManager().callEvent(new PlayerCustomDeathEvent(p, pl, pl.getCause()));
            }

            for (PlayerStat s : PlayerStat.values()) {
                double stat = map.get(s);
                if (pl.getStat(s) < pl.getMaxStat(s) && !s.isRegen()) {
                    pl.setStat(s, pl.getMaxStat(s));
                }
                pl.setMaxStat(s, stat);
                if (pl.getStat(s) > pl.getMaxStat(s)) {
                    if (s.isRegen()) {
                        pl.setStat(s, pl.getMaxStat(s));
                    }
                }
                if (isSoulCryActive.containsKey(sbPlayer.getUniqueId())) {
                    if (isSoulCryActive.get(sbPlayer.getUniqueId())) {
                        if (s.equals(PlayerStat.FEROCITY)) {
                            pl.setMaxStat(s, stat + 400);
                        }
                    }
                }


            }

            if (sbPlayer.getMaxStat(HEALTH) >= 100) {
                double newHealth = 0;
                double oldrng = (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
                if (oldrng == 0) {
                    newHealth = 0;
                } else {
                    if (sbPlayer.getMaxStat(HEALTH) <= 200) {
                        newHealth = (sbPlayer.getMaxStat(HEALTH)) / 5;
                    } else {
                        double newRng = (40 - 0);
                        newHealth = Math.floor(((sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);

                    }
                }
                pl.getPlayer().setMaxHealth(Math.floor(newHealth));
                oldrng = (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
                double newRng = 20;
                if (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) >= 100 && sbPlayer.getMaxStat(HEALTH) <= 200) {
                    newRng = (sbPlayer.getMaxStat(HEALTH) / 5 - 0);
                } else {
                    newRng = (40 - 0);
                }
                newHealth = Math.ceil(((sbPlayer.getStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);
                if (newHealth > sbPlayer.getMaxHealth()) {
                    newHealth = sbPlayer.getMaxHealth();
                }
                sbPlayer.setHealth(Math.ceil(newHealth));

            } else {
                pl.getPlayer().setMaxHealth(20);
                pl.getPlayer().setHealth(20);
            }

            //maxStats.put(sbPlayer.getUniqueId(),map);
        }
    }

    public static void doRegenStats() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            SBPlayer pl = new SBPlayer(p);
            if (pl.getStat(PlayerStat.INTELLIGENCE) < pl.getMaxStat(PlayerStat.INTELLIGENCE)) {
                double newintl = pl.getStat(PlayerStat.INTELLIGENCE) + (pl.getMaxStat(PlayerStat.INTELLIGENCE) * 0.1);
                if (newintl > pl.getMaxStat(PlayerStat.INTELLIGENCE)) {
                    newintl = pl.getMaxStat(PlayerStat.INTELLIGENCE);
                }
                pl.setStat(INTELLIGENCE, newintl);
            }
            if (pl.getStat(PlayerStat.HEALTH) < pl.getMaxStat(PlayerStat.HEALTH)) {
                double newhlth = pl.getStat(PlayerStat.HEALTH) + (pl.getMaxStat(PlayerStat.HEALTH) * 0.01 + 1.5);
                if (newhlth > pl.getMaxStat(PlayerStat.HEALTH)) {
                    newhlth = pl.getMaxStat(PlayerStat.HEALTH);
                }
                pl.setStat(HEALTH, newhlth);
            }
        }

    }

    public void setAllStats(HashMap<UUID, HashMap<PlayerStat, Double>> stats, PlayerStat... ignore) {
        for (PlayerStat s : stats.get(sbPlayer.getUniqueId()).keySet()) {
            for (PlayerStat b : ignore) {
                if (s == b) {
                    return;
                }
            }
            setCurrentAndMaxStat(s, stats.get(sbPlayer.getUniqueId()).get(s));
        }
    }

    public void queueMiddleActionText(SBPlayer p, String s, long timeInTicks) {
        s = SUtil.colorize(s);
        if (queuedBarMessages.containsKey(p.getUniqueId())) {
            LinkedHashMap<String, Long> temp = new LinkedHashMap<>(queuedBarMessages.get(p.getUniqueId()));
            temp.put(s, timeInTicks);
            queuedBarMessages.put(p.getUniqueId(), temp);
            String finalS = s;
            new BukkitRunnable() {
                @Override
                public void run() {
                    LinkedHashMap<String, Long> temp = new LinkedHashMap<>(queuedBarMessages.get(p.getUniqueId()));
                    temp.remove(finalS);
                    queuedBarMessages.put(p.getUniqueId(), temp);
                }
            }.runTaskLater(SBX.getInstance(), timeInTicks);
        } else {
            LinkedHashMap<String, Long> map = new LinkedHashMap<>();
            queuedBarMessages.put(p.getUniqueId(), map);
        }
    }

    public void setCurrentAndMaxStat(PlayerStat stat, double v) {
        setMaxStat(stat, v);
        setStat(stat, v);
    }

    public void setMaxStat(PlayerStat stat, double v) {
        HashMap<PlayerStat, Double> updatedStat = new HashMap<>(maxStats.get(sbPlayer.getUniqueId()));
        updatedStat.put(stat, v);
        maxStats.put(sbPlayer.getUniqueId(), updatedStat);
    }

    public void setOverflowStat(PlayerStat stat, double v) {
        HashMap<PlayerStat, Double> updatedStat = new HashMap<>(bonusStats.get(sbPlayer.getUniqueId()));
        updatedStat.put(stat, v);
        bonusStats.put(sbPlayer.getUniqueId(), updatedStat);
    }

    public void sendBarMessage(String message) {
        SUtil.sendActionText(sbPlayer, SUtil.colorize(message));
    }

    public void playJingle(Jingle j, boolean loop) {
        j.send(sbPlayer.getPlayer(), loop);
    }

    public void playJingle(String file, boolean loop) {
        Song song = NBSDecoder.parse(new File(SBX.getInstance().getDataFolder().getPath() + "/Jingles/" + file + ".nbs"));
        playSong(song, sbPlayer.getPlayer(), loop);
    }

    public double getCurrentSkillExp(SkillType type) {
        if (cachedSkills.containsKey(sbPlayer.getUniqueId())) {
            return cachedSkills.get(sbPlayer.getUniqueId()).get(type);
        } else {
            HashMap<SkillType,Double> map = new HashMap<>();
            map.put(type,0D);
            cachedSkills.put(sbPlayer.getUniqueId(),map);
        }
        return cachedSkills.get(sbPlayer.getUniqueId()).get(type);
    }

    public int getSkillLvl(SkillType type) {
        if (!cachedSkillLvls.containsKey(sbPlayer.getUniqueId())) {
            HashMap<SkillType,Integer> map = new HashMap<>();
            for(SkillType skill:SkillType.values()) {
                map.put(skill,0);
            }
            cachedSkillLvls.put(sbPlayer.getUniqueId(),map);
        }
        if (!cachedSkillLvls.get(sbPlayer.getUniqueId()).containsKey(type)) {
            HashMap<SkillType,Integer> map = new HashMap<>();
            map.put(type,0);
            cachedSkillLvls.put(sbPlayer.getUniqueId(),map);
        }
        return cachedSkillLvls.get(sbPlayer.getUniqueId()).get(type);
    }

    public void setSkillLvl(SkillType type, int lvl) {
        HashMap<SkillType, Integer> temp;
        if (cachedSkillLvls.containsKey(sbPlayer.getUniqueId())) {
            temp = new HashMap<>(cachedSkillLvls.get(sbPlayer.getUniqueId()));
        } else {
            temp = new HashMap<>();
        }
        temp.put(type, lvl);
        cachedSkillLvls.put(sbPlayer.getUniqueId(), temp);
        //SBX.getMongoStats().setData(sbPlayer.getUniqueId(), type.getName() + "_lvl", lvl);
    }

    public void levelSkill(SkillType type) {
        int curLvl = getSkillLvl(type);
        if (curLvl != type.getMaxLvl()) {
            setSkillLvl(type, curLvl + 1);
            playSound(sbPlayer.getLocation(), Sound.LEVEL_UP, 2, 1);
            for (String s : getLevelUpMessage(type, curLvl + 1)) {
                sendMessage(s);
            }
        }
        if(sbPlayer.getCurrentSkillExp(type)>=SkillType.getLvlXP(getSkillLvl(type))) {
            if(getSkillLvl(type)!=type.getMaxLvl()) {
                levelSkill(type);
            }
        }
    }

    public List<String> getLevelUpMessage(SkillType type, int newLvl) {
        ArrayList<String> strings = new ArrayList<>();
        String border = "&3&l" + Strings.repeat("-", 64);
        strings.add(SUtil.colorize("&3&l" + border));
        strings.add("&b&l SKILL LEVEL UP &r&3" + type.getName() + " &8" + (newLvl - 1) + "➜&3" + (newLvl));
        strings.add("");
        strings.add("&a&l REWARDS");
        strings.add("&e  Coming soon!");
        strings.add(border);
        return SUtil.colorize(strings);
    }

    public void playSong(Song song, Player p, boolean loop) {
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.addPlayer(p);
        if (loop) {
            rsp.setRepeatMode(RepeatMode.ALL);
        }
        rsp.setPlaying(true);
    }

    public void addSkillXP(SkillType type, double amt) {
        HashMap<SkillType, Double> map;
        if (cachedSkills.containsKey(sbPlayer.getUniqueId())) {
            map = new HashMap<>(cachedSkills.get(sbPlayer.getUniqueId()));
            //idfk what this is; Intellij thingy:
            //what it does:
            //if(map.get(type)==null) {
            //   map.put(type,amt);
            // }
            map.merge(type, amt, Double::sum);
        } else {
            map = new HashMap<>();
            map.put(type, amt);
        }

        cachedSkills.put(sbPlayer.getUniqueId(), map);
        sbPlayer.playSound(sbPlayer.getLocation(), Sound.ORB_PICKUP, 2, 2f);
        if (sbPlayer.getCurrentSkillExp(type) >= SkillType.getLvlXP(getSkillLvl(type))) {
            if(getSkillLvl(type)!=type.getMaxLvl()) {
                levelSkill(type);
            }
        }
    }

    public boolean hasAuctions() {
        for (Document doc : new MongoAH().getAllDocuments()) {
            if (doc.get("owner").equals(getUniqueId().toString()))
                return true;
        }

        return false;
    }

    public ArrayList<AuctionItemHandler> getItemsBided() {
        ArrayList<AuctionItemHandler> list =  new ArrayList<>();
        for (AuctionItemHandler item : AuctionItemHandler.ITEMS.values()) {
            AtomicBoolean con = new AtomicBoolean(true);
            if (AuctionBidHandler.bids.get(item.getAuctionID()) != null) {
                for (AuctionBidHandler bid : AuctionBidHandler.bids.get(item.getAuctionID())) {
                    if (bid.getUuid().toString().equals(getUniqueId().toString()) && con.get()) {
                        list.add(item);
                        con.set(false);
                    }
                }
            }
        }
        return list;
    }

    public enum PlayerStat {
        HEALTH("&a", "health", 100, true, makeColorfulItem(Material.GOLDEN_APPLE, "&cHealth", 1, 0, "")),
        DEFENSE("&a", "defense", 0, false, makeColorfulItem(Material.IRON_CHESTPLATE, "&aDefense", 1, 0, "")),
        STRENGTH("strength", 0, false, makeColorfulItem(Material.BLAZE_POWDER, "&cStrength", 1, 0, "")),
        SPEED("&a", "speed", 100, false, makeColorfulItem(Material.SUGAR, "&rSpeed", 1, 0, ""), new String[]{"Walk_Speed"}),
        CRITICAL_CHANCE("crit_chance", 30, false, makeColorfulSkullItem("&9Crit Chance", "http://textures.minecraft.net/texture/3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1, "")),
        CRITICAL_DAMAGE("crit_damage", 50, false, makeColorfulSkullItem("&9Crit Damage", "http://textures.minecraft.net/texture/ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1, "")),
        INTELLIGENCE("&a", "intelligence", 100, true, makeColorfulItem(Material.ENCHANTED_BOOK, "&bIntelligence", 1, 0, "")),
        MINING_SPEED("&a", "mining_speed", 0, false, makeColorfulItem(Material.DIAMOND_PICKAXE, "&6Mining Speed", 1, 0, "")),
        ATTACK_SPEED("attack_speed", 0, false, makeColorfulItem(Material.GOLD_AXE, "&eAttack Speed", 1, 0, "")),
        SEA_CREATURE_CHANCE("sea_creature_chance", 0, false, makeColorfulItem(Material.PRISMARINE_CRYSTALS, "&3Sea Creature Chance", 1, 0, "")),
        MAGIC_FIND("magic_find", 0, false, makeColorfulItem(Material.STICK, "&bMagic Find", 1, 0, "")),
        PET_LUCK("pet_luck", 0, false, makeColorfulSkullItem("&dPet Luck", "http://textures.minecraft.net/texture/93c8aa3fde295fa9f9c27f734bdbab11d33a2e43e855accd7465352377413b", 1, "")),
        TRUE_DEFENSE("true_defense", 0, false, makeColorfulItem(Material.INK_SACK, "&rTrue Defense", 1, 15, "")),
        FEROCITY("&a", "ferocity", 0, false, makeColorfulItem(Material.INK_SACK, "&cFerocity", 1, 1, "")),
        ABILITY_DAMAGE("ability_damage", 0, false, makeColorfulItem(Material.BEACON, "&cAbility Damage", 1, 0, ""), new String[]{"ABILITY_DAMAGE_PERCENT", "WEAPON_ABILITY_DAMAGE"}),
        MINING_FORTUNE("&a", "mining_fortune", 0, false, makeColorfulSkullItem("&6Mining Fortune", "http://textures.minecraft.net/texture/f07dff657d61f302c7d2e725265d17b64aa73642391964fb48fc15be950831d8", 1, "")),
        FARMING_FORTUNE("&a", "farming_fortune", 0, false, makeColorfulSkullItem("&6Farming Fortune", "http://textures.minecraft.net/texture/2ab879e1e590041146bc78c018af5877d39e5475eb7db368fcaf2acda373833d", 1, "")),
        FORAGING_FORTUNE("&a", "foraging_fortune", 0, false, makeColorfulSkullItem("&6Foraging Fortune", "http://textures.minecraft.net/texture/4f960c639d4004d1882575aeba69f456fb3c744077935714947e19c1306d2733", 1, "")),
        PRISTINE("pristine", 0, false, makeColorfulSkullItem("&5Pristine", "http://textures.minecraft.net/texture/db6975af70724d6a44fd5946e60b2717737dfdb545b4dab1893351a9c9dd183c", 1, "")),
        DAMAGE("damage", 0, false, makeColorfulItem(Material.IRON_SWORD, "&cDamage", 1, 0, "")),
        GEAR_SCORE("&d", "gear_score", 0, false, makeColorfulItem(Material.INK_SACK, "&dGear Score", 1, 0, "")),
        BREAKING_POWER("breaking_power", 0, false, makeColorfulItem(Material.IRON_PICKAXE, "&7Breaking Power", 1, 0, ""));


        private String name;
        private double base;
        private boolean isRegen;
        private ItemStack stack;
        private String color;
        private String[] alias;

        PlayerStat(String color, String name, double base, boolean isRegen, ItemStack stack) {
            this.name = name;
            this.base = base;
            this.isRegen = isRegen;
            this.stack = stack;
            this.color = color;
        }
        PlayerStat(String color, String name, double base, boolean isRegen, ItemStack stack, String[] alias) {
            this.name = name;
            this.base = base;
            this.isRegen = isRegen;
            this.stack = stack;
            this.color = color;
            this.alias = alias;
        }

        PlayerStat(String name, double base, boolean isRegen, ItemStack stack) {
            this.name = name;
            this.base = base;
            this.isRegen = isRegen;
            this.stack = stack;
            this.color = "&c";
        }

        PlayerStat(String name, double base, boolean isRegen, ItemStack stack, String[] alias) {
            this.name = name;
            this.base = base;
            this.isRegen = isRegen;
            this.stack = stack;
            this.color = "&c";
            this.alias = alias;
        }

        public double getBase() {
            return base;
        }

        public boolean isRegen() {
            return isRegen;
        }

        public ItemStack getStack() {
            return stack;
        }

        public String getColor() {
            return SUtil.colorize(color);
        }

        public static PlayerStat getStat(String stat) {
            for (PlayerStat value : PlayerStat.values()) {
                if(value.name().equals(stat)) {
                    return value;
                }
                if(value.alias != null) {
                    for (String key : value.alias) {
                        if(stat.equals(key)) {
                            return value;
                        }
                    }
                }
            }
            return null;
        }
    }

    public PlayerIsland getPlayerIsland() {
        for (FileConfiguration cfg : Data.getAllIslandFiles()) {
            if (cfg.getString("owner").equals(sbPlayer.getUniqueId().toString()) || cfg.getStringList("members").contains(sbPlayer.getUniqueId().toString()))
                return new PlayerIslandHandler(IslandId.fromString(cfg.getString("id")));
        }

        return null;
    }

    public boolean hasIsland() {
        return getPlayerIsland() != null;
    }

    public DamageCause getCause() {
        return causes.get(getUniqueId());
    }
    public void setCause(DamageCause cause) {
        causes.put(getUniqueId(), cause);
    }
}
