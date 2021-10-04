package net.atlas.SkyblockSandbox.listener.sbEvents.skillEvents;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.event.customEvents.SkillEXPGainEvent;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.skills.SkillType;
import net.atlas.SkyblockSandbox.slayer.SlayerTier;
import net.atlas.SkyblockSandbox.slayer.Slayers;
import net.atlas.SkyblockSandbox.sound.Jingle;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static net.atlas.SkyblockSandbox.SBX.activeSlayers;
import static net.atlas.SkyblockSandbox.SBX.prevSkillEvent;

public class SkillXPGainListener extends SkyblockListener<SkillEXPGainEvent> {
    @EventHandler
    public void callEvent(SkillEXPGainEvent event) {
        SBPlayer p = event.getPlayer();

        if(event.isCancelled()) {
            return;
        }

        p.addSkillXP(event.getSkill(), event.getExpAmt());

        /*if(p.getCurrentSkillExp(event.getSkill())>=SkillType.getLvlXP(p.getSkillLvl(event.getSkill()))) {
            p.levelSkill(event.getSkill());
        }*/

        if (event.getSkill().equals(SkillType.COMBAT)) {
            if (activeSlayers.containsKey(p.getUniqueId())) {
                activeSlayers.get(p.getUniqueId()).values().stream().findFirst().ifPresent(playerXpMap -> {
                    Slayers slayer = activeSlayers.get(p.getUniqueId()).keySet().stream().findFirst().orElse(null);
                    assert slayer != null;
                    if(event.getDamage().getEntity().getType().equals(slayer.getSlayerClass().getSlayerType())) {

                        SlayerTier tier = playerXpMap.keySet().stream().findFirst().orElse(null);
                        Double playerXP = playerXpMap.get(playerXpMap.keySet().stream().findFirst().orElse(null));
                        if (playerXP >= 0) {
                            playerXP += event.getExpAmt();
                            playerXpMap.put(tier, playerXP);
                            HashMap<Slayers, HashMap<SlayerTier, Double>> temp2 = new HashMap<>();
                            temp2.put(slayer, playerXpMap);
                            activeSlayers.put(p.getUniqueId(), temp2);
                            assert tier != null;
                            if (playerXP >= tier.getXpAmt()) {
                                Entity en = slayer.spawn(p, tier, event.getDamage());
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(en.isDead()) {
                                            p.playJingle(Jingle.RARE_DROP,false);
                                            p.sendMessage(SUtil.colorize("  &6&lNICE! SLAYER BOSS SLAIN!"));
                                            p.sendMessage(SUtil.colorize("   &5&l> &7Talk to Maddox to claim your " + slayer.getSingular() + " Slayer XP!"));
                                            activeSlayers.remove(p.getUniqueId());
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(SBX.getInstance(),1L,5);
                                playerXpMap.put(tier, -1D);
                                temp2.put(slayer, playerXpMap);
                                activeSlayers.put(p.getUniqueId(), temp2);
                            }
                        }
                    }
                });
            }
        }

        p.queueMiddleActionText(p,SUtil.colorize(" &3+" + event.getExpAmt() + " " + event.getSkill().getName() + " (" + p.getCurrentSkillExp(event.getSkill()) + "/" + SkillType.getLvlXP(p.getSkillLvl(event.getSkill())) + ")"),20L);
    }
}
