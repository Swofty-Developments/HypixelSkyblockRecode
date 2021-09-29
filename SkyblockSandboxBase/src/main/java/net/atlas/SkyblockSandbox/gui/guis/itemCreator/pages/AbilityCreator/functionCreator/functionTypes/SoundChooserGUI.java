package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes;

import com.google.common.base.Enums;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionSelectorGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionsEditorGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SoundChooserGUI extends PaginatedGUI {
    public static Set<UUID> searching = new HashSet<>();
    public static Map<UUID, String> search = new HashMap<>();

    private final int index2;
    private final int funcIndex;
    private final Sound sound;

    public SoundChooserGUI(SBPlayer owner, int index, int funcIndex) {
        super(owner);
        this.index2 = index;
        this.funcIndex = funcIndex;
        this.sound = Enums.getIfPresent(org.bukkit.Sound.class, FunctionUtil.getFunctionData(owner.getItemInHand(), index, funcIndex, net.atlas.SkyblockSandbox.abilityCreator.functions.Sound.dataValues.SOUND)).orNull();
        ;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getCurrentItem() == null) return;
        List<ItemStack> items = new ArrayList<>();
        for (Sound value : Sound.values()) {
            items.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "\n&eClick to set!"));
        }
        Player player = (Player) event.getWhoClicked();

        switch (event.getCurrentItem().getType()) {
            case STAINED_GLASS_PANE: {
                event.setCancelled(true);
                break;
            }
            case ANVIL: {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    if (search.containsKey(player.getUniqueId())) {
                        search.remove(player.getUniqueId());
                        searching.remove(player.getUniqueId());
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1f, 1.5f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 3);
                        return;

                    }
                    event.setCancelled(true);
                    return;
                }

                searching.add(player.getUniqueId());

                new AnvilGUI(player, e -> {
                    if (e.getName() == null || e.getName().equals("")) {
                        player.sendMessage(SUtil.colorize("&cInvalid Search!"));
                        return;
                    }

                    SoundChooserGUI.search.put(player.getUniqueId(), e.getName());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateItems();
                        }
                    }.runTaskLater(SBX.getInstance(), 4);
                }).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter search", 1, 0, SUtil.colorize("&6^^^^^^^\n&3Your Search"))).open();


                break;
            }
            case ARROW: {
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aNext")) {
                    if (getGui().next()) {
                        getGui().updateTitle("Select a Sound (Page " + getGui().getCurrentPageNum() + ")");
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are on the last page.");
                    }
                    break;
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§aPrevious")) {
                    if (!getGui().previous()) {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are already on the first page.");
                    } else {
                        getGui().updateTitle("Select a Sound (Page " + getGui().getCurrentPageNum() + ")");
                    }
                    break;
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("§cBack")) {
                    if (sound == null) {
                        new FunctionSelectorGUI(getOwner(), index2, funcIndex).open();
                    } else {
                        new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.SOUND, index2, funcIndex).open();
                    }
                }
                break;
            }
            default: {
                player.setItemInHand(FunctionUtil.setFunctionData(player.getItemInHand(),index2,funcIndex, net.atlas.SkyblockSandbox.abilityCreator.functions.Sound.dataValues.SOUND,event.getCurrentItem().getItemMeta().getDisplayName().replace(SUtil.colorize("&a"), "").toUpperCase()));
                new FunctionsEditorGUI(getOwner(), AbilityValue.FunctionType.SOUND, index2, funcIndex).open();
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public String getTitle() {
        if (getGui() == null) {
            return "Select a Sound (Page 1)";
        }
        return "Select a Sound (Page " + getGui().getCurrentPageNum() + ")";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public int getPageSize() {
        return 28;
    }

    @Override
    public void setItems() {
        for(int i = 0;i<getGui().getInventory().getSize();i++) {
            getGui().removeItem(i);
        }
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(SUtil.colorize("&7 "))).asGuiItem());
        List<ItemStack> items = new ArrayList<>();
        for (Sound value : Sound.values()) {
            items.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "\n&eClick to set!"));
        }


        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);

        if (getGui().getCurrentPageNum() > 0) {
            setItem(45, prev);
        }

        ItemStack searchItem = makeColorfulItem(Material.ANVIL, "§aSearch Sounds", 1, 0,
                "§7Search through all sounds.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeColorfulItem(Material.ANVIL, "§aSearch Sounds", 1, 0,
                SUtil.colorize("&7Search through all sounds.\n&7Filtered: &6" + search.get(getOwner().getUniqueId()) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        ItemStack close = makeColorfulItem(Material.ARROW, "§cBack", 1, 0, "");
        setItem(49, close);

        if (search.containsKey(getOwner().getUniqueId())) {
            setItem(48, searchItemsReset);

            List<ItemStack> matches = searchFor(search.get(getOwner().getUniqueId()), getGui().getInventory(), getOwner());

            if (!matches.isEmpty()) {
                for (ItemStack i : matches) {
                    getGui().addItem(ItemBuilder.from(i).asGuiItem());
                }
            }
            return;
        }

        setItem(48, searchItem);
        if (!items.isEmpty()) {
            for (ItemStack i : items) {
                getGui().addItem(ItemBuilder.from(i).asGuiItem());
            }
        }
    }


    private List<ItemStack> searchFor(String whatToSearch, Inventory inv, Player player) {
        List<ItemStack> matches = new ArrayList<>();
        List<ItemStack> toBeChecked = new ArrayList<>();
        List<ItemStack> list = new ArrayList<>();
        for (Sound value : Sound.values()) {
            list.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "", "&eClick to set!"));
        }

        for (ItemStack item : list) {
            if (item != null) {
                toBeChecked.add(item);
            }
        }
        for (ItemStack item : toBeChecked) {
            if (ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase()).contains(whatToSearch.toLowerCase())) {
                matches.add(item);
            }
        }

        return matches;
    }

}
