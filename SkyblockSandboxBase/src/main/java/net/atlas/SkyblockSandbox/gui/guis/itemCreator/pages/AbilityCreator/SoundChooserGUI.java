package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.SBGUI;
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

public class SoundChooserGUI extends SBGUI {
    public static Set<Player> searching = new HashSet<>();
    public static Map<Player, String> search = new HashMap<>();

    private final int index2;
    private final int count;
    private final boolean update;
    private final Sound sound;
    private final PaginatedGui gui;
    public SoundChooserGUI(SBPlayer owner, int index, int count, boolean update, Sound sound) {
        super(owner);
        this.index2 = index;
        this.count = count;
        this.update = update;
        this.sound = sound;
        this.gui = Gui.paginated()
                .title(Component.text("Select a Sound (Page " + index+1 + ")"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        gui.getFiller().fillBorder(ItemBuilder.from(FILLER_GLASS).asGuiItem());

        List<ItemStack> items = new ArrayList<>();
        for(Sound value : Sound.values()){
            items.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "\n&eClick to set!"));
        }


        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53,next);

        if(gui.getCurrentPageNum() > 0) {
            setItem(45, prev);
        }

        ItemStack searchItem = makeColorfulItem(Material.ANVIL, "§aSearch Sounds", 1, 0,
                "§7Search through all sounds.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeColorfulItem(Material.ANVIL, "§aSearch Sounds", 1, 0,
                SUtil.colorize("&7Search through all sounds.\n&7Filtered: &6" + search.get(owner) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        ItemStack close = makeColorfulItem(Material.ARROW, "§cBack", 1, 0, "");
        setItem(49, close);

        if (search.containsKey(owner)) {
            setItem(48, searchItemsReset);

            List<ItemStack> matches = searchFor(search.get(owner), inventory, owner);

            if(items != null && !matches.isEmpty()) {
                for(int i = 0; i < getMaxItemsPerPage(); i++) {
                    index = getMaxItemsPerPage() * gui.getCurrentPageNum() + i;
                    if(index >= matches.size()) break;
                    gui.addItem(ItemBuilder.from(matches.get(index)).asGuiItem());
                }
            }
            return;
        }

        setItem(48, searchItem);
        if(items != null && !items.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * gui.getCurrentPageNum() + i;
                if(index >= items.size()) break;
               gui.addItem(ItemBuilder.from(items.get(index)).asGuiItem());
            }
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getCurrentItem() == null) return;
        List<ItemStack> items = new ArrayList<>();
        for(Sound value : Sound.values()){
            items.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "\n&eClick to set!"));
        }
        Player player = (Player) event.getWhoClicked();

        switch (event.getCurrentItem().getType()) {
            case STAINED_GLASS_PANE: {
                event.setCancelled(true);
                break;
            }
            case ANVIL: {
                if(event.getClick().equals(ClickType.RIGHT)) {
                    if (search.containsKey(player)) {
                        search.remove(player);
                        searching.remove(player);
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1f, 1.5f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SoundChooserGUI.this.open();
                            }
                        }.runTaskLater(SBX.getInstance(), 3);
                        return;

                    }
                    event.setCancelled(true);
                    return;
                }

                searching.add(player);

                new AnvilGUI(player, e -> {
                    if(e.getName() == null || e.getName() == "") {
                        new User(player).sendMessage("&cInvalid search!");
                        return;
                    }

                    SoundChooserGUI.search.put(player, e.getName());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SoundChooserGUI.this.open();
                        }
                    }.runTaskLater(Items.getInstance(), 4);
                }).setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeItem(Material.PAPER, "Enter search", 1, 0, IUtil.colorize("&6^^^^^^^\n&3Your Search"))).open();


                break;
            }
            case ARROW: {
                if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aNext")) {
                    if(!((index + 1) >= items.size())) {
                        page = page + 1;
                        super.open();
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are on the last page.");
                    }
                    break;
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aPrevious")) {
                    if (page == 0) {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                        player.sendMessage(ChatColor.RED + "You are already on the first page.");
                    } else {
                        page = page - 1;
                        super.open();
                    }
                    break;
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§cBack")){
                    if(sound == null) {
                        new FunctionsCreatorGUI(new PlayerMenuUtility(player), index2, count, update).open();
                    } else {
                        new FunctionsEditorGUI(new PlayerMenuUtility(player), "Sound Function", index2, count, update, sound).open();
                    }
                }
                break;
            }
            default: {
                if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.ID) && !(AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_TYPE) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_DELAY) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_AMOUNT) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_VOLUME) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_PITCH))) {
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.SOUND_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Sound Function"));
                } else {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.SOUND_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Sound Function"));
                }
                new FunctionsEditorGUI(new PlayerMenuUtility(player), "Sound Function" , index2, count, update, Sound.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), ""))).open();
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
        return null;
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public void setItems() {

    }


    private List<ItemStack> searchFor(String whatToSearch, Inventory inv, Player player) {
        List<ItemStack> matches = new ArrayList<>();
        List<ItemStack> toBeChecked = new ArrayList<>();
        List<ItemStack> list = new ArrayList<>();
        for(Sound value : Sound.values()){
            list.add(makeColorfulItem(Material.NOTE_BLOCK, "&a" + value.name(), 1, 0, "","&eClick to set!"));
        }

        for(ItemStack item : list) {
            if(item != null) {
                toBeChecked.add(item);
            }
        }
        for(ItemStack item : toBeChecked) {
            if(ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase()).contains(whatToSearch.toLowerCase())) {
                matches.add(item);
            }
        }

        return matches;
    }

    @Override
    public Gui getGUI() {

    }
}
