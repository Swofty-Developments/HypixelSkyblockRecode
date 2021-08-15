package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.Particles;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ParticleChooserGUI extends PaginatedGUI {
    public static Set<Player> searching = new HashSet<>();
    public static Map<Player, String> search = new HashMap<>();

    private final int index2;
    private final int count;
    private final boolean update;
    public ParticleChooserGUI(SBPlayer owner, int index, int count, boolean update) {
        super(owner);
        this.index2 = index;
        this.count = count;
        this.update = update;
    }

    @Override
    public String getTitle() {
        return "Select a Particle (Page " + getGui().getCurrentPageNum() + ")";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getCurrentItem() == null) return;
        if(!event.getClickedInventory().equals(getGui().getInventory())) return;

        List<ItemStack> items = AbilityData.ListOfParticles();
        Player player = (Player) event.getWhoClicked();

        switch (event.getCurrentItem().getType()) {
            case STAINED_GLASS_PANE: {
                event.setCancelled(true);
                break;
            }
            case ARROW: {
                if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aNext")) {
                    if(getGui().next()) {
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are on the last page.");
                    }
                    break;
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aPrevious")) {
                    if (!getGui().previous()){
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are already on the first page.");
                    }
                    break;
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§cBack")) {
                    new FunctionsCreatorGUI(getOwner(), index2, count, update).open();
                } else {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                    ItemStack item = event.getCurrentItem();
                    Particles value = AbilityData.ValueFromName(item.getItemMeta().getDisplayName().replace(SUtil.colorize("&7Particle: &a"), ""));
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.PARTICLE_TYPE)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.PARTICLE_TYPE, count, value.name()));
                    } else {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Particle Function"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.PARTICLE_TYPE, count, value.name()));
                    }
                    new FunctionsEditorGUI(getOwner(), "Particle Function", index2, count, update, value).open();
                }
                break;
            }
            default: {
                ItemStack item = event.getCurrentItem();
                Particles value = AbilityData.ValueFromName(item.getItemMeta().getDisplayName().replace(SUtil.colorize("&7Particle: &a"), ""));
                if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.PARTICLE_TYPE)) {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.PARTICLE_TYPE, count, value.name()));
                } else {
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Particle Function"));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.PARTICLE_TYPE, count, value.name()));
                }
                new FunctionsEditorGUI(getOwner(), "Particle Function", index2, count, update, value).open();
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            }
        }
    }

    @Override
    public int getPageSize() {
        return 21;
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());
        List<ItemStack> items = AbilityData.ListOfParticles();

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(44, next);

        if(getGui().getCurrentPageNum() > 0) {
            setItem(36, prev);
        }

        ItemStack close = makeColorfulItem(Material.ARROW, "§cBack", 1, 0);
        setItem(40, close);


        if(items != null && !items.isEmpty()) {
            for(ItemStack item:items) {
                getGui().addItem(ItemBuilder.from(item).asGuiItem());
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }
}
