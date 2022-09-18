package net.atlas.SkyblockSandbox.island.islands.bossRush;

import net.atlas.SkyblockSandbox.player.SBPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BossHandler {
    private DungeonBoss db;
    private List<Consumer<SBPlayer>> aiActions = new ArrayList<>();

    public BossHandler(DungeonBoss _db) {
        this.db = _db;
    }

    public Consumer<SBPlayer> addAIAction(Consumer<SBPlayer> cs) {
        aiActions.add(cs);
        return cs;
    }


}
