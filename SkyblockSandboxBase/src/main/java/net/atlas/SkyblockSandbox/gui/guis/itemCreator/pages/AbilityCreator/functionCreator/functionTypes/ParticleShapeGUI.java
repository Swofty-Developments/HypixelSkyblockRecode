package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes;

import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Particle;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ParticleShapeGUI extends NormalGUI {
    private int index;
    private int funcIndex;
    public ParticleShapeGUI(SBPlayer owner, int index, int funcIndex) {
        super(owner);
        this.index = index;
        this.funcIndex = funcIndex;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        int i = 0;
        for (Particle.ParticleShape shape:Particle.ParticleShape.values()) {
            setAction(i+11,event -> {
                getOwner().setItemInHand(FunctionUtil.setFunctionData(getOwner().getItemInHand(),index,funcIndex,Particle.dataValues.PARTICLE_SHAPE, shape));
            });
            i++;
        }
        return true;
    }

    @Override
    public String getTitle() {
        return "Choose a Particle Shape";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        int i = 0;
        for (Particle.ParticleShape shape:Particle.ParticleShape.values()) {
            setItem(i+11,makeColorfulItem(shape.getMat(),"&a" + SUtil.firstLetterUpper(shape.name()),1,0,"","&eClick to set the shape!"));
            i++;
        }
    }
}
