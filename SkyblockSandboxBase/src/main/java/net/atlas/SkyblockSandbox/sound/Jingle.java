package net.atlas.SkyblockSandbox.sound;

import org.bukkit.entity.Player;

import static net.atlas.SkyblockSandbox.sound.SoundUtil.ferocityProc;
import static net.atlas.SkyblockSandbox.sound.SoundUtil.rareDropJingle;
import static net.atlas.SkyblockSandbox.sound.SoundUtil.*;

public enum Jingle {
    RARE_DROP {
        public void send(Player p) {
            rareDropJingle(p);
        }
    },
    FEROCITY_START {
        public void send(Player p) {
            ferocityProcStart(p);
        }
    },
    FEROCITY {
        public void send(Player p) {
            ferocityProc(p);
        }
    },
    SOULCRY {
        @Override
        public void send(Player p) {
            soulCry(p);
        }
    };

    public abstract void send(Player p);
}
