package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.nemo_64.spigotutilities.playerinputs.chatinput.PlayerChatInput;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.Backable;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static net.atlas.SkyblockSandbox.util.NBTUtil.getInteger;
import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class ItemDescriptionGUI extends NormalGUI implements Backable {

    public static final String[] PROFANITIES = {
            "amy", "fuck", "shit", "ass", "nigga", "nigger", "niggger", "niggerr", "cum", "sex", "niger",
            "uwu", "owo", "motherfucker", "fucker", "maploop", "mightykloon", "swooftyy", "swofty", "sin_ender",
            "dngreenbean", "duop", "numpties", "amy224", "amy221", "lmfao", "mappy-chan", "mapy-chan", "coom", "dick",
            "pussy", "shlong", "virgin", "virginity", "vagina", "vaagina", "mom", "cock", "cok", "cocc", "dong",
            "bitch", "bitchass"
    };

    public ItemDescriptionGUI(SBPlayer player) {
        super(player);

    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        SBPlayer player = getOwner();
        event.setCancelled(true);
        if (event.getCurrentItem().getType().equals(Material.PAPER)) {
            if (event.getClick().equals(ClickType.RIGHT)) {
                SBItemBuilder stack = new SBItemBuilder(player.getItemInHand()).removeDescriptionLine(getInteger(event.getCurrentItem(), "Line"));
                System.out.println(stack.description);
                player.setItemInHand(stack.build());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new ItemDescriptionGUI(getOwner()).open();
                    }
                }.runTaskLater(SBX.getInstance(), 2);
            } else {
                if (false) {
                    String[] s = event.getCurrentItem().getItemMeta().getDisplayName().split("#");
                    int i = Integer.parseInt(s[1]) - 1;

                    ItemMeta meta = player.getItemInHand().getItemMeta();
                    List<String> l = new ArrayList<>(meta.getLore());
                    l.remove(l.get(i));
                    meta.setLore(l);
                    player.getItemInHand().setItemMeta(meta);
                    SBItemStack b = new SBItemStack(player.getItemInHand());
                    player.setItemInHand(b.addDescriptionLine((player.getItemInHand()), NBTUtil.getString(event.getCurrentItem(), "Lore_" + (i + 1))));
                    player.playSound(event.getWhoClicked().getLocation(), Sound.CAT_MEOW, 2, 0);
                    player.sendMessage(ChatColor.GREEN + "Success! Transferred line #" + (i + 1) + " to item description!");
                    new ItemDescriptionGUI(getOwner()).open();
                } else {
                    startChat(player, event.getCurrentItem(), false);
                }
            }
        }
    }

    @Override
    public boolean setClickActions() {
        SBPlayer player = getOwner();
        setAction(53, event -> {
            if (player.getItemInHand().hasItemMeta()) {
                if (player.getItemInHand().getItemMeta().hasLore()) {
                    if (player.getItemInHand().getItemMeta().getLore().size() > 48) {
                        player.sendMessage("§e------------------------------------------------");
                        player.sendMessage("§cYou have reached the item lore limit on your item!");
                   //     player.sendMessage("§cIf you want to increase it, you can buy a rank");
                   //     player.sendMessage("at §6§nhttps://store.skyblocksandbox.net§c!");
                        player.sendMessage("§e------------------------------------------------");
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        return;
                    }
                }
            }
            startChat(player, event.getCurrentItem(), true);
        });
        return false;
    }

    @Override
    public String getTitle() {
        return "Edit Item Description";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        SBPlayer player = getOwner();
        getGui().getFiller().fillBorder(ItemBuilder.from(FILLER_GLASS).asGuiItem());
        setItem(53, makeColorfulItem(Material.WOOL, "§aAdd line", 1, 5, SUtil.colorize("&7Add a line of lore to", "&7your item!", "", "&eClick to add!")));

        SBItemBuilder stack = new SBItemBuilder(player.getItemInHand());
        if (!stack.description.isEmpty()) {
            for (int i = 0; i < stack.description.size(); ++i) {
                int w = i + 1;
                ItemStack b = makeColorfulItem(Material.PAPER, "§aDescription #" + w, 1, 0, SUtil.colorize("§7Lore Content:", "", stack.description.get(i) + "", "", "&eClick to edit!", "&eRight-Click to remove"));
                b = NBTUtil.setInteger(b, i, "Line");
                getGui().addItem(ItemBuilder.from(b).asGuiItem());
            }
        }
    }

    private void startChat(Player p, ItemStack stack, boolean add) {
        SBItemBuilder item = new SBItemBuilder(p.getItemInHand());
        p.closeInventory();
        int line = !add ? getInteger(stack, "Line") : item.description.size();
        PlayerChatInput.PlayerChatInputBuilder<String> builder = new PlayerChatInput.PlayerChatInputBuilder<>(SBX.getInstance(), p);
        builder.onExpireMessage(colorize("&cYou failed to set the lore after 3 minute.")).sendValueMessage(colorize("&7Please input your description for line " + (line + 1) + "!\n&eSay &ccancel &eto stop."));
        builder.setValue((player, str) -> {
            return str;
        });
        builder.isValidInput((player, str) -> {
            if (str.isEmpty()) {
                getOwner().sendMessage("&cThat is not a valid input!");
                new ItemDescriptionGUI(getOwner()).open();
                return false;
            }
            for (String s : PROFANITIES) {
                if (ChatColor.stripColor(str).toLowerCase().contains(s)) {
                    player.sendMessage("§cYou cannot use that word in your lore!");
                    new ItemDescriptionGUI(getOwner()).open();
                    return false;
                }
            }
            return true;
        }).onFinish((player, str) -> {
            if(add) {
                player.setItemInHand(item.addDescriptionLine(str).build());
            } else {
                player.setItemInHand(item.setDescriptionLine(line, str).build());
            }
            new ItemDescriptionGUI(getOwner()).open();
        }).onInvalidInput(((player, s) -> true)).expiresAfter(3600).onExpire((player -> {
            new ItemDescriptionGUI(getOwner()).open();
        })).toCancel("cancel").onCancel((player -> {
            getOwner().sendMessage("&cCanceled!");
            new ItemDescriptionGUI(getOwner()).open();
        }));
        builder.build().start();
    }

    @Override
    public void openBack() {
        new ItemCreatorGUIMain(getOwner()).open();
    }

    @Override
    public String backTitle() {
        return "Auto Item Creator";
    }

    @Override
    public int backItemSlot() {
        return 48;
    }
}
