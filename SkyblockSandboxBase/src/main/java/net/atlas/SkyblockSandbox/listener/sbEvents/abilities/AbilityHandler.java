package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.WitherImpact;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        try {
            if (event.getPlayer().getItemInHand() == null) return;
            if (!event.getItem().hasItemMeta()) return;
            if (!AbilityData.hasAbility(event.getItem())) return;
            ItemStack item = event.getItem();
            Player player = event.getPlayer();

            if (player.getItemInHand().getType().equals(Material.AIR)) return;
            if (!player.getItemInHand().hasItemMeta()) return;
            if (!AbilityData.hasAbility(player.getItemInHand())) return;

            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (!player.isSneaking()) {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("LEFT_CLICK")) {
                            Function(item, i, player, event);
                        }
                    }
                } else {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("SHIFT_LEFT_CLICK")) {
                            Function(item, i, player, event);
                        }
                    }
                }
            }
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (!player.isSneaking()) {
                    for (int i = 0; i < 6; ++i) {
                        if (AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i).equals("RIGHT_CLICK")) {
                            Function(item, i, player, event);
                        }
                    }
                    return;
                } else {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("SHIFT_RIGHT_CLICK")) {
                            Function(item, i, player, event);
                        }
                    }
                }
            }
        }catch(NullPointerException e){

        }
    }
    public void Function(ItemStack item, int i, Player player, PlayerInteractEvent event) {
        SBPlayer p = new SBPlayer(player);
        event.setCancelled(true);
        int cooldown = (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) : 0);
        int manaCost = (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) : 0);
        int damage = (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) : 0);
        String name = "" + AbilityData.retrieveData(EnumAbilityData.NAME, item, i);
        if (p.getStat(PlayerStat.INTELLIGENCE) < manaCost) {
            player.sendMessage("§cYou do not have enough mana to do that.");
            return;
        }
        if (CooldownManager.abilityCooldown("" + AbilityData.retrieveData(EnumAbilityData.ID, player.getItemInHand(), i), player, cooldown)) {
            player.sendMessage("§cYou are on cooldown!");
            return;
        }
        if (name.equals("")) {
            SBX.abilityUsed.put(player, true);

            p.queueMiddleActionText(p, SUtil.colorize("§b    §b-" + manaCost + " Mana (§6NONE§b)    "),20L);
            //p.sendBarMessage(player, "§c" + Math.round(user.getHealth()) + "/" + Math.round(user.getTotalHealth()) + "❤§b    §b-" + manaCost + " Mana (§6NONE§b)    " + Math.round(user.getIntelligence()) + "/" + Math.round(user.getTotalIntelligence()) + "✎ Mana");
            //new User(player).setIntelligence(new User(player).getIntelligence() - (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) : 0));
            p.setStat(PlayerStat.INTELLIGENCE,p.getStat(PlayerStat.INTELLIGENCE)-manaCost);
        } else {
            if (manaCost != 0) {
                SBX.abilityUsed.put(player, true);
                p.queueMiddleActionText(p,"§b    §b-" + manaCost + " Mana (§6" + name + "§b)    ",20L);
                //IUtil.sendActionText(player, "§c" + Math.round(user.getHealth()) + "/" + Math.round(user.getTotalHealth()) + "❤§b    §b-" + manaCost + " Mana (§6" + name + "§b)    " + Math.round(user.getIntelligence()) + "/" + Math.round(user.getTotalIntelligence()) + "✎ Mana");
                p.setStat(PlayerStat.INTELLIGENCE,p.getStat(PlayerStat.INTELLIGENCE)-manaCost);
            }
        }
        switch (String.valueOf(AbilityData.retrieveData(EnumAbilityData.BASE_ABILITY, item, i))) {
            case "INSTANT_TRANSMISSION":
                /*new InstantTransmission(player,
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) : 0),
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) : 0),
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) : 0),
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.SPEED, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.SPEED, item, i).toString()) : 0)).run();*/
                break;
            case "WITHER_IMPACT":
                /*new WitherImpact(player,
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.DAMAGE, item, i).toString()) : 0),
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.COOLDOWN, item, i).toString()) : 0),
                        (NumUtils.isInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) ? Integer.parseInt(AbilityData.retrieveData(EnumAbilityData.MANA_COST, item, i).toString()) : 0)).run();*/
                new WitherImpact().rightClickAirAction(player,event,item);
                break;
        }
        new FunctionHandler(player, i, String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i))).run();
    }
}
