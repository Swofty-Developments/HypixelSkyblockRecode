package net.atlas.SkyblockSandbox.item.ability;

import net.atlas.SkyblockSandbox.item.SBItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class AbiltyListener implements Listener {

    Ability ability;

    public AbiltyListener(Ability ability) {
        this.ability = ability;
    }

    @EventHandler
    public void clickEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        SBItemStack sbitem = new SBItemStack(item);
        if (item != null && item.hasItemMeta()) {
            if (sbitem.hasAbility()) {
                for (int i = 0; i <= sbitem.getAbilAmount(); i++) {
                    if (((String) sbitem.getAbilData(EnumAbilityData.NAME, i)).equalsIgnoreCase(ability.getAbilityName())) {
                        switch (e.getAction()) {
                            case RIGHT_CLICK_BLOCK:
                                if (p.isSneaking()) {
                                    ability.shiftRightClickBlockAction(p, e, e.getClickedBlock(), e.getItem());
                                } else {
                                    ability.rightClickBlockAction(p, e, e.getClickedBlock(), e.getItem());
                                }
                                break;
                            case RIGHT_CLICK_AIR:
                                if (p.isSneaking()) {
                                    ability.shiftRightClickAirAction(p, e, e.getItem());
                                } else {
                                    ability.rightClickAirAction(p, e, e.getItem());
                                }
                                break;
                            case LEFT_CLICK_AIR:
                                if (p.isSneaking()) {
                                    ability.shiftLeftClickAirAction(p, e.getItem());
                                } else {
                                    ability.leftClickAirAction(p, e.getItem());
                                }
                                break;
                            case LEFT_CLICK_BLOCK:
                                if (p.isSneaking()) {
                                    ability.shiftLeftClickBlockAction(p, e, e.getClickedBlock(), e.getItem());
                                } else {
                                    ability.leftClickBlockAction(p, e, e.getClickedBlock(), e.getItem());
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void rightClickEntityEvent(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        ability.rightClickEntityEvent(p, e);
    }


    @EventHandler
    public void shiftEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (p.isSneaking()) {
            ability.shiftEvent(p, e);
        }
    }
}
