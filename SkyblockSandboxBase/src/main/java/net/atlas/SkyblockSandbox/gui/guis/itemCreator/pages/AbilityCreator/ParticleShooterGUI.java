package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator;

import net.atlas.SkyblockSandbox.gui.SBGUI;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
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

public class ParticleShooterGUI extends SBGUI {
    private final int index;
    private final int count;
    private final boolean update;
    private final Particles particle;
    private final String function;
    public ParticleShooterGUI(SBPlayer owner, String function, int index, int count, boolean update, Particles particle) {
        super(owner);
        this.index = index;
        this.count = count;
        this.update = update;
        this.particle = particle;
        this.function = function;
    }

    @Override
    public String getTitle() {
        return "Item Functions";
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = playerMenuUtility.getOwner();
        switch (event.getSlot()) {
            case 31: {
                new FunctionsCreatorGUI(new PlayerMenuUtility(player), index, count, update).open();
                break;
            }
            case 14: {
                if(event.getCurrentItem().equals(FILLER_GLASS)) return;

                AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                    @Override
                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                        if (!ItemUtilities.isInteger(event.getName())) {
                            player.sendMessage("§cThat's not a valid number!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            return;
                        }
                        if (Integer.parseInt(event.getName()) > 15) {
                            player.sendMessage("§cThe range cannot be more than 15!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                            player.closeInventory();
                            return;
                        }
                        if (Integer.parseInt(event.getName()) < 0) {
                            player.sendMessage("§cThe range cannot be less than 0!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                            player.closeInventory();
                            return;
                        }
                        if (ItemUtilities.isInteger(event.getName()) && event.getName() != null) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOT_RANGE, count, event.getName()));
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                        } else {
                            invalidNumberError(event, player);
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                        }
                    }
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeItem(Material.PAPER, "Enter your range", 1, 0));
                gui.open();
                break;
            }
            case 13: {
                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOT_RANGE)) {
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "False"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                        if (event.getClick().equals(ClickType.LEFT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "True"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                    } else {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "False"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();

                        }
                        if (event.getClick().equals(ClickType.LEFT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_SHOOTING, count, "True"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                    }
                }
                break;
            }
            case 12: {
                if(event.getCurrentItem().equals(FILLER_GLASS)) return;

                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_TYPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHAPE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOT_RANGE) || AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.PARTICLE_SHOOTING)) {
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "False"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                        if (event.getClick().equals(ClickType.LEFT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "True"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                    } else {
                        player.setItemInHand(AbilityData.removeFunction(player.getItemInHand(), index, count, player));
                        if (event.getClick().equals(ClickType.RIGHT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "False"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();

                        }
                        if (event.getClick().equals(ClickType.LEFT)) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_ENTITY, count, "True"));
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                            setItems();
                        }
                    }
                }
                break;
            }
            case 4: {
                if(event.getCurrentItem().equals(FILLER_GLASS)) return;

                AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
                    @Override
                    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                        if (!ItemUtilities.isFloat(event.getName())) {
                            player.sendMessage("§cThat's not a valid float!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            return;
                        }
                        if (Float.parseFloat(event.getName()) > 5) {
                            player.sendMessage("§cThe range cannot be more than 5!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                            player.closeInventory();
                            return;
                        }
                        if (Float.parseFloat(event.getName()) < 0) {
                            player.sendMessage("§cThe range cannot be less than 0!");
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
                            player.closeInventory();
                            return;
                        }
                        if (ItemUtilities.isFloat(event.getName()) && event.getName() != null) {
                            player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_DAMAGE_MULTIPLIER, count, event.getName()));
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                        } else {
                            invalidNumberError(event, player);
                            Bukkit.getScheduler().runTaskLater(Items.getInstance(), ParticleShooterGUI.super::open, 2);
                        }
                    }
                });

                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, makeItem(Material.PAPER, "Enter your range", 1, 0));
                gui.open();
                break;
            }
            case 5: {
                if (event.getCurrentItem().equals(FILLER_GLASS)) return;

                if (AbilityData.hasFunctionData(player.getItemInHand(), index, count, EnumFunctionsData.ID)) {
                    if (event.getClick().equals(ClickType.RIGHT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_MESSAGE, count, "False"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        setItems();
                    }
                    if (event.getClick().equals(ClickType.LEFT)) {
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.PARTICLE_MESSAGE, count, "True"));
                        player.setItemInHand(AbilityData.setFunctionData(player.getItemInHand(), index, EnumFunctionsData.NAME, count, "Particle Function"));
                        setItems();
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
        setFillerGlass();
        inventory.setItem(31, makeItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Function editor #" + count));
        inventory.setItem(13, makeColorfulItem(Material.IRON_BLOCK, "&aToggle Shooting", 1, 0, "&7Toggle shooting for the\n&bParticle Function&7!\n\n&eLeft-click to enable\n&bRight-click to disable"));
        if(AbilityData.retrieveFunctionData(EnumFunctionsData.PARTICLE_SHOOTING, playerMenuUtility.getOwner().getItemInHand(), index, count).equals("True")) {
            inventory.setItem(14, makeColorfulItem(Material.STICK, "&aShooting Range", 1, 0, "&7Set the range of the\n&7the &bParticle Function&7!\n\n&eClick to set!"));
            inventory.setItem(12, makeColorfulItem(Material.SKULL_ITEM, "&aToggle Entity Damage", 1, 0, "&7Toggle Entity Damage for\n&7the &bParticle Function&7!\n\n&eLeft-click to enable\n&bRight-click to disable"));
            inventory.setItem(4, makeColorfulItem(Material.IRON_AXE, "&aDamage Multiplier", 1, 0, "&7Set the damage multiplier\n&7of the &bParticle\n&bFunction&7!\n&7Default: &a0.3\n\n&eClick to set!"));
            inventory.setItem(5, makeColorfulItem(Material.ENDER_PORTAL_FRAME, "&aToggle Message", 1,0, "&7Toggle Message for\n&7the &bParticle Function&7!\n\n&eLeft-click to enable\n&bRight-click to disable"));
        } else {
            inventory.setItem(14, FILLER_GLASS);
            inventory.setItem(12, FILLER_GLASS);
            inventory.setItem(4, FILLER_GLASS);
            inventory.setItem(5, FILLER_GLASS);
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
