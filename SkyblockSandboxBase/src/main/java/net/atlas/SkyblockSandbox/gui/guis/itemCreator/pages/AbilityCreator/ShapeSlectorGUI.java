package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Particles;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShapeSlectorGUI extends SBGUI {
    private final int index;
    private final String function;
    private final int count;
    private final boolean update;
    private final Particles particle;
    private SBPlayer player;
    public ShapeSlectorGUI(SBPlayer owner, String fuction, int index, int count, boolean update, Particles particle) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        this.particle = particle;
        this.player = owner;
    }

    @Override
    public String getTitle() {
        return "Item Functions";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        setAction(31,event -> {
            //new ParticleChooserGUI(getOwner(), index, count, update).open();
        });

        /*for(int i=0;i<4;i++) {
            setAction(i+15,event -> {
                SBItemStack b = new SBItemStack(event.getCurrentItem());
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals("&7Shape: &aOval") || event.getCurrentItem().getItemMeta().getDisplayName().equals("&7Shape: &aSquare")) return;
                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE)) {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHAPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&7Shape: &a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, count, Objects.requireNonNull(particle).name()));
                } else {
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHAPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&7Shape: &a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, count, Objects.requireNonNull(particle).name()));
                }
                new FunctionsEditorGUI(new PlayerMenuUtility(player), function, index, count, update, particle);
            });
        }*/

        return true;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(12, makeColorfulItem(Material.ENDER_PEARL,"&7Shape: &aCircle", 1, 0, "&7Set the shape of the","&7particle played!","","&eClick to set!"));
        setItem(13, makeColorfulItem(Material.EGG,"&7Shape: &aOval", 1, 0, "&7Set the shape of the","&7particle played!","","&cNOT IMPLEMENTED"));
        setItem(14, makeColorfulItem(Material.IRON_BLOCK,"&7Shape: &aSquare", 1, 0, "&7Set the shape of the","&7particle played!","","&cNOT IMPLEMENTED"));
        setItem(15, makeColorfulSkullItem("&7Shape: &aOn Player", player.getDisplayName(),1, "&7Set the shape of the","&7particle played!","","&eClick to set!"));
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Function Editor #" + count));
    }
}
