package net.atlas.SkyblockSandbox.player;

import lombok.Setter;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.database.mongo.MongoAH;
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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

import static net.atlas.SkyblockSandbox.SBX.*;
import static net.atlas.SkyblockSandbox.listener.sbEvents.PlayerJoin.maxStats;
import static net.atlas.SkyblockSandbox.listener.sbEvents.PlayerJoin.bonusStats;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.HEALTH;
import static net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat.INTELLIGENCE;
import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;
import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulSkullItem;


@Setter
public class SBPlayer extends PluginPlayer {

    static SBPlayer sbPlayer;

    public SBPlayer(Player player) {
        super(player);
        sbPlayer = this;
    }

    public double getStat(PlayerStat stat) {
        if(!bonusStats.containsKey(sbPlayer.getUniqueId())) {
            if(!bonusStats.get(sbPlayer.getUniqueId()).containsKey(stat)) {
                HashMap<PlayerStat,Double> temp = new HashMap<>(bonusStats.get(sbPlayer.getUniqueId()));
                temp.put(stat,0D);
                bonusStats.put(sbPlayer.getUniqueId(),temp);
            }
        }
        return bonusStats.get(sbPlayer.getUniqueId()).get(stat);
    }

    public double getMaxStat(PlayerStat stat) {
        if (!maxStats.containsKey(sbPlayer.getUniqueId())) {
            if(!maxStats.get(sbPlayer.getUniqueId()).containsKey(stat)) {
                HashMap<PlayerStat, Double> temp = new HashMap<>(maxStats.get(sbPlayer.getUniqueId()));
                temp.put(stat, 0D);
                maxStats.put(sbPlayer.getUniqueId(), temp);
            }
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
            sbPlayer.getInventory().addItem(item.item().asBukkitItem());
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
        sbPlayer.getPlayer().setItemInHand(i.item().asBukkitItem());
    }

    public boolean hasSpace() {
        for (ItemStack i : sbPlayer.getInventory().getContents()) {
            if (i == null || i.getType().equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }

    public void updateStats() {
        HashMap<PlayerStat, Double> map = NBTUtil.getAllStats(sbPlayer);

        for (PlayerStat s : PlayerStat.values()) {
            double stat = map.get(s);
            if (getStat(s) < getMaxStat(s) && !s.isRegen()) {
                setStat(s, getMaxStat(s));
            }
            setMaxStat(s, stat);
            if (getStat(s) > getMaxStat(s)) {
                if (s.isRegen()) {
                    setStat(s, getMaxStat(s));
                }
            }
            if (isSoulCryActive.containsKey(sbPlayer.getUniqueId())) {
                if (isSoulCryActive.get(sbPlayer.getUniqueId())) {
                    if (s.equals(PlayerStat.FEROCITY)) {
                        setMaxStat(s, stat + 400);
                    }
                }
            }


        }

        if(sbPlayer.getMaxStat(HEALTH)>100) {
            double newHealth = 0;
            double oldrng = (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
            if (oldrng == 0) {
                newHealth = 0;
            } else {
                if(sbPlayer.getMaxStat(HEALTH)<=200) {
                    newHealth = (sbPlayer.getMaxStat(HEALTH))/5;
                } else {
                    double newRng = (40 - 0);
                    newHealth = Math.floor(((sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);

                }
            }
            getPlayer().setMaxHealth(Math.floor(newHealth));
        } else {
            getPlayer().setMaxHealth(20);
        }

        double newHealth;
        double oldrng = (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) - 0);
        if (oldrng == 0)
            newHealth = 0;
        else {
            double newRng = 20;
            if (sbPlayer.getMaxStat(SBPlayer.PlayerStat.HEALTH) >= 100 && sbPlayer.getMaxStat(HEALTH)<=200) {
                newRng = (sbPlayer.getMaxStat(HEALTH)/5 - 0);
            } else {
                newRng = (40-0);
            }
            newHealth = Math.ceil(((sbPlayer.getStat(SBPlayer.PlayerStat.HEALTH) - 0) * newRng) / oldrng);
            if(newHealth> sbPlayer.getMaxHealth()) {
                newHealth = sbPlayer.getMaxHealth();
            }
            sbPlayer.setHealth(Math.ceil(newHealth));
        }
        //maxStats.put(sbPlayer.getUniqueId(),map);
    }

    public void doRegenStats() {
        HashMap<PlayerStat, Double> map = NBTUtil.getAllStats(sbPlayer);
        if (getStat(PlayerStat.INTELLIGENCE) < getMaxStat(PlayerStat.INTELLIGENCE)) {
            double newintl = getStat(PlayerStat.INTELLIGENCE) + (getMaxStat(PlayerStat.INTELLIGENCE) * 0.1);
            if (newintl > getMaxStat(PlayerStat.INTELLIGENCE)) {
                newintl = getMaxStat(PlayerStat.INTELLIGENCE);
            }
            setStat(INTELLIGENCE,newintl);
        }
        if (getStat(PlayerStat.HEALTH) < getMaxStat(PlayerStat.HEALTH)) {
            double newhlth = getStat(PlayerStat.HEALTH) + (getMaxStat(PlayerStat.HEALTH) * 0.01 + 1.5);
            if (newhlth > getMaxStat(PlayerStat.HEALTH)) {
                newhlth = getMaxStat(PlayerStat.HEALTH);
            }
            setStat(HEALTH,newhlth);
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

    public void queueMiddleActionText(SBPlayer p,String s,long timeInTicks) {
        s = SUtil.colorize(s);
        if(queuedBarMessages.containsKey(p.getUniqueId())) {
            LinkedHashMap<String,Long> temp = new LinkedHashMap<>(queuedBarMessages.get(p.getUniqueId()));
            temp.put(s,timeInTicks);
            queuedBarMessages.put(p.getUniqueId(),temp);
            String finalS = s;
            new BukkitRunnable() {
                @Override
                public void run() {
                    LinkedHashMap<String,Long> temp = new LinkedHashMap<>(queuedBarMessages.get(p.getUniqueId()));
                    temp.remove(finalS);
                    queuedBarMessages.put(p.getUniqueId(),temp);
                }
            }.runTaskLater(SBX.getInstance(),timeInTicks);
        } else {
            LinkedHashMap<String,Long> map = new LinkedHashMap<>();
            queuedBarMessages.put(p.getUniqueId(),map);
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

    public void playJingle(Jingle j,boolean loop) {
        j.send(sbPlayer.getPlayer(),loop);
    }

    public void playJingle(String file,boolean loop) {
        Song song = NBSDecoder.parse(new File(SBX.getInstance().getDataFolder().getPath() + "/Jingles/" + file + ".nbs"));
        playSong(song,sbPlayer.getPlayer(),loop);
    }

    public double getCurrentSkillExp(SkillType type) {
        return cachedSkills.get(sbPlayer.getUniqueId()).get(type);
    }

    public void playSong(Song song,Player p,boolean loop) {
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.addPlayer(p);
        if(loop) {
            rsp.setRepeatMode(RepeatMode.ALL);
        }
        rsp.setPlaying(true);
    }

    public void addSkillXP(SkillType type, double amt) {
        HashMap<SkillType, Double> map;
        if(cachedSkills.containsKey(sbPlayer.getUniqueId())) {
            map = new HashMap<>(cachedSkills.get(sbPlayer.getUniqueId()));
        } else {
            map = new HashMap<>();
        }
        if(cachedSkills.containsKey(sbPlayer.getUniqueId())) {
            map.put(type,map.get(type)+amt);
        } else {
            map.put(type,amt);
        }
        cachedSkills.put(sbPlayer.getUniqueId(),map);


    }
    public boolean hasAuctions() {
        for (Document doc : new MongoAH().getAllDocuments()) {
            if (doc.get("owner").equals(getUniqueId().toString()))
                return true;
        }

        return false;
    }

    public enum PlayerStat {
        HEALTH("health", 100, true,makeColorfulItem(Material.GOLDEN_APPLE,"&cHealth",1,0,"")),
        DEFENSE("defense", 0, false,makeColorfulItem(Material.IRON_CHESTPLATE,"&aDefense",1,0,"")),
        STRENGTH("strength", 0, false,makeColorfulItem(Material.BLAZE_POWDER,"&cStrength",1,0,"")),
        SPEED("speed", 100, false,makeColorfulItem(Material.SUGAR,"&rSpeed",1,0,"")),
        CRIT_CHANCE("crit_chance", 30, false,makeColorfulSkullItem("&9Crit Chance","http://textures.minecraft.net/texture/3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b",1,"")),
        CRIT_DAMAGE("crit_damage", 50, false,makeColorfulSkullItem("&9Crit Damage","http://textures.minecraft.net/texture/ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c",1,"")),
        INTELLIGENCE("intelligence", 100, true,makeColorfulItem(Material.ENCHANTED_BOOK,"&bIntelligence",1,0,"")),
        MINING_SPEED("mining_speed",0,false,makeColorfulItem(Material.DIAMOND_PICKAXE,"&6Mining Speed",1,0,"")),
        ATTACK_SPEED("attack_speed", 0, false,makeColorfulItem(Material.GOLD_AXE,"&eAttack Speed",1,0,"")),
        SEA_CREATURE_CHANCE("sea_creature_chance",0,false,makeColorfulItem(Material.PRISMARINE_CRYSTALS,"&3Sea Creature Chance",1,0,"")),
        MAGIC_FIND("magic_find",0,false,makeColorfulItem(Material.STICK,"&bMagic Find",1,0,"")),
        PET_LUCK("pet_luck",0,false,makeColorfulSkullItem("&dPet Luck","http://textures.minecraft.net/texture/93c8aa3fde295fa9f9c27f734bdbab11d33a2e43e855accd7465352377413b",1,"")),
        TRUE_DEFENSE("true_defense",0,false,makeColorfulItem(Material.INK_SACK,"&rTrue Defense",1, 15,"")),
        FEROCITY("ferocity", 0, false,makeColorfulItem(Material.INK_SACK,"&cFerocity",1,1,"")),
        ABILITY_DAMAGE("ability_damage",0,false,makeColorfulItem(Material.BEACON,"&cAbility Damage",1,0,"")),
        MINING_FORTUNE("mining_fortune",0,false,makeColorfulSkullItem("&6Mining Fortune","http://textures.minecraft.net/texture/f07dff657d61f302c7d2e725265d17b64aa73642391964fb48fc15be950831d8",1,"")),
        FARMING_FORTUNE("farming_fortune",0,false,makeColorfulSkullItem("&6Farming Fortune","http://textures.minecraft.net/texture/2ab879e1e590041146bc78c018af5877d39e5475eb7db368fcaf2acda373833d",1,"")),
        FORAGING_FORTUNE("foraging_fortune",0,false,makeColorfulSkullItem("&6Foraging Fortune","http://textures.minecraft.net/texture/4f960c639d4004d1882575aeba69f456fb3c744077935714947e19c1306d2733", 1,"")),
        PRISTINE("pristine",0,false,makeColorfulSkullItem("&5Pristine","http://textures.minecraft.net/texture/db6975af70724d6a44fd5946e60b2717737dfdb545b4dab1893351a9c9dd183c",1, "")),
        DAMAGE("damage", 0, false,makeColorfulItem(Material.IRON_SWORD,"&cDamage",1,0,""));


        private String name;
        private double base;
        private boolean isRegen;
        private ItemStack stack;

        PlayerStat(String name, double base, boolean isRegen,ItemStack stack) {
            this.name = name;
            this.base = base;
            this.isRegen = isRegen;
            this.stack = stack;
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
    }

    public PlayerIsland getPlayerIsland() {
        for (FileConfiguration cfg : Data.getAllIslandFiles()) {
            if (cfg.getString("owner").equals(sbPlayer.getUniqueId().toString()))
                return new PlayerIslandHandler(IslandId.fromString(cfg.getString("id")));
        }

        return null;
    }

    public boolean hasIsland() {
        return getPlayerIsland() != null;
    }

}
