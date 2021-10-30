package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.nemo_64.spigotutilities.playerinputs.chatinput.PlayerChatInput;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.PaginatedGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.atlas.SkyblockSandbox.util.NBTUtil.getInteger;
import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class SetAbilityDescriptionMenu extends PaginatedGUI {
    private final int indexx;

    public SetAbilityDescriptionMenu(SBPlayer owner, int indexx) {
        super(owner);
        this.indexx = indexx;
    }

    @Override
    public String getTitle() {
        return "Set ability description line";
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
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 52:
                startChat(p, event.getCurrentItem(), true);
                break;
            case 45:
                if(getGui().getPrevPageNum()==1&&getGui().getCurrentPageNum()==1) {
                    new AbilityCreatorGUI(getOwner(),indexx).open();
                }  else {
                    getGui().previous();
                }
                break;
            case 53:
                getGui().next();
                break;
            default:
                if(event.getCurrentItem().getType().equals(Material.WOOL)) {
                    if(event.getClick()== ClickType.LEFT) {
                        ItemStack i = event.getCurrentItem();
                        AnvilGUI anvilGUI2 = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event2) {
                                if(event2.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                                    event2.setWillClose(true);
                                    event2.setWillDestroy(true);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SBItemStack p1 = new SBItemStack(getOwner().getItemInHand());
                                            SBItemStack p2 = new SBItemStack(event.getCurrentItem());
                                            ItemStack i = p1.setAbilDescriptLine(colorize(event2.getName()),indexx, p2.getInteger(p1.asBukkitItem(),"Line"));
                                            p1 = new SBItemStack(i);
                                            i = p1.refreshLore();
                                            getOwner().setItemInHand(i);
                                            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
                                        }
                                    }.runTaskLater(SBX.getInstance(), 1);
                                }
                            }
                        });
                        anvilGUI2.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter Lore Line", 1, 0, "§a^^^^^","§7Enter a lore line in the","§7text box!"));
                        anvilGUI2.open();
                        break;
                    } else if (event.getClick() == ClickType.RIGHT) {
                        startChat(p, event.getCurrentItem(), false);
                    }
                    else {
                        if(event.getClick()==ClickType.MIDDLE) {
                            ItemStack i = event.getCurrentItem();

                            SBItemStack p1 = new SBItemStack(getOwner().getItemInHand());
                            i = p1.removeAbilityDescriptionLine(p1.asBukkitItem(),indexx, p1.getInteger(i,"Line"));
                            p1 = new SBItemStack(i);
                            i = p1.refreshLore();
                            getOwner().setItemInHand(i);
                            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
                            p.playSound(p.getLocation(), Sound.CAT_MEOW,1,2);
                            break;
                        }
                    }
                }
        }
    }

    @Override
    public void setItems() {
        getGui().getFiller().fillBorder(ItemBuilder.from(super.FILLER_GLASS).name(Component.text(colorize("&7 "))).asGuiItem());
        
        SBItemBuilder builder = new SBItemBuilder(getOwner().getItemInHand());
        List<String> lore = builder.abilities.get(indexx).description;

        ItemStack next = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Next Page", 1, 0, "§7Go to the next page.");
        ItemStack prev = makeColorfulItem(Material.ARROW, ChatColor.GREEN + "Previous Page", 1, 0, "§7Go to the previous page.");
        setItem(53, next);
        setItem(52,makeColorfulItem(Material.BOOK_AND_QUILL,ChatColor.GREEN + "Add lore line",1,0,ChatColor.GRAY + "Click to add a lore line."));

        if(getGui().getCurrentPageNum() > 1) {
            setItem(45, prev);
        } else {
            setItem(45, prev);
        }

        ItemStack close = makeColorfulItem(Material.BARRIER, "§cClose", 1, 0);
        setItem(49, close);


        if(!lore.isEmpty()) {
            for(String s:lore) {
                ItemStack loreItem = new ItemStack(Material.WOOL,1,DyeColor.LIME.getData());
                ItemMeta meta = loreItem.getItemMeta();
                meta.setDisplayName(colorize("&aLine #" + lore.indexOf(s)));
                String newLore = colorize(lore.get(lore.indexOf(s)));
                List<String> finalList = new ArrayList<>();
                finalList.add(newLore);

                finalList.addAll(colorize("", "&eLeft-Click to edit!", "", "&eRight-Click to edit in chat!", "&7or if you don't have enough space", "&7and want to add special &aSymbols&7.", "", "&bMiddle-Click to remove!"));
                meta.setLore(finalList);
                loreItem.setItemMeta(meta);
                loreItem = NBTUtil.setString(loreItem, UUID.randomUUID().toString(),"UUID");
                loreItem = NBTUtil.setInteger(loreItem, lore.indexOf(s),"Line");
                getGui().addItem(ItemBuilder.from(loreItem).asGuiItem());
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    private void startChat(Player p, ItemStack stack, boolean add) {
        p.closeInventory();
        int line = getInteger(stack, "Line");
        PlayerChatInput.PlayerChatInputBuilder<String> builder = new PlayerChatInput.PlayerChatInputBuilder<>(SBX.getInstance(), p);
        builder.onExpireMessage(colorize("&cYou failed to set the lore after 1 minute.")).sendValueMessage(colorize("&7Please input your description for line " + line + "!\n&eSay &ccancel &eto stop."));
        builder.setValue((player, str) -> {
            return str;
        });
        builder.isValidInput((player, str) -> !(str.equals("") || str.isEmpty())).onFinish((player, str) -> {
            SBItemBuilder item = new SBItemBuilder(player.getItemInHand());
            if(add) {
                player.setItemInHand(item.abilities.get(indexx).addDescription(str).build().build());
            } else {
                player.setItemInHand(item.abilities.get(indexx).setDescription(line, str).build().build());
            }
            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
        }).onInvalidInput(((player, s) -> {
            getOwner().sendMessage("&cThat is not a valid input!");
            return true;
        })).expiresAfter(3600).onExpire((player -> {
            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
        })).toCancel("cancel").onCancel((player -> {
            getOwner().sendMessage("&cCanceled!");
            new SetAbilityDescriptionMenu(getOwner(),indexx).open();
        }));
        builder.build().start();
    }
}
