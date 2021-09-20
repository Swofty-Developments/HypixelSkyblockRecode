package net.atlas.SkyblockSandbox.gui;

import dev.triumphteam.gui.guis.Gui;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public abstract class TwoPlayerGUI extends NormalGUI {

    private Gui p1Gui;
    private Gui p2Gui;
    private SBPlayer player1;
    private SBPlayer player2;

    public TwoPlayerGUI(SBPlayer tradeInit, SBPlayer player1, SBPlayer player2) {
        super(tradeInit);
        this.player1 = player1;
        this.player2 = player2;
    }

    @Deprecated
    @Override
    public Gui getGui() {
        return gui();
    }

    @Deprecated
    @Override
    public String getTitle() {
        return "";
    }

    public abstract String getp1title();

    public abstract String getp2title();

    public Gui getP1Gui() {
        return p1Gui;
    }

    public Gui getP2Gui() {
        return p2Gui;
    }

    @Override
    public void updateItems() {
        setItems();
        getP1Gui().update();
        getP1Gui().open(player1);
        getP2Gui().update();
        getP2Gui().open(player2);
    }

    @Override
    public void open() {
        this.p1Gui = Gui.gui()
                .title(Component.text(getp1title()))
                .rows(getRows())
                .create();

        this.p2Gui = Gui.gui()
                .title(Component.text(getp2title()))
                .rows(getRows())
                .create();
        setItems();
        if (!setClickActions()) {
            p1Gui.setDefaultClickAction(this::handleMenu);
            p2Gui.setDefaultClickAction(this::handleMenu);
        } else {
            p1Gui.setDefaultClickAction(event -> {
                event.setCancelled(true);
            });

            p2Gui.setDefaultClickAction(event -> {
                event.setCancelled(true);
            });
        }
        p1Gui.open(player1);
        p2Gui.open(player2);

    }


}
