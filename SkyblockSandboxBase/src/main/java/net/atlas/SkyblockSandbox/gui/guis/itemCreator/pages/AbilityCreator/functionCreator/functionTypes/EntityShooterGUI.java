package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes;


import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.item.ability.Entities;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityShooterGUI extends PaginatedGUI {
    public static Set<Player> searching = new HashSet<>();
    public static Map<Player, String> search = new HashMap<>();

    private final int index2;
    private final int count;
    private final boolean update;
    private final Entities entity;
    private final SBPlayer owner;

    public EntityShooterGUI(SBPlayer owner, int index, int count, boolean update, Entities entity) {
        super(owner);
        this.index2 = index;
        this.count = count;
        this.update = update;
        this.entity = entity;
        this.owner = owner;
    }

    @Override
    public String getTitle() {
        return "Select a Entity (Page " + getGui().getCurrentPageNum() + ")";
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
    public void handleMenu(InventoryClickEvent event) {
       /* event.setCancelled(true);
        if(event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getCurrentItem() == null) return;
        List<ItemStack> items = new ArrayList<>();
        for(Entities value : Entities.values()){
            if(value.getC() != null) {
                items.add(makeColorfulItem(value.getC(), "&a" + value.name(), 1, 0, "\n&eClick to set!"));
            } else {
                items.add(makeColorfulCustomSkullItem(value.getB(), "&a" + value.name(), 1, "\n&eClick to set!"));
            }
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
                                EntityShooterGUI.this.open();
                            }
                        }.runTaskLater(Items.getInstance(), 3);
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

                    EntityShooterGUI.search.put(player, e.getName());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            EntityShooterGUI.this.open();
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
                    if(entity == null) {
                        new FunctionsCreatorGUI(new PlayerMenuUtility(player), index2, count, update).open();
                    } else {
                        new FunctionsEditorGUI(new PlayerMenuUtility(player), "Entity Shooter Function", index2, count, update, entity).open();
                    }
                } else {
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.ID) && !(AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_TYPE) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_DELAY) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_AMOUNT) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_VOLUME) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_PITCH))) {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.ENTITY_SHOOTER_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Entity Shooter Function"));
                    } else {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.ENTITY_SHOOTER_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Entity Shooter Function"));
                    }
                    new FunctionsEditorGUI(new PlayerMenuUtility(player), "Entity Shooter Function" , index2, count, update, Entities.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), ""))).open();
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                }
                break;
            }
            default: {
                if (AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.ID) && !(AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_TYPE) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_DELAY) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_AMOUNT) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_VOLUME) || AbilityData.hasFunctionData(player.getItemInHand(), index2, count, EnumFunctionsData.SOUND_PITCH))) {
                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index2, count, player));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.ENTITY_SHOOTER_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Entity Shooter Function"));
                } else {
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.ENTITY_SHOOTER_TYPE, count, event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), "")));
                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index2, EnumFunctionsData.NAME, count, "Entity Shooter Function"));
                }
                new FunctionsEditorGUI(new PlayerMenuUtility(player), "Entity Shooter Function" , index2, count, update, Entities.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().replace(IUtil.colorize("&a"), ""))).open();
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            }
        }
    }
    */
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        /*
        fillBorder();

        List<ItemStack> items = new ArrayList<>();
        for(Entities value : Entities.values()){
            if(value.getC() != null) {
                items.add(makeColorfulItem(value.getC(), "&a" + value.name(), 1, 0, "\n&eClick to set!"));
            } else {
                items.add(makeColorfulCustomSkullItem(value.getB(), "&a" + value.name(), 1, "\n&eClick to set!"));
            }
        }


        ItemStack next = makeItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        inventory.setItem(53, next);

        if(page > 0) {
            inventory.setItem(45, prev);
        }

        ItemStack searchItem = makeItem(Material.ANVIL, "§aSearch Entities", 1, 0,
                "§7Search through all Entities.\n\n§eClick to search!");

        ItemStack searchItemsReset = makeItem(Material.ANVIL, "§aSearch Entities", 1, 0,
                IUtil.colorize("&7Search through all Entities.\n&7Filtered: &6" + search.get(playerMenuUtility.getOwner()) + "\n\n&eClick to search!\n&bRight-Click to reset!"));

        ItemStack close = makeColorfulItem(Material.ARROW, "§cBack", 1, 0, "");
        inventory.setItem(49, close);

        if (search.containsKey(playerMenuUtility.getOwner())) {
            inventory.setItem(48, searchItemsReset);

            List<ItemStack> matches = searchFor(search.get(playerMenuUtility.getOwner()), inventory, playerMenuUtility.getOwner());

            if(items != null && !matches.isEmpty()) {
                for(int i = 0; i < getMaxItemsPerPage(); i++) {
                    index = getMaxItemsPerPage() * page + i;
                    if(index >= matches.size()) break;
                    inventory.addItem(matches.get(index));
                }
            }
            return;
        }

        inventory.setItem(48, searchItem);
        if(items != null && !items.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if(index >= items.size()) break;
                inventory.addItem(items.get(index));
            }
        }
    }


    private List<ItemStack> searchFor(String whatToSearch, Inventory inv, Player player) {
        List<ItemStack> matches = new ArrayList<>();
        List<ItemStack> toBeChecked = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        for(Entities value : Entities.values()){
            if(value.getC() != null) {
                items.add(makeColorfulItem(value.getC(), "&a" + value.name(), 1, 0, "\n&eClick to set!"));
            } else {
                items.add(makeColorfulCustomSkullItem(value.getB(), "&a" + value.name(), 1, "\n&eClick to set!"));
            }
        }

        for(ItemStack item : items) {
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
         */
    }


}