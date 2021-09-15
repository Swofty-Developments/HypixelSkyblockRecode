package net.atlas.SkyblockSandbox.sound;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundUtil {

    public static void soundWithDelay(Player p, Sound sound,float volume,float pitch,long delay) {
        new BukkitRunnable() {

            @Override
            public void run() {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }.runTaskLater(SBX.getInstance(), delay);
    }


    public static void rareDropJingle(Player p) {
        soundWithDelay(p,Sound.NOTE_PLING,1, 0.594604F,6);
        soundWithDelay(p,Sound.NOTE_PLING, 1,0.793701F,9);
        soundWithDelay(p,Sound.NOTE_PLING, 1,1.059436F,12 );
        soundWithDelay(p,Sound.NOTE_PLING, 1, 1.189207F,15);
    }

    public static void ferocityProc(Player p) {
        soundWithDelay(p,Sound.IRONGOLEM_THROW, 1,1.498307F,0);
        soundWithDelay(p,Sound.IRONGOLEM_THROW, 1,1.498307F,0);
        soundWithDelay(p,Sound.ZOMBIE_WOODBREAK, 0.1F, 1.498307F,0);
    }

    public static void soulCry(Player p) {
        soundWithDelay(p,Sound.GHAST_SCREAM2, 1,1.887749F,0);
        soundWithDelay(p,Sound.GHAST_SCREAM2, 1,1.781797F,1);
        soundWithDelay(p,Sound.GHAST_SCREAM2, 1, 1.587401F,1);
    }

    public static void ferocityProcStart(Player p) {
        soundWithDelay(p,Sound.FIRE_IGNITE,2, 0.594604F,0);
    }


}
