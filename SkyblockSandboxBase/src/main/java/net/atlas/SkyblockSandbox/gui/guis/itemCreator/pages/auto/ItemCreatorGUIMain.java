package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto;

import me.nemo_64.spigotutilities.playerinputs.chatinput.PlayerChatInput;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemTypeGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilitySelectorGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.TagsEditorGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.stream.Stream;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.ItemDescriptionGUI.PROFANITIES;
import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class ItemCreatorGUIMain extends NormalGUI {

    public ItemCreatorGUIMain(SBPlayer owner) {
        super(owner);
    }

    public void handleMenu(InventoryClickEvent event) {
    }

    @Override
    public boolean setClickActions() {
        setAction(31, event -> {
            event.getWhoClicked().closeInventory();
        });
        setAction(4, event -> {
            startChat(getOwner());
        });
        setAction(13, event -> {
            if (getOwner().getStringFromItemInHand("non-legacy").equals("true")) {
                new ItemDescriptionGUI(getOwner()).open();
            } else {
                getOwner().sendMessage("&cNeed to set a name or rarity before doing this.");
            }
        });
        setAction(15, event -> {
            if (getOwner().getStringFromItemInHand("non-legacy").equals("true")) {
                new AbilitySelectorGUI(getOwner()).open();
            } else {
                getOwner().sendMessage("&cNeed to set a name or rarity before doing this.");
            }
        });
        setAction(22, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            if (player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                new TagsEditorGUI(player).open();
            } else {
                player.sendMessage("§cYour item must have a displayname and lore first in order to set it's tags!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
            }
        });
        setAction(14, event -> {
            new StatsEditorGUI(getOwner()).open();
        });
        setAction(12, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            new RaritiesGUI(getOwner()).open();
        });
        setAction(11, event -> {
            SBPlayer player = new SBPlayer((Player) event.getWhoClicked());
            if (player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()) {
                if (Stream.of(Material.STAINED_CLAY, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.INK_SACK, Material.WOOL, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE).anyMatch(material -> player.getItemInHand().getType().equals(material))) {
                    player.closeInventory();
                    player.performCommand("dye");
                } else {
                    player.sendMessage("§cThat isn't a dyeable item!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
                }
            } else {
                player.sendMessage("§cYour item must have a displayname and lore first in order to set it's color!");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
            }
        });
        setAction(20,event -> {
            if (getOwner().getStringFromItemInHand("non-legacy").equals("true")) {
                new ItemTypeGUI(getOwner()).open();
            } else {
                getOwner().sendMessage("&cNeed to set a name or rarity before doing this.");
            }
        });
        setAction(21,event -> {
            new PetBuilderGUI(getOwner()).open();
        });
        setAction(40,event -> {
            getOwner().closeInventory();
        });
        setAction(23, event -> {
            if (getOwner().getStringFromItemInHand("non-legacy").equals("true")) {
                if (getOwner().getStringFromItemInHand("type") != null) {
                    new EnchantGUI(getOwner()).open();
                } else {
                    getOwner().sendMessage("&cYou need to set an item type before doing this!");
                }
            } else {
                getOwner().sendMessage("&cNeed to set a name or rarity before doing this.");
            }
        });
        return true;
    }

    @Override
    public String getTitle() {
        return "Auto Item Creator";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void setItems() {
        setMenuGlass();

        setItem(40, makeColorfulItem(Material.BARRIER, "§cClose", 1, 0));
        setItem(4, makeColorfulItem(Material.NAME_TAG, "§aRename Item", 1, 0,
                SUtil.colorize("&7Rename the item you have", "&7in your hand!", "", "&eClick to rename!")));

        setItem(13, makeColorfulItem(Material.PAPER, "§aEdit Item Lore", 1, 0, SUtil.colorize("&7Edit the lore of the item you", "&7have in your hand!", "", "&eClick to edit!")));
        setItem(14, makeColorfulItem(Material.GOLDEN_APPLE, "§aEdit Item Stats", 1, 0, SUtil.colorize("&7Edit the stats the item has!", "&7Including Defense, Health, and Intelligence!", "", "&eClick to edit!")));
        setItem(12, makeColorfulItem(Material.PAINTING, "§aSet item Rarity", 1, 0, SUtil.colorize("&7Set the rarity of your item", "&7you can choose anything", "&7between: &fCommon&7, &aUncommon", "&9Rare&7, &5Epic&7, &6Legendary&7,", "&dMythic&7, &cSpecial&7.", "", "§cNote: The last line of", "§clore in your item will", "§cturn into the rarity name.", "", "&eClick to set!")));
        setItem(15, makeColorfulItem(Material.GLOWSTONE_DUST, "§aSet item ability", 1, 0, SUtil.colorize("&7Create your own custom ability!", "&7Using the Base Abilites or", "&7the Advanced functions!", "", "&eClick to create!")));
        setItem(21,makeColorfulItem(Material.BARRIER,"&aPet Builder",1,0,SUtil.colorize("&7Create your own custom pet!","","&eClick to create!")));
        setItem(23, makeColorfulItem(Material.ENCHANTED_BOOK, "§aEdit Item Enchants", 1, 0, SUtil.colorize("&7Edit the enchants the item has!", "", "&eClick to edit!")));
        setItem(22, makeColorfulItem(Material.BOOK_AND_QUILL, "§aEdit Item Tags", 1, 0, SUtil.colorize("&7Edit the tags the item has!", "&7Including Unbreakable, Enchant tag,", "&7Glowing tag, and the Damage Tag!", "", "&eClick to edit!")));
        setItem(11, makeColorfulItem(Material.INK_SACK, "§aSet item color", 1, 5, SUtil.colorize("&7Edit the color of the item!", "&7you can change any item", "&7from: &aWool &7, &aStained_Clay &7, ", "&aStained_Glass_Panes &7, &aStained_Glass  ", "&7or &aINK_SACK(DYES)")));
        setItem(20,makeColorfulItem(Material.GOLD_AXE,"&aSet item type",1,0,"&7Set the item type!","","&eClick to set the item type!"));

    }

    private void startChat(Player p) {
        SBItemBuilder item = new SBItemBuilder(p.getItemInHand());
        p.closeInventory();
        PlayerChatInput.PlayerChatInputBuilder<String> builder = new PlayerChatInput.PlayerChatInputBuilder<>(SBX.getInstance(), p);
        builder.onExpireMessage(colorize("&cYou failed to set the name after 3 minute.")).sendValueMessage(colorize("&7Please input the name of the item!" + "\n&eSay &ccancel &eto stop."));
        builder.setValue((player, str) -> {
            return str;
        });
        builder.isValidInput((player, str) -> {
            if (str.isEmpty()) {
                getOwner().sendMessage("&cThat is not a valid input!");
                new ItemCreatorGUIMain(getOwner()).open();
                return false;
            }
            for (String s : PROFANITIES) {
                if (ChatColor.stripColor(str).toLowerCase().contains(s)) {
                    player.sendMessage("§cYou cannot use that word in your name!");
                    new ItemCreatorGUIMain(getOwner()).open();
                    return false;
                }
            }
            return true;
        }).onFinish((player, str) -> {
            player.setItemInHand(item.name(str).build());
            new ItemCreatorGUIMain(getOwner()).open();
        }).onInvalidInput(((player, s) -> true)).expiresAfter(3600).onExpire((player -> {
            new ItemCreatorGUIMain(getOwner()).open();
        })).toCancel("cancel").onCancel((player -> {
            getOwner().sendMessage("&cCanceled!");
            new ItemCreatorGUIMain(getOwner()).open();
        }));
        builder.build().start();
    }
}
