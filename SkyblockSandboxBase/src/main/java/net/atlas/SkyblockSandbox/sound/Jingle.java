package net.atlas.SkyblockSandbox.sound;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

import static net.atlas.SkyblockSandbox.sound.SoundUtil.ferocityProc;
import static net.atlas.SkyblockSandbox.sound.SoundUtil.rareDropJingle;
import static net.atlas.SkyblockSandbox.sound.SoundUtil.*;

public enum Jingle {
    RARE_DROP {
        @Override
        public void send(Player p,boolean loop) {
            rareDropJingle(p);
        }
    },
    FEROCITY_START {
        @Override
        public void send(Player p,boolean loop) {
            ferocityProcStart(p);
        }
    },
    FEROCITY {
        @Override
        public void send(Player p,boolean loop) {
            ferocityProc(p);
        }
    },
    SOULCRY {
        @Override
        public void send(Player p,boolean loop) {
            soulCry(p);
        }
    };
    /** NEEDS A FILE IN THERE OTHERWISE IT WILL BREAK **/

    /*DUNGEON_DRAMA {
        @Override
        public void send(Player p, boolean loop) {
            Song song = NBSDecoder.parse(new File(SBX.getInstance().getDataFolder().getPath() + "/Jingles/dungeon_drama.nbs"));
            playSong(song,p,loop);
        }
    };*/
    public abstract void send(Player p,boolean loop);

    public void playSong(Song song,Player p,boolean loop) {
        RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.addPlayer(p);
        if(loop) {
            rsp.setRepeatMode(RepeatMode.ALL);
        }
        rsp.setPlaying(true);
    }
}
