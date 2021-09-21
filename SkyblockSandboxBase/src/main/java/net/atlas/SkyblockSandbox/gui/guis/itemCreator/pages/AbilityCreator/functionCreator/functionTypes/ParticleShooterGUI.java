package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.FunctionSelectorGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ParticleShooterGUI extends NormalGUI {
    private final int index;
    private final int count;
    private final AbilityValue.FunctionType function;

    public ParticleShooterGUI(SBPlayer owner, AbilityValue.FunctionType function, int index, int count) {
        super(owner);
        this.index = index;
        this.count = count;
        this.function = function;
    }

    @Override
    public String getTitle() {
        return "Particle Shooter";
    }

    @Override
    public int getRows() {
        return 4;
    }


    @Override
    public void handleMenu(InventoryClickEvent event) {
        SBPlayer player = getOwner();
        event.setCancelled(true);
        switch (event.getSlot()) {
            case 31: {
                new FunctionSelectorGUI(getOwner(), index, count).open();
                break;
            }
            case 14: {
                if (event.getCurrentItem().equals(FILLER_GLASS)) return;

                AnvilGUI gui = new AnvilGUI(player.getPlayer(), event1 -> {
                    if (!NumUtils.isInt(event1.getName())) {
                        player.sendMessage("§cThat's not a valid number!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        return;
                    }
                    if (Integer.parseInt(event1.getName()) > 15) {
                        player.sendMessage("§cThe range cannot be more than 15!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (Integer.parseInt(event1.getName()) < 0) {
                        player.sendMessage("§cThe range cannot be less than 0!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (NumUtils.isInt(event1.getName()) && event1.getName() != null) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOT_RANGE, count, event1.getName()));
                    } else {
                        invalidNumberError(event1, player);
                    }
                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your range", 1, 0));
                gui.open();
                break;
            }
            case 13: {
                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (!AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOT_RANGE)) {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                    }
                    if (event.getClick().equals(ClickType.RIGHT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "False"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                    if (event.getClick().equals(ClickType.LEFT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "True"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                }
                break;
            }
            case 12: {
                if (event.getCurrentItem().equals(FILLER_GLASS)) return;

                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (!AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOT_RANGE) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING)) {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                    }
                    if (event.getClick().equals(ClickType.RIGHT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "False"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                    if (event.getClick().equals(ClickType.LEFT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "True"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                }
                break;
            }
            case 4: {
                if (event.getCurrentItem().equals(FILLER_GLASS)) return;

                AnvilGUI gui = new AnvilGUI(player.getPlayer(), event12 -> {
                    if (!NumUtils.isFloat(event12.getName())) {
                        player.sendMessage("§cThat's not a valid float!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        return;
                    }
                    if (Float.parseFloat(event12.getName()) > 5) {
                        player.sendMessage("§cThe range cannot be more than 5!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (Float.parseFloat(event12.getName()) < 0) {
                        player.sendMessage("§cThe range cannot be less than 0!");
                        Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                        player.closeInventory();
                        return;
                    }
                    if (NumUtils.isFloat(event12.getName()) && event12.getName() != null) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_MULTIPLIER, count, event12.getName()));
                    } else {
                        invalidNumberError(event12, player);
                    }
                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), ParticleShooterGUI.super::open, 2);
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your range", 1, 0));
                gui.open();
                break;
            }
            case 5: {
                if (event.getCurrentItem().equals(FILLER_GLASS)) return;

                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (event.getClick().equals(ClickType.RIGHT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_MESSAGE, count, "False"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                    if (event.getClick().equals(ClickType.LEFT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_MESSAGE, count, "True"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        updateItems();
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean setClickActions() {
        return false;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Function editor #" + count));
        setItem(13, makeColorfulItem(Material.IRON_BLOCK, "&aToggle Shooting", 1, 0, "&7Toggle shooting for the\n&bParticle Function&7!\n\n&eLeft-click to enable\n&bRight-click to disable"));
        if (AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHOOTING, getOwner().getItemInHand(), index, count).equals("True")) {
            String enabled = "";
            if (AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, getOwner().getItemInHand(), index, count).equals("True")) {
                enabled = "&aTRUE";
            } else {
                enabled = "&cFALSE";
            }
            setItem(12, makeColorfulItem(Material.SKULL_ITEM, "&aToggle Entity Damage", 1, 0, "&7Toggle Entity Damage for\n&7the &bParticle Function&7!\n\n&7Enabled: " + enabled + "\n\n&eLeft-click to enable\n&bRight-click to disable"));
            setItem(14, makeColorfulItem(Material.STICK, "&aShooting Range", 1, 0, "&7Set the range of the\n&7the &bParticle Function&7!\n\n&eClick to set!"));
            setItem(4, makeColorfulItem(Material.IRON_AXE, "&aDamage Multiplier", 1, 0, "&7Set the damage multiplier\n&7of the &bParticle\n&bFunction&7!\n&7Default: &a0.3\n\n&eClick to set!"));
            setItem(5, makeColorfulItem(Material.ENDER_PORTAL_FRAME, "&aToggle Message", 1, 0, "&7Toggle Message for\n&7the &bParticle Function&7!\n\n&eLeft-click to enable\n&bRight-click to disable"));
        } else {
            setItem(14, FILLER_GLASS);
            setItem(12, FILLER_GLASS);
            setItem(4, FILLER_GLASS);
            setItem(5, FILLER_GLASS);
        }

    }


    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }
}
