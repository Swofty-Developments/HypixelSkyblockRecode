package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.Entities;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.Particles;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FunctionsEditorGUI extends NormalGUI {
    private final int index;
    private final String function;
    private final int count;
    private final boolean update;
    private final Particles particle;
    private final Sound sound;
    public FunctionsEditorGUI(SBPlayer owner, String fuction, int index, int count, boolean update, Particles particle) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        this.particle = particle;
        sound = null;
    }
    public FunctionsEditorGUI(SBPlayer owner, String fuction, int index, int count, boolean update, Entities entityType) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        this.particle = null;
        sound = null;
    }
    public FunctionsEditorGUI(SBPlayer owner, String fuction, int index, int count, boolean update, Sound sound) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        this.sound = sound;
        particle = null;
    }
    public FunctionsEditorGUI(SBPlayer owner, String fuction, int index, int count, boolean update, String headTexture) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        this.sound = null;
        particle = null;
    }
    public FunctionsEditorGUI(SBPlayer owner, String fuction, int index, int count, boolean update) {
        super(owner);
        this.index = index;
        this.function = fuction;
        this.count = count;
        this.update = update;
        particle = null;
        sound = null;
    }

    public FunctionsEditorGUI(SBPlayer owner, String function, int index, int count, boolean update, Material projectileMat) {
        super(owner);
        this.index = index;
        this.function = function;
        this.count = count;
        this.update = update;
        this.sound = null;
        particle = null;
    }


    @Override
    public String getTitle() {
        return "Editing - " + function;
    }

    @Override
    public int getRows() {
        return 4;
    }


    @Override
    public void handleMenu(InventoryClickEvent event) {
        if(event.getClickedInventory().equals(getOwner().getInventory())) return;
        event.setCancelled(true);
        SBPlayer player = getOwner();
        switch (event.getSlot()) {
            case 31: {
                new FunctionsGUI(getOwner(), index).open();
                break;
            }
        }
        switch (function) {
            case "Teleport Function": {
                switch (event.getSlot()) {
                    case 13: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 100) {
                                    player.sendMessage("§cThe range cannot be more than 100!");
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 0) {
                                    player.sendMessage("§cThe range cannot be less than 0!");
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isInt(event.getName()) && event.getName() != null) {
                                    if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.TELEPORT_RANGE)) {
                                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_RANGE, count, event.getName()));
                                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));
                                    } else {
                                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_RANGE, count, event.getName()));
                                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));
                                    }
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your range", 1, 0));
                        gui.open();

                        break;
                    }
                    case 14: {
                        if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                            if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.TELEPORT_RANGE)) {
                                if (event.getClick().equals(ClickType.RIGHT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_MESSAGE, count, "False"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));
                                }
                                if (event.getClick().equals(ClickType.LEFT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_MESSAGE, count, "True"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));
                                }
                            } else {
                                player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                                if (event.getClick().equals(ClickType.RIGHT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_MESSAGE, count, "False"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));

                                }
                                if (event.getClick().equals(ClickType.LEFT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.TELEPORT_MESSAGE, count, "True"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Teleport Function"));
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case "Implosion Function": {
                switch (event.getSlot()) {
                    case 13: {
                        AnvilGUI gui = new AnvilGUI(player, event1 -> {
                            if (!NumUtils.isInt(event1.getName())) {
                                player.sendMessage("§cThat's not a valid number!");
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                return;
                            }
                            if (Integer.parseInt(event1.getName()) > 15) {
                                player.sendMessage("§cThe range cannot be more than 15!");
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                player.closeInventory();
                                return;
                            }
                            if (Integer.parseInt(event1.getName()) < 0) {
                                player.sendMessage("§cThe range cannot be less than 0!");
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                player.closeInventory();
                                return;
                            }
                            if (NumUtils.isInt(event1.getName()) && event1.getName() != null) {
                                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID) && !AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.IMPLOSION_MESSAGE)) {
                                    player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_RANGE, count, event1.getName()));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));
                                } else {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_RANGE, count, event1.getName()));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));
                                }
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                            } else {
                                invalidNumberError(event1, player);
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your range", 1, 0));
                        gui.open();

                        break;
                    }
                    case 14: {
                        if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                            if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.IMPLOSION_RANGE)) {
                                if (event.getClick().equals(ClickType.RIGHT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_MESSAGE, count, "False"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));
                                }
                                if (event.getClick().equals(ClickType.LEFT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_MESSAGE, count, "True"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));
                                }
                            } else {
                                player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                                if (event.getClick().equals(ClickType.RIGHT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_MESSAGE, count, "False"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));

                                }
                                if (event.getClick().equals(ClickType.LEFT)) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.IMPLOSION_MESSAGE, count, "True"));
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Implosion Function"));
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case "Particle Function": {
                switch (event.getSlot()) {
                    case 12: {
                        if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID) && (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE))) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        } else {
                            player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        }
                        new ParticleShooterGUI(getOwner(), function, index, count, update, particle).open();
                        break;
                    }
                    case 13: {
                        if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID) && (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE))) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        } else {
                            player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        }
                        new ParticleChooserGUI(getOwner(), index, count, update).open();
                        break;
                    }
                    case 14: {
                        if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID) && (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE))) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        } else {
                            player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_TYPE, index, particle.name()));
                        }
                        new ShapeSlectorGUI(getOwner(), function, index, count, update, particle).open();
                        break;
                    }
                }
                break;
            }
            case "Sound Function": {
                switch (event.getSlot()){
                    case 13: {
                        if(event.getClick().isLeftClick()) {
                            new SoundChooserGUI(getOwner(), index, count, update, sound).open();
                        } else if (event.getClick().isRightClick()) {
                            Sound sound1 = Sound.valueOf(String.valueOf(AbilityData.retrieveFunctionData(EnumFunctionsData.SOUND_TYPE, player.getItemInHand(), index, count)));
                            float volume = AbilityData.floatFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_VOLUME, index, count, 1.0F);
                            float pitch = AbilityData.floatFromFunction(player.getItemInHand(), EnumFunctionsData.SOUND_PITCH, index, count, 0.5F);
                            player.playSound(player.getLocation(), sound1, pitch, volume);
                        }
                        break;
                    }
                    case 11: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isFloat(event.getName())) {
                                    player.sendMessage("§cThat's not a valid float!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) > 2.0) {
                                    player.sendMessage("§cThe pitch cannot be more than 2.0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) < 0.5) {
                                    player.sendMessage("§cThe pitch cannot be less than 0.5!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.SOUND_PITCH, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your pitch", 1, 0));
                        gui.open();

                        break;
                    }
                    case 12: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isFloat(event.getName())) {
                                    player.sendMessage("§cThat's not a valid float!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) > 10.0) {
                                    player.sendMessage("§cThe volume cannot be more than 10.0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) < 1.0) {
                                    player.sendMessage("§cThe volume cannot be less than 1.0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.SOUND_VOLUME, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your volume", 1, 0));
                        gui.open();

                        break;
                    }
                    case 14: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 30) {
                                    player.sendMessage("§cThe volume cannot be more than 30!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 1) {
                                    player.sendMessage("§cThe amount cannot be less than 1!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isInt(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.SOUND_AMOUNT, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your amount", 1, 0));
                        gui.open();

                        break;
                    }
                    case 15: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isFloat(event.getName())) {
                                    player.sendMessage("§cThat's not a valid float!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) > 5.0) {
                                    player.sendMessage("§cThe delay cannot be more than 5.0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Float.parseFloat(event.getName()) < 0.0) {
                                    player.sendMessage("§cThe delay cannot be less than 0.0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.SOUND_DELAY, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your delay", 1, 0));
                        gui.open();

                        break;
                    }
                }
                break;
            }
            case "Head Shooter Function": {
                switch (event.getSlot()){
                    case 13: {
                        new HeadChooserGUI(getOwner(), index, count, update).open();
                        break;
                    }
                    case 11: {
                        if(event.isLeftClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_DAMAGE_ENTITY, count, "True"));
                            setItems();
                        } else if (event.isRightClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_DAMAGE_ENTITY, count, "False"));
                            setItems();
                        }
                        break;
                    }
                    case 12: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 30) {
                                    player.sendMessage("§cThe range cannot be more than 30!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 0) {
                                    player.sendMessage("§cThe range cannot be less than 0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_RANGE, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your range", 1, 0));
                        gui.open();
                        break;
                    }
                    case 14: {
                        if(event.isLeftClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_KICKBACK, count, "True"));
                            setItems();
                        } else if (event.isRightClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_KICKBACK, count, "False"));
                            setItems();
                        }
                        break;
                    }
                    case 15: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 15) {
                                    player.sendMessage("§cThe range cannot be more than 15!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 0) {
                                    player.sendMessage("§cThe range cannot be less than 0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_KICKBACK_RANGE, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your kickback range", 1, 0));
                        gui.open();
                        break;
                    }
                    case 20: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 100000) {
                                    player.sendMessage("§cThe range cannot be more than 100,000!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 0) {
                                    player.sendMessage("§cThe range cannot be less than 0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.HEAD_SHOOTER_BASE_DAMAGE, count, event.getName()));
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                } else {
                                    invalidNumberError(event, player);
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                }
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your base damage", 1, 0));
                        gui.open();
                        break;
                    }

                }
                break;
            }
            case "Projectile Shooter Function": {
                switch (event.getSlot()){
                    case 13: {
                        new ProjectileChooserGUI(getOwner(), index, count, update).open();
                        break;
                    }
                    case 11: {
                        if(event.isLeftClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, count, "True"));
                            setItems();
                        } else if (event.isRightClick()) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, count, "False"));
                            setItems();
                        }
                        break;
                    }
                    case 20: {
                        AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                                if (!NumUtils.isInt(event.getName())) {
                                    player.sendMessage("§cThat's not a valid number!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) > 100000) {
                                    player.sendMessage("§cThe range cannot be more than 100,000!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (Integer.parseInt(event.getName()) < 0) {
                                    player.sendMessage("§cThe range cannot be less than 0!");
                                    Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                                    player.closeInventory();
                                    return;
                                }
                                if (NumUtils.isFloat(event.getName()) && event.getName() != null) {
                                    player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PROJECTILE_SHOOTER_BASE_DAMAGE, count, event.getName()));
                                } else {
                                    invalidNumberError(event, player);
                                }
                                Bukkit.getScheduler().runTaskLater(SBX.getInstance(), FunctionsEditorGUI.super::open, 2);
                            }
                        });

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeColorfulItem(Material.PAPER, "Enter your base damage", 1, 0));
                        gui.open();
                        break;
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
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        switch (function) {
            case "Teleport Function": {
                setItem(13, makeColorfulItem(Material.ENDER_PEARL, "&aTeleport Range", 1, 0, "&7Edit the range of the\n&bTeleport Function&7!\n&7Maximum: &a100\n&7Minimum: &a0\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.FEATHER,"&aToggle message", 1, 0, "&7Turn on and off the\n&bTeleport Function &7message!\n\n&bRight-click to disable!\n&eLeft-click to enable!"));
                break;
            }
            case "Implosion Function": {
                setItem(13, makeColorfulItem(Material.BOOK_AND_QUILL, "&aImplosion Range", 1, 0, "&7Edit the range of the\n&bImplosion Function&7!\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.FEATHER,"&aToggle message", 1, 0, "&7Turn on and off the\n&bImplosion Function &7message!\n\n&bRight-click to disable!\n&eLeft-click to enable!"));
                break;
            }
            case "Particle Function": {
                setItem(13, makeColorfulItem(particle.getB(),"&aCurrently edited particle",1,0,"&7Currently editing:\n&b" + particle.name()+"\n\n&eClick to change!"));
                setItem(12, makeColorfulItem(Material.IRON_BLOCK, "&aParticle shooting", 1, 0, "&7Turn on and off\n&7Shooting, set the\n&7range and set if\n&7it can damage entities\n&7for the &bParticle Function\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.WATCH, "&aShape of Particle", 1, 0, "&7Set the shape of the particles\n&7played\n&7Includes: Circle, Oval, On Player\n&7and Square\n\n&eClick to set!"));
                break;
            }
            case "Sound Function": {
                setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aCurrently edited sound", 1, 0, "&7Currently editing:\n&b" + sound.name()+"\n\n&eLeft-Click to change!\n&bRight-Click to play!"));
                setItem(11, makeColorfulItem(Material.STICK, "&aSet the pitch", 1, 0, "&7Set the pitch of the\n&7sound played!\n\n&7Maximum: &a2.0\n&7Minimum: &a0.5\n\n&eClick to set!"));
                setItem(12, makeColorfulItem(Material.IRON_BLOCK, "&aSet the volume", 1, 0, "&7Set the volume of the\n&7sound played!\n\n&7Maximum: &a10.0\n&7Minimum: &a1.0\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.BOOK_AND_QUILL, "&aSet the amount", 1, 0, "&7Set the amount of the\n&7sound played!\n\n&7Maximum: &a30\n&7Minimum: &a1\n\n&eClick to set!"));
                setItem(15, makeColorfulItem(Material.WATCH, "&aSet the delay", 1, 0, "&7Set the delay of the\n&7sound played!\n\n&7Maximum: &a5.0\n&7Minimum: &a0.0\n\n&eClick to set!"));
                break;
            }
            case "Head Shooter Function": {
                ItemStack playerItem = getOwner().getItemInHand();
                if(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_TYPE, index, count, null) == null) {
                    setItem(13, makeColorfulItem(Material.SKULL_ITEM, "&aSet the head", 1, 3, "&7Set the head of the\n&bhead shooter function&7!\n&eClick to set!"));
                } else {
                    setItem(13, makeColorfulSkullItem(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_TYPE, index, count, null), "&aSet the head", 1, "&7Set the head of the\n&bhead shooter function&7!\n\n&eClick to set!"));
                }
                setItem(11, makeColorfulItem(Material.IRON_SWORD, "&aToggle Entity Damage", 1, 0, "&7Toggle if the spawned\n&7head will damage an\n&7entity\n\n&eLeft-Click to enable!\n&bRight-Click to disable!"));
                setItem(12, makeColorfulItem(Material.STICK, "&aSet the range", 1, 0, "&7Set the range of the\n&7head spawned!\n&7Maximum: &a30\n&7Minimum: &a0\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.COMMAND, "&aToggle kickback", 1, 0, "&7Toggle if the head,\n&7when it hits the ground\n&7to cause kickback!\n\n&eLeft-Click to enable\n&bRight-Click to disable!"));
                if(AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_KICKBACK, index, count, false)) {
                    setItem(15, makeColorfulItem(Material.ENDER_PORTAL_FRAME, "&aSet the kickback range", 1, 0, "&7Set the range of the\n&7kickback produced!\n&7Maximum: &a15\n&7Minimum: &a0\n\n&eClick to set!"));
                } else {
                    setItem(15, FILLER_GLASS);
                }
                if(AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_DAMAGE_ENTITY, index, count, false)) {
                    setItem(20, makeColorfulItem(Material.IRON_BLOCK, "&aSet the base damage", 1, 0, "&7Set the base damage of the\n&7head spawned!\n&7Maximum: &a100,000\n&7Minimum: &a1000\n\n&eClick to set!"));
                } else {
                    setItem(20, FILLER_GLASS);
                }
                break;
            }
            case "Projectile Shooter Function": {
                ItemStack playerItem = getOwner().getItemInHand();
                if(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, count, null) == null) {
                    setItem(13, makeColorfulItem(Material.IRON_SWORD, "&aSet the projectile", 1, 0, "&7Set the projectile of the\n&bprojectile shooter function&7!\n&eClick to set!"));
                } else {
                    setItem(13, makeColorfulItem(Material.valueOf(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, count, null)), "&aSet the projectile", 1,0, "&7Set the projectile of the\n&bprojectile shooter function&7!\n\n&eClick to set!"));
                }
                setItem(11, makeColorfulItem(Material.IRON_SWORD, "&aToggle Entity Damage", 1, 0, "&7Toggle if the spawned\n&7projectile will damage an\n&7entity\n\n&eLeft-Click to enable!\n&bRight-Click to disable!"));
                setItem(15, FILLER_GLASS);

                if(AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, index, count, false)) {
                    setItem(20, makeColorfulItem(Material.IRON_BLOCK, "&aSet the base damage", 1, 0, "&7Set the base damage of the\n&7projectile spawned!\n&7Maximum: &a100,000\n&7Minimum: &a1000\n\n&eClick to set!"));
                } else {
                    setItem(20, FILLER_GLASS);
                }
                break;
            }
        }
    }

    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }
}
