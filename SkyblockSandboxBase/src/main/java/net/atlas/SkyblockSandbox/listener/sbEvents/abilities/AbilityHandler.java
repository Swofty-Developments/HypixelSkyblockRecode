package net.atlas.SkyblockSandbox.listener.sbEvents.abilities;

import net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.SBPlayer.PlayerStat;
import net.atlas.SkyblockSandbox.util.NumUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

public class AbilityHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        try {
            if (event.getPlayer().getItemInHand() == null) return;
            if (!event.getItem().hasItemMeta()) return;
            if (!AbilityData.hasAbility(event.getItem())) return;
            ItemStack item = event.getItem();
            SBPlayer player = new SBPlayer(event.getPlayer());

            if (player.getItemInHand().getType().equals(Material.AIR)) return;
            if (!player.getItemInHand().hasItemMeta()) return;
            if (!AbilityData.hasAbility(player.getItemInHand())) return;

            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (!player.isSneaking()) {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("LEFT_CLICK")) {
                            function(item, i, player, event);
                        }
                    }
                } else {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("SHIFT_LEFT_CLICK")) {
                            function(item, i, player, event);
                        }
                    }
                }
            }
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (!player.isSneaking()) {
                    for (int i = 0; i < 6; ++i) {
                        if (AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i).equals("RIGHT_CLICK")) {
                            function(item, i, player, event);
                        }
                    }
                    return;
                } else {
                    for (int i = 0; i < 6; ++i) {
                        if (String.valueOf(AbilityData.retrieveData(EnumAbilityData.FUNCTION, item, i)).equals("SHIFT_RIGHT_CLICK")) {
                            function(item, i, player, event);
                        }
                    }
                }
            }
        }catch(NullPointerException e){

        }
    }
    public void function(ItemStack item, int aindex, SBPlayer player, PlayerInteractEvent event) {
        for (int i = 1; i < 6; i++) {
            for(Class<? extends AdvancedFunctions> l:new Reflections("net.atlas.SkyblockSandbox.abilityCreator.functions").getSubTypesOf(AdvancedFunctions.class)) {
                try {
                    AdvancedFunctions function = l.newInstance();
                    if(function.name().equals(String.valueOf(AbilityData.retrieveFunctionData("name", player.getItemInHand(), aindex, i)))) {
                        function.setOwner(new SBPlayer(player));
                        function.setAindex(aindex);
                        function.setFindex(i);
                        function.runnable();
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
