package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.player.pets.PetBuilder;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.isTyping;
import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.storedPets;

public class PetBuilderGUI extends NormalGUI {
    public PetBuilderGUI(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return "Pet Builder";
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        if (!storedPets.containsKey(getOwner().getUniqueId())) {
            storedPets.put(getOwner().getUniqueId(), PetBuilder.init()
                    .texture("http://textures.minecraft.net/texture/")
                    .level(0)
                    .xp(0)
                    .name("Placeholder")
                    .rarity(Rarity.COMMON));
        }
        setItem(4, new SBItemStack(storedPets.get(getOwner().getUniqueId()).build()).refreshLore());
        setItem(31, makeColorfulItem(Material.ARROW, "&cGo Back", 1, 0, "&7To create an item"));
        setItem(12, makeColorfulItem(Material.ENDER_PORTAL_FRAME, "§aSet pet level", 1, 0,
                SUtil.colorize("", "&eClick to set the level!")));
        setItem(13, makeColorfulItem(Material.NAME_TAG, "§aSet pet name", 1, 0,
                SUtil.colorize("", "&eClick to set the name!")));
        setItem(14,makeColorfulItem(Material.STICK,"&aSet Pet Texture",1,0,"","&eClick to set the texture &lIN CHAT.","&cNOTE: &7You will need to paste a texture","&7from minecraft-heads.com."));
        setItem(22,makeColorfulItem(Material.BOOK_AND_QUILL,"&aSet pet perks!",1,0,"","&eClick to add pet perks!"));
        setItem(23, makeColorfulSkullItem("&aSet Pet Type!", "http://textures.minecraft.net/texture/49d0e833d9bda32f2d736d8c3c3be8b9b964addd59357c12263ffccb8b8dae", 1, "&7Click to set the pet type!"));
        setItem(35,makeColorfulItem(Material.STAINED_CLAY,"&aFinish pet!",1, DyeColor.GREEN.getData(),"&7Finish and get the stored pet!","","&eClick to finish!","&bRight-click to clear!"));
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public boolean setClickActions() {
        setAction(12, event -> {
            Player player = getOwner().getPlayer();
            AnvilGUI gui = new AnvilGUI(getOwner().getPlayer(), event1 -> {
                if (!NumUtils.isInt(event1.getName())) {
                    invalidNumberError(event1, player);
                    return;
                }
                int lvl = Integer.parseInt(event1.getName());
                storedPets.get(getOwner().getUniqueId()).level(lvl);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new PetBuilderGUI(getOwner()).open();
                    }
                }.runTaskLater(SBX.getInstance(), 1);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter value", 1, 0));
            gui.open();
        });

        setAction(13, event -> {
            Player player = getOwner().getPlayer();
            AnvilGUI gui = new AnvilGUI(getOwner().getPlayer(), event1 -> {
                storedPets.get(getOwner().getUniqueId()).name(SUtil.colorize(event1.getName()));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new PetBuilderGUI(getOwner()).open();
                    }
                }.runTaskLater(SBX.getInstance(), 1);
            });

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter value", 1, 0));
            gui.open();
        });
        setAction(14,event -> {
            isTyping.put(getOwner().getUniqueId(),true);
            getOwner().closeInventory();
            getOwner().sendMessage(SUtil.colorize("&a&lPlease type the texture ID from the bottom of the minecraft-heads page in chat now. (Example: https://i.imgur.com/T93IgIk.png)"));
        });
        setAction(22, event -> {
            new PetPerkGUI(getOwner()).open();
        });

        setAction(31, event -> {
            new ItemCreatorGUIMain(getOwner()).open();
        });
        setAction(23,event -> {
            petTypeGui(getOwner());
        });
        setAction(35,event -> {
            if(event.getClick()== ClickType.RIGHT) {
                storedPets.remove(getOwner().getUniqueId());
                getOwner().playSound(getOwner().getLocation(),Sound.CAT_MEOW,1,1);
                updateItems();
            } else {
                getOwner().playSound(getOwner().getLocation(),Sound.ITEM_PICKUP,1,1);
                getOwner().getInventory().addItem(new SBItemStack(storedPets.get(getOwner().getUniqueId()).build()).refreshLore());
            }
        });
        return true;
    }

    public AnvilGUI petTypeGui(SBPlayer player) {
        AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
            storedPets.get(getOwner().getUniqueId()).petType(event1.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateItems();
                }
            }.runTaskLater(SBX.getInstance(), 1);
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Set Pet Type", 1, 0));
        gui.open();
        return gui;
    }


    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }
}
