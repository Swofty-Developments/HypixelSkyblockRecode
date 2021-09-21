package net.atlas.SkyblockSandbox.abilityCreator;

import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import lombok.Setter;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.FunctionSelectorGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

import static net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions.GUIType.BOOLEAN;
import static net.atlas.SkyblockSandbox.abilityCreator.AdvancedFunctions.GUIType.ENUM;
import static net.atlas.SkyblockSandbox.item.ability.AbilityData.removeFunction;
import static net.atlas.SkyblockSandbox.item.ability.AbilityData.setFunctionData;
import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

@Getter
@Setter
public abstract class AdvancedFunctions extends NormalGUI {
    protected int aindex;
    protected int findex;
    protected Gui backGUI;
    private ItemStack back;
    private Object[] enumList;

    public AdvancedFunctions(SBPlayer owner) {
        super(owner);
    }

    @Override
    public String getTitle() {
        return name() + " Function";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setItems() {
        if (backGUI != null) {
            back = makeColorfulItem(Material.ARROW, "&aBack", 1, 0, "&8Go back to the " + backGUI.getInventory().getTitle() + ".");
        } else {
            back = makeColorfulItem(Material.ARROW, "&aBack", 1, 0, "&8Go back to the function creator page.");
        }
        setMenuGlass();
        setItem(49, back);
        for (int i = 0; i < itemList().size(); i++) {

            if (i < 1) {
                setItem(22+i,itemList().get(i));
            } else if(i < 3) {
                setItem(21+i,itemList().get(i));
            } else if(i < 6) {
                setItem(20+i,itemList().get(i));
            }
            else if(i < 12) {
                setItem(26+i,itemList().get(i));
            }
            else {
                throw new NullPointerException("Cannot have more than 12 variables"); //todo add more variable slots
            }
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        SBPlayer player = getOwner();
        ItemStack item = player.getItemInHand();
        event.setCancelled(true);
        if(event.getClickedInventory().equals(player.getInventory())) return;
        if(event.getCurrentItem().equals(FILLER_GLASS)) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(colorize("&aBack"))) {
            if(backGUI == null) {
                new FunctionSelectorGUI(player, aindex, findex).open();
                return;
            }
            backGUI.update();
            backGUI.open(player);
        }
        if(NBTUtil.getString(event.getCurrentItem(), "guiType").equals("") || NBTUtil.getString(event.getCurrentItem(), "name").equals("")) return;
        if(aindex == 0 || findex == 0) throw new NullPointerException("aIndex and fIndex Cant be unset or 0");
        for (String string : AbilityData.listFunctionData(item, aindex, findex)) {
            if (!string.equals("id") && !string.equals("name") && !string.contains(name())) {
                System.out.println(string);
                return;
            }
        }
        GUIType type = GUIType.valueOf(NBTUtil.getString(event.getCurrentItem(), "guiType"));
        String name = NBTUtil.getString(event.getCurrentItem(), "name");
        String min = NBTUtil.getString(event.getCurrentItem(), "min");
        String max = NBTUtil.getString(event.getCurrentItem(), "max");
        switch (type) {
            case BOOLEAN: {
                if (event.getClick().isLeftClick()) {
                    player.setItemInHand(setFunctionData(item, aindex, name() + "_" + name, findex, "True"));
                } else if (event.getClick().isRightClick()) {
                    player.setItemInHand(setFunctionData(item, aindex, name() + "_" + name, findex, "False"));
                }
                setData("name", name(), player, aindex, findex);
                break;
            }
            case STRING: {
                setData("name", name(), player, aindex, findex);
                AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
                    if (event1.getName() == "" || event1.getName() == null) {
                        player.sendMessage("§cThat's not a valid string!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        return;
                    }
                    player.setItemInHand(setFunctionData(item, aindex, name() + "_" + name, findex, event1.getName()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateItems();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your " + name, 1, 0));
                gui.open();

                break;
            }
            case INT: {
                setData("name", name(), player, aindex, findex);
                AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
                    if (!NumUtils.isInt(event1.getName())) {
                        player.sendMessage("§cThat's not a valid number!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        return;
                    }
                    if (Integer.parseInt(event1.getName()) > Math.round(Float.parseFloat(max))) {
                        player.sendMessage("§cThe " + name + " cannot be more than " + max + "!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (Integer.parseInt(event1.getName()) < Math.round(Float.parseFloat(min))) {
                        player.sendMessage("§cThe " + name + " cannot be less than " + min + "!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (!NumUtils.isInt(event1.getName()) && event1.getName() == null) invalidNumberError(event1, player);
                    player.setItemInHand(setFunctionData(item, aindex, name() + "_" + name, findex, event1.getName()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateItems();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your " + name, 1, 0));
                gui.open();

                break;
            }
            case FLOAT: {
                setData("name", name(), player, aindex, findex);
                AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
                    if (!NumUtils.isFloat(event1.getName())) {
                        player.sendMessage("§cThat's not a valid number!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        return;
                    }
                    if (Float.parseFloat(event1.getName()) > Float.parseFloat(max)) {
                        player.sendMessage("§cThe " + name + " cannot be more than " + max + "!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (Float.parseFloat(event1.getName()) < Float.parseFloat(min)) {
                        player.sendMessage("§cThe " + name + " cannot be less than " + min + "!");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateItems();
                            }
                        }.runTaskLater(SBX.getInstance(), 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (!NumUtils.isFloat(event1.getName()) && event1.getName() == null) invalidNumberError(event1, player);
                    player.setItemInHand(setFunctionData(item, aindex, name() + "_" + name, findex, event1.getName()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateItems();
                        }
                    }.runTaskLater(SBX.getInstance(), 2);
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your " + name, 1, 0));
                gui.open();
                break;
            }
            case ENUM: {
                EnumGUI enumGui = new EnumGUI(player);
                enumGui.setBackGUI(this);
                enumGui.setAindex(aindex);
                enumGui.setFindex(aindex);
                enumGui.setEnumList(enumList);
                enumGui.setGuiMat(event.getCurrentItem().getType());
                enumGui.setFunctionName(name());
                enumGui.setGuiName(name);
                enumGui.open();
            }
        }
    }

    public static void handleFunctions(ClickType type) {

    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    public abstract void runnable();
    public abstract ArrayList<ItemStack> itemList();
    public abstract String name();
    public abstract String description();
    public abstract Material material();
    public abstract AdvancedFunctions getGUI();

    public enum GUIType {
        STRING,
        BOOLEAN,
        FLOAT,
        INT,
        ENUM,
        ITEM
    }

    protected ItemStack createItem(String name, GUIType type, Material material, String min, String max, Class Enum) {
        ItemStack item = null;
        if (type.equals(GUIType.INT)) {
            if (min == null || max == null) {
                throw new NullPointerException("Min and max needs to be set!");
            }
            item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&7Maximum: &a" + Math.round(Float.parseFloat(max)) + "\n&7Minimum: &a" + Math.round(Float.parseFloat(min)) + "\n\n&eClick to set!");
        } else if (type.equals(BOOLEAN)) {
            item = makeColorfulItem(material, "&aToggle " + name, 1, 0, "&7Turn on and off the\n&b" + name() + " Function &7" + name + "!\n\n&bRight-click to disable!\n&eLeft-click to enable!");
        } else if (type.equals(GUIType.FLOAT)) {
            if (min == null || max == null) {
                throw new NullPointerException("Min and max needs to be set!");
            }
            item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&7Maximum: &a" + Float.parseFloat(max) + "\n&7Minimum: &a" + Float.parseFloat(min) + "\n\n&eClick to set!");
        } else if (type.equals(GUIType.STRING)) {
            item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&eClick to set!");
        } else if (type.equals(ENUM)) {
            enumList = Enum.getEnumConstants();
            if (AbilityData.retrieveFunctionData(name() + "_" + name, getOwner().getItemInHand(), aindex, findex) == "") {
                item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&eClick to set!");
            } else {
                item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&7Currently selected: &a" + AbilityData.retrieveFunctionData(name() + "_" + name, getOwner().getItemInHand(), aindex, findex) + "\n\n&eClick to set!");
            }
        } else if (type.equals(GUIType.ITEM)) {
            item = makeColorfulItem(material, "&aSet the " + name, 1, 0, "&7Edit the " + name + " of the\n&b" + name() + " Function&7!\n\n&eClick to set!");
        }//todo maybe add more of these idk
        ItemStack step1 = NBTUtil.setString(item, type.name(), "guiType");
        ItemStack step3 = step1;
        if (min != null && max != null) {
            ItemStack step2 = NBTUtil.setString(step1, min, "min");
            step3 = NBTUtil.setString(step2, max, "max");
        }
        return NBTUtil.setString(step3, name, "name");
    }


    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }

    public void setData(String key, Object data, SBPlayer player, int aindex, int findex) {
        if(aindex > 5)
            throw new NullPointerException("Ability index can't be higher than 5!");
        if(findex > 5)
            throw new NullPointerException("Function count can't be higher than 3!");

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(player.getItemInHand());
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound ability = (attributes.getCompound("Abilities") != null ? attributes.getCompound("Abilities") : new NBTTagCompound());
        NBTTagCompound abilitySlot = (ability.getCompound("Ability_" + aindex) != null ? ability.getCompound("Ability_" + aindex) : new NBTTagCompound());
        NBTTagCompound function = (abilitySlot.getCompound("Functions") != null ? abilitySlot.getCompound("Functions") : new NBTTagCompound());
        NBTTagCompound functionSlot = (function.getCompound("Function_" + findex) != null ? function.getCompound("Function_" + findex) : new NBTTagCompound());

        functionSlot.setString(key, data.toString());
        function.set("Function_" + findex, functionSlot);
        functionSlot.setString("id", UUID.randomUUID().toString());
        abilitySlot.setString("id", UUID.randomUUID().toString());

        abilitySlot.set("Functions", function);
        ability.set("Ability_" + aindex, abilitySlot);
        attributes.set("Abilities", ability);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);
        player.setItemInHand(NBTUtil.setInteger(CraftItemStack.asBukkitCopy(nmsItem), 1, "hasAbility"));
    }


}
