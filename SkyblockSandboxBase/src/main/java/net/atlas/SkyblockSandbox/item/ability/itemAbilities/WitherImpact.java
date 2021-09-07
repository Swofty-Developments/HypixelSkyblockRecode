package net.atlas.SkyblockSandbox.item.ability.itemAbilities;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.ManaEvent;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.LootListener;
import net.atlas.SkyblockSandbox.island.islands.end.dragFight.StartFight;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.item.ability.Ability;
import net.atlas.SkyblockSandbox.item.ability.AbilityType;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.scoreboard.DragonScoreboard;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.TeleportAbility;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

public class WitherImpact extends Ability {
    @Override
    public String getAbilityName() {
        return "Wither Impact";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public double getManaCost() {
        return 250;
    }

    @Override
    public List<String> getAbilDescription() {
        return new ArrayList<>(Arrays.asList("§7Teleport §a10 blocks §7ahead of", "§7you. Then implode dealing", "§c10,000 §7damage to nearby", "§7enemies. Also applies the wither", "§7shield scroll ability reducing", "§7damage taken and granting an absorption shield for §e5", "§7seconds."));
    }

    @Override
    public void leftClickAirAction(Player p, ItemStack item) {

    }

    @Override
    public void leftClickBlockAction(Player p, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }


    @Override
    public void rightClickAirAction(Player player, PlayerInteractEvent event, ItemStack item) {
        SBItemStack sbitem = new SBItemStack(item);
        SBPlayer p = new SBPlayer(player);

        //if(sbitem.getAbilData(EnumAbilityData.NAME,j).equals(getAbilityName())) {
        if (p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) <= getManaCost()) {
            player.sendMessage("§cYou do not have enough mana to do that.");
            return;
        }
        p.setStat(SBPlayer.PlayerStat.INTELLIGENCE, p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) - getManaCost());

        double d = 10000 * (1 + (p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) / 100) * 0.3);

        Location l = player.getLocation().clone();
        Set<Material> TRANSPARENT = new HashSet<Material>();
        TRANSPARENT.add(Material.AIR);
        TRANSPARENT.add(Material.STATIONARY_WATER);
        TRANSPARENT.add(Material.VINE);
        TeleportAbility teleportability = new TeleportAbility();
        Block targetBlock = teleportability.getTeleportBlock(player, TRANSPARENT, 10);
        if (targetBlock != null) {
            if (targetBlock.getLocation().distance(player.getLocation()) < 11) {
                player.sendMessage(ChatColor.RED + "There are blocks in the way!");
            }
            Location targetLocation = targetBlock.getLocation();
            targetLocation.setPitch(player.getLocation().getPitch());
            targetLocation.setYaw(player.getLocation().getYaw());
            player.teleport(targetLocation.add(0.5D, 0.0D, 0.5D));
            player.playSound(player.getLocation(), Sound.EXPLODE, 5, 1);
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            ManaEvent manaEvent = new ManaEvent(p, ManaEvent.ManaCause.ABILITY);
            SBX.abilityUsed.put(player, true);
            p.queueMiddleActionText(p, "§b    §b-250 Mana (§6Wither Impact§b)    ", 20L);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float) p.getLocation().getX(),
                    (float) p.getLocation().getY(), (float) p.getLocation().getZ(),
                    0, 0, 0, 7, 6);
            ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
        int i = 0;
        double totalDmg = 0;
        for (Entity en : player.getNearbyEntities(6, 6, 6)) {
            if (en.getType().equals(EntityType.PLAYER) || en.getType().equals(EntityType.ARMOR_STAND) || en.hasMetadata("entity-tag")) {
            } else {
                if (!en.getType().equals(EntityType.PLAYER)) {
                    if (en instanceof LivingEntity) {
                        i++;
                        totalDmg += d;
                        if (en.isDead()) {
                            ((LivingEntity) en).damage(0);
                        } else {
                            if (en instanceof EnderDragon) {
                                if (StartFight.fightActive) {
                                    DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                                    dragonScoreboard.updateDragonDMG(player, d);
                                    LootListener.damage.put(player, StartFight.playerDMG.get(player));
                                    ((LivingEntity) en).damage(d);
                                }
                            } else {
                                ((LivingEntity) en).damage(d);
                            }
                        }
                    }
                }

            }
        }
        if (i >= 1) {
            DecimalFormat format = new DecimalFormat("#,###");
            player.sendMessage(SUtil.colorize("&7Your Implosion hit &c" + i + "&7 enemies dealing &c" + format.format(totalDmg) + " damage&7."));
        }

        //}

    }

    @Override
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(player, event, item);
    }

    @Override
    public void shiftLeftClickAirAction(Player player, ItemStack item) {

    }

    @Override
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftRightClickAirAction(Player player, PlayerInteractEvent event, ItemStack item) {

    }

    @Override
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void hitEntityAction(Player p, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item) {

    }

    @Override
    public void breakBlockAction(Player p, BlockBreakEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void clickedInInventoryAction(Player p, InventoryClickEvent event) {

    }

    @Override
    public void shiftEvent(Player p, PlayerToggleSneakEvent e) {

    }

    @Override
    public void rightClickEntityEvent(Player p, PlayerInteractAtEntityEvent e) {

    }

    @Override
    public void playerFishAction(Player p, PlayerFishEvent event, ItemStack item) {

    }

    @Override
    public void playerShootAction(Player p, EntityShootBowEvent event, ItemStack item) {

    }
}
